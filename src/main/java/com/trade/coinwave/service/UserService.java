package com.trade.coinwave.service;

import com.trade.coinwave.domain.VerificationType;
import com.trade.coinwave.model.User;

public interface UserService {
    public User findUserProfileByJwtToken(String jwtToken) throws Exception;
    public User findUserByEmail(String email) throws Exception;
    public User findUserById(Long userId) throws Exception;
    public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user);
    public User updateUserPassword(User user, String newPassword);

}
