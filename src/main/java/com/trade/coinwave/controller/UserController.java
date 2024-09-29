package com.trade.coinwave.controller;

import com.trade.coinwave.request.ForgetPasswordTokenRequest;
import com.trade.coinwave.domain.VerificationType;
import com.trade.coinwave.model.ForgetPasswordToken;
import com.trade.coinwave.model.User;
import com.trade.coinwave.model.VerificationCode;
import com.trade.coinwave.request.ResetPasswordRequest;
import com.trade.coinwave.response.ApiResponse;
import com.trade.coinwave.response.AuthResponse;
import com.trade.coinwave.service.EmailService;
import com.trade.coinwave.service.ForgetPasswordService;
import com.trade.coinwave.service.UserService;
import com.trade.coinwave.service.VerificationCodeService;
import com.trade.coinwave.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private ForgetPasswordService forgetPasswordService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile (@RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserProfileByJwtToken(token);
        if (user == null) {
            throw new Exception("Invalid token");
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/api/users/verification/{verficationType}/send-otp")
    public ResponseEntity<String> sendVerificationCode (@RequestHeader("Authorization") String token,
                                                      @PathVariable VerificationType verificationType) throws Exception {

        User user = userService.findUserProfileByJwtToken(token);

        VerificationCode verificationCode = verificationCodeService.findVerificationCodeByUser(user.getId());
        if (verificationCode == null) {
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        }

        if (verificationType.equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpToEmail(user.getEmail(), verificationCode.getOtp());
        }

        return new ResponseEntity<>("OTP successfully sent to email", HttpStatus.OK);

    }

    @PatchMapping("/api/user/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(@PathVariable String otp,
                                                              @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserProfileByJwtToken(token);
        VerificationCode verificationCode = verificationCodeService.findVerificationCodeByUser(user.getId());

        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL) ?
                verificationCode.getEmail() : verificationCode.getPhone();

        boolean isVerified = verificationCode.getOtp().equals(otp);
        User updatedUser = null;
        if (isVerified) {
            updatedUser = userService.enableTwoFactorAuthentication(
                    verificationCode.getVerificationType(),
                    sendTo,
                    user
            );

            verificationCodeService.deleteVerificationCode(verificationCode);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        throw new Exception("Invalid verification code");
    }

    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgetPasswordOtp (
            @RequestBody ForgetPasswordTokenRequest forgetPasswordTokenRequest) throws Exception {

        User user = userService.findUserByEmail(forgetPasswordTokenRequest.getSendTo());
        String otp = OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgetPasswordToken forgetPasswordToken = forgetPasswordService.findByUser(user.getId());
        if (forgetPasswordToken == null) {
            forgetPasswordService.createForgetPasswordToken(user, id, otp,
                    forgetPasswordTokenRequest.getVerificationType(),
                    forgetPasswordTokenRequest.getSendTo());
        }

        if (forgetPasswordTokenRequest.getVerificationType().equals(VerificationType.EMAIL)) {
            assert forgetPasswordToken != null;
            emailService.sendVerificationOtpToEmail(user.getEmail(), forgetPasswordToken.getOtp());
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(id);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);

    }

    @PatchMapping("/auth/user/reset-password/verify-otp/")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String id,
                                              @RequestBody ResetPasswordRequest resetPasswordRequest,
                                              @RequestHeader("Authorization") String token) throws Exception {

        ForgetPasswordToken forgetPasswordToken = forgetPasswordService.findById(id);

        boolean isVerified = forgetPasswordToken.getOtp().equals(resetPasswordRequest.getOtp());
        if (isVerified) {
            userService.updateUserPassword(forgetPasswordToken.getUser(), resetPasswordRequest.getPassword());
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("Password updated successfully");
            return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
        }
        throw new Exception("Invalid verification code");
    }

}
