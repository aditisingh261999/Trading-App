package com.trade.coinwave.service;

import com.trade.coinwave.domain.VerificationType;
import com.trade.coinwave.model.User;
import com.trade.coinwave.model.VerificationCode;

public interface VerificationCodeService {
    VerificationCode sendVerificationCode(User user, VerificationType verificationType);
    VerificationCode getVerificationCode(Long id) throws Exception;
    VerificationCode findVerificationCodeByUser(Long userId);
    void deleteVerificationCode(VerificationCode verificationCode);

}
