package com.trade.coinwave.controller;

import com.trade.coinwave.model.User;
import com.trade.coinwave.model.Wallet;
import com.trade.coinwave.model.Withdrawal;
import com.trade.coinwave.service.UserService;
import com.trade.coinwave.service.WalletService;
import com.trade.coinwave.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class WithdrawalController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/withdrawal/{amout}")
    public ResponseEntity<Withdrawal> withdrawalRequest(
            @PathVariable Long amount,
            @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserProfileByJwtToken(token);
        Wallet wallet = walletService.getUserWallet(user);

        Withdrawal withdrawal = withdrawalService.requestWithdrawal(BigDecimal.valueOf(amount), user);
        walletService.addBalanceToUserWallet(wallet.getUser(), wallet.getBalance());

        // implementation of wallet transaction service here

        return ResponseEntity.ok(withdrawal);

    }

    @PatchMapping("/api/admin/withdraw/{id}/proceed/{accept}")
    public ResponseEntity<Withdrawal> processWithdrawal(
            @PathVariable Long id,
            @PathVariable boolean accept,
            @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserProfileByJwtToken(token);
        Wallet wallet = walletService.getUserWallet(user);

        Withdrawal withdrawal = withdrawalService.processWithdrawal(id, accept);
        if (!accept) {
            walletService.addBalanceToUserWallet(wallet.getUser(), wallet.getBalance());
        }
        return ResponseEntity.ok(withdrawal);
    }

    @GetMapping("/api/withdrawal")
    public ResponseEntity<Withdrawal> getWithdrawalHistory(
            @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserProfileByJwtToken(token);
        List<Withdrawal> withdrawals = withdrawalService.getUserWithdrawalHistory(user);
        return ResponseEntity.ok(withdrawals.get(withdrawals.size() - 1));

    }

    @GetMapping("/api/admin/withdrawal/")
    public ResponseEntity<List<Withdrawal>> getAllWithdrawalRequest(
            @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserProfileByJwtToken(token);
        List<Withdrawal> withdrawalList = withdrawalService.getAllWithdrawals(user);
        return ResponseEntity.ok(withdrawalList);
    }

}
