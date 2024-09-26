package com.trade.coinwave.service;

import com.trade.coinwave.domain.VerificationType;
import com.trade.coinwave.model.ForgetPasswordToken;
import com.trade.coinwave.model.User;

public interface ForgetPasswordService {
    ForgetPasswordToken createForgetPasswordToken(User user,
                                                  String id, String otp,
                                                  VerificationType verificationType,
                                                  String sendTo);
    ForgetPasswordToken findById(String id);
    ForgetPasswordToken findByUser(Long userId);
    void deleteForgetPasswordToken(ForgetPasswordToken forgetPasswordToken);

}
