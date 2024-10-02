package com.trade.coinwave.service;

import com.trade.coinwave.model.Order;
import com.trade.coinwave.model.User;
import com.trade.coinwave.model.Wallet;

import java.math.BigDecimal;

public interface WalletService {

    Wallet getUserWallet(User user);
    Wallet addBalanceToUserWallet(User user, BigDecimal amount);
    Wallet findUserWalletById(Long id) throws Exception;
    Wallet walletToWalletTransfer(User sender, Wallet receiver, BigDecimal amount) throws Exception;
    Wallet payOrderPayment(Order order, User user) throws Exception;

}
