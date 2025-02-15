package com.trade.coinwave.controller;

import com.trade.coinwave.model.*;
import com.trade.coinwave.service.OrderService;
import com.trade.coinwave.service.PaymentService;
import com.trade.coinwave.service.UserService;
import com.trade.coinwave.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private final WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    private final OrderService orderService;


    public WalletController(WalletService walletService, OrderService orderService) {
        this.walletService = walletService;
        this.orderService = orderService;
    }

    @GetMapping("/api/wallet")
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserProfileByJwtToken(token);
        return ResponseEntity.ok(walletService.getUserWallet(user));
    }

    @PutMapping("/api/wallet/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization") String token,
            @PathVariable Long walletId,
            @RequestBody WalletTransaction walletTransaction) throws Exception {

        User senderUser = userService.findUserProfileByJwtToken(token);
        Wallet receiverWallet = walletService.findUserWalletById(walletId);
        Wallet wallet = walletService.walletToWalletTransfer(
                senderUser,
                receiverWallet,
                walletTransaction.getAmount()
        );

        return ResponseEntity.ok(wallet);
    }

    @PutMapping("/api/wallet/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(
            @RequestHeader("Authorization") String token,
            @PathVariable Long orderId) throws Exception {

        User senderUser = userService.findUserProfileByJwtToken(token);
        Order order = orderService.getOrderById(orderId);
        Wallet wallet = walletService.payOrderPayment(order, senderUser);

        return ResponseEntity.ok(wallet);
    }

    @PutMapping("/api/wallet/deposit")
    public ResponseEntity<Wallet> addMoneyToWallet(
            @RequestHeader("Authorization") String token,
            @RequestParam(name = "order_id") Long orderId,
            @RequestParam(name = "payment_id") String paymentId) throws Exception {

        User user = userService.findUserProfileByJwtToken(token);
        Wallet wallet = walletService.getUserWallet(user);
        PaymentOrder order = paymentService.getPaymentOrderById(orderId);

        boolean status = paymentService.proceedPaymentOrder(order, paymentId);

        if (status) {
            wallet = walletService.addBalanceToUserWallet(user, order.getAmount());
        }

        return ResponseEntity.ok(wallet);
    }
}
