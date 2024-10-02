package com.trade.coinwave.service;

import com.trade.coinwave.domain.OrderType;
import com.trade.coinwave.model.Order;
import com.trade.coinwave.model.User;
import com.trade.coinwave.model.Wallet;
import com.trade.coinwave.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    WalletService walletService;

    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet = walletRepository.findByUserId(user.getId());
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
            walletRepository.save(wallet);
        }
        return wallet;
    }

    @Override
    public Wallet addBalanceToUserWallet(User user, BigDecimal amount) {
        BigDecimal balance = getUserWallet(user).getBalance();
        BigDecimal newBalance = balance.add(amount);
        getUserWallet(user).setBalance(newBalance);

        return walletRepository.save(getUserWallet(user));
    }

    @Override
    public Wallet findUserWalletById(Long id) throws Exception {
        Optional<Wallet> wallet = walletRepository.findById(id);
        if (wallet.isPresent()) {
            return wallet.get();
        }
        throw new Exception("Wallet not found");
    }

    @Override
    public Wallet walletToWalletTransfer(User sender, Wallet receiver, BigDecimal amount) throws Exception {
        Wallet senderWallet = getUserWallet(sender);
        if (senderWallet.getBalance().compareTo(amount) < 0) {
            throw new Exception("Insufficient balance");
        }
        BigDecimal senderBalance = senderWallet
                .getBalance()
                .subtract(amount);
        senderWallet.setBalance(senderBalance);
        walletRepository.save(senderWallet);

        BigDecimal receiverBalance = receiver
                .getBalance()
                .add(amount);
        receiver.setBalance(receiverBalance);
        walletRepository.save(receiver);

        return senderWallet;
    }

    @Override
    public Wallet payOrderPayment(Order order, User user) throws Exception {
        Wallet wallet = getUserWallet(user);
        if (order.getOrderType().equals(OrderType.BUY)) {
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());
            if (newBalance.compareTo(order.getPrice()) < 0) {
                throw new Exception("Insufficient balance for this order");
            }
            wallet.setBalance(newBalance);
        } else if (order.getOrderType().equals(OrderType.SELL)) {
            BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);
        }

        walletRepository.save(wallet);
        return wallet;
    }
}
