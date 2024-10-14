package com.trade.coinwave.service;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.trade.coinwave.domain.PaymentMethod;
import com.trade.coinwave.model.PaymentOrder;
import com.trade.coinwave.model.User;
import com.trade.coinwave.response.PaymentResponse;

import java.math.BigDecimal;

public interface PaymentService {

    PaymentOrder createOrder (User user, BigDecimal amount, PaymentMethod paymentMethod);
    PaymentOrder getPaymentOrderById(long orderId) throws Exception;
    boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;
    PaymentResponse createRazorpayPaymentLink(User user, BigDecimal amount);
    PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;

}
