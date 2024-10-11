package com.trade.coinwave.controller;

import com.trade.coinwave.model.PaymentDetails;
import com.trade.coinwave.model.User;
import com.trade.coinwave.service.PaymentDetailsService;
import com.trade.coinwave.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentDetailsController {

    private final PaymentDetailsService paymentDetailsService;
    private final UserService userService;

    public PaymentDetailsController(PaymentDetailsService paymentDetailsService, UserService userService) {
        this.paymentDetailsService = paymentDetailsService;
        this.userService = userService;
    }

    @PostMapping("/payment-details")
    public ResponseEntity<PaymentDetails> addPaymentDetails(
            @RequestBody PaymentDetails paymentDetailsRequest,
            @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserProfileByJwtToken(token);
        PaymentDetails paymentDetails = paymentDetailsService.addPaymentDetails(
                paymentDetailsRequest.getAccountNumber(),
                paymentDetailsRequest.getAccountHolderName(),
                paymentDetailsRequest.getBankName(),
                paymentDetailsRequest.getIfscCode(),
                user
        );
        return ResponseEntity.ok().body(paymentDetails);
    }

    @GetMapping("/user")
    public ResponseEntity<PaymentDetails> getUserPaymentDetails(
            @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserProfileByJwtToken(token);
        PaymentDetails paymentDetails = paymentDetailsService.getUserPaymentDetails(user);
        return ResponseEntity.ok().body(paymentDetails);
    }
}
