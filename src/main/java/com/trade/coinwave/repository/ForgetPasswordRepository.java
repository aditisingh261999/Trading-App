package com.trade.coinwave.repository;

import com.trade.coinwave.model.ForgetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgetPasswordRepository extends JpaRepository<ForgetPasswordToken, String> {
    ForgetPasswordToken findByUserId(Long userId);
}
