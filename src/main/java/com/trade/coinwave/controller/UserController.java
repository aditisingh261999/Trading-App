package com.trade.coinwave.controller;

import com.trade.coinwave.domain.VerificationType;
import com.trade.coinwave.model.TwoFactorAuth;
import com.trade.coinwave.model.User;
import com.trade.coinwave.model.VerificationCode;
import com.trade.coinwave.service.EmailService;
import com.trade.coinwave.service.UserService;
import com.trade.coinwave.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeService verificationCodeService;

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
    public ResponseEntity<User> enableTwoFactorAuthentication(@RequestHeader ("Authorization") String token,
                                                              @RequestParam String otp,
                                                              @RequestParam VerificationType verificationType) throws Exception {

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
                    user);

            verificationCodeService.deleteVerificationCode(verificationCode);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        throw new Exception("Invalid verification code");
    }
}
