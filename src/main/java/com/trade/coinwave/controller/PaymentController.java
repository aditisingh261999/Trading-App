package com.trade.coinwave.controller;

import com.trade.coinwave.domain.PaymentMethod;
import com.trade.coinwave.model.PaymentOrder;
import com.trade.coinwave.model.User;
import com.trade.coinwave.response.PaymentResponse;
import com.trade.coinwave.service.PaymentService;
import com.trade.coinwave.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/api/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long amount,
            @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserProfileByJwtToken(token);

        PaymentResponse paymentResponse;

        PaymentOrder paymentOrder = paymentService.createOrder(
                user,
                BigDecimal.valueOf(amount),
                paymentMethod
        );

        if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
            paymentResponse = paymentService.createRazorpayPaymentLink(user, BigDecimal.valueOf(amount));
        } else {
            paymentResponse = paymentService.createStripePaymentLink(user, amount, paymentOrder.getId());
        }

        return ResponseEntity.ok(paymentResponse);

    }

}
