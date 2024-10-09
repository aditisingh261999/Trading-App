package com.trade.coinwave.repository;

import com.trade.coinwave.model.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
    List <Withdrawal> findByUserId(long userId);
}
