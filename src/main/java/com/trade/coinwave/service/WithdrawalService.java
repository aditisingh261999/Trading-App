package com.trade.coinwave.service;

import com.trade.coinwave.model.User;
import com.trade.coinwave.model.Withdrawal;

import java.math.BigDecimal;
import java.util.List;

public interface WithdrawalService {

    Withdrawal requestWithdrawal(BigDecimal amount, User user);
    Withdrawal processWithdrawal(Long withdrawalId, boolean accept) throws Exception;
    List<Withdrawal> getUserWithdrawalHistory(User user);
    List<Withdrawal> getAllWithdrawals(User user);
}
