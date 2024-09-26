package com.trade.coinwave.service;

import com.trade.coinwave.domain.VerificationType;
import com.trade.coinwave.model.ForgetPasswordToken;
import com.trade.coinwave.model.User;
import com.trade.coinwave.repository.ForgetPasswordRepository;
import com.trade.coinwave.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForgetPasswordServiceImpl implements ForgetPasswordService{

    @Autowired
    private ForgetPasswordRepository forgetPasswordRepository;

    @Override
    public ForgetPasswordToken createForgetPasswordToken(User user, String id, String otp, VerificationType verificationType, String sendTo) {
        ForgetPasswordToken forgetPasswordToken = new ForgetPasswordToken();
        forgetPasswordToken.setUser(user);
        forgetPasswordToken.setOtp(otp);
        forgetPasswordToken.setVerificationType(verificationType);
        forgetPasswordToken.setId(id);
        forgetPasswordToken.setSendTo(sendTo);

        return forgetPasswordRepository.save(forgetPasswordToken);
    }

    @Override
    public ForgetPasswordToken findById(String id) {
        Optional<ForgetPasswordToken> token = forgetPasswordRepository.findById(id);
        return token.orElse(null);
    }

    @Override
    public ForgetPasswordToken findByUser(Long userId) {
        return forgetPasswordRepository.findByUserId(userId);
    }

    @Override
    public void deleteForgetPasswordToken(ForgetPasswordToken forgetPasswordToken) {
        forgetPasswordRepository.delete(forgetPasswordToken);
    }
}
