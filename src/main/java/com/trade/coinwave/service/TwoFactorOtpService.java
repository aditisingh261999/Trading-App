package com.trade.coinwave.service;

import com.trade.coinwave.model.TwoFactorOTP;
import com.trade.coinwave.model.User;

public interface TwoFactorOtpService {

    TwoFactorOTP createTwofactorOtp(User user, String otp, String jwt);

    TwoFactorOTP findByUser(Long userId);

    TwoFactorOTP findById(String Id);

    boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp);

    void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp);

}
