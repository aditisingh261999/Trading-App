package com.trade.coinwave.service;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.trade.coinwave.domain.PaymentMethod;
import com.trade.coinwave.domain.PaymentOrderStatus;
import com.trade.coinwave.model.PaymentOrder;
import com.trade.coinwave.model.User;
import com.trade.coinwave.repository.PaymentOrderRepository;
import com.trade.coinwave.response.PaymentResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${razorpay.api.key}")
    private String razorpaySecretKey;

    @Value("${razorpay.api.secret}")
    private String apiSecretKey;

    @Override
    public PaymentOrder createOrder(User user, BigDecimal amount, PaymentMethod paymentMethod) {
        PaymentOrder order = new PaymentOrder();
        order.setAmount(amount);
        order.setPaymentMethod(paymentMethod);
        order.setUser(user);
        return paymentOrderRepository.save(order);

    }

    @Override
    public PaymentOrder getPaymentOrderById(long orderId) throws Exception {
        Optional<PaymentOrder> order = paymentOrderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        }
        throw new Exception("Order not found");
    }

    @Override
    public boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
        if (paymentOrder.getPaymentOrderStatus().equals(PaymentOrderStatus.PENDING)) {
            if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)) {
                RazorpayClient razorpayClient = new RazorpayClient(razorpaySecretKey, apiSecretKey);
                Payment payment = razorpayClient.payments.fetch(paymentId);

                Integer amount = payment.get("amount");
                String status = payment.get("status");

                if (status.equals("captured")) {
                    paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.SUCCESS);
                    return true;
                } else {
                    paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.FAILED);
                    paymentOrderRepository.save(paymentOrder);
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public PaymentResponse createRazorpayPaymentLink(User user, BigDecimal amount) {
        Long Amount = amount.longValue() * 100;
        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpaySecretKey, apiSecretKey);

            // create JSON Object with payment link request parameters
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", "INR");

            // create JSON Object with customer details
            JSONObject customerRequest = new JSONObject();
            customerRequest.put("name", user.getFullName());
            customerRequest.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customerRequest);

            // create JSON Object with the notification settings
            JSONObject notificationRequest = new JSONObject();
            notificationRequest.put("email", true);
            paymentLinkRequest.put("notify", notificationRequest);

            // set the reminder settings
            paymentLinkRequest.put("reminder_enable", true);

            // set the callback url and method
            paymentLinkRequest.put("callback_url", "http://localhost:5173/wallet");
            paymentLinkRequest.put("callback_method", "get");

            // create payment link using the paymentLink.create() method
            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = paymentLink.get("id");
            String paymentLinkUrl = paymentLink.get("short_url");

            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setPaymentURL(paymentLinkUrl);

            return paymentResponse;

        } catch (RazorpayException e) {
            System.out.println("Error creating payment link: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams sessionCreateParams = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/wallet?order_id=" + orderId)
                .setCancelUrl("http://localhost:5173/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount * 100)
                                .setProductData(SessionCreateParams
                                        .LineItem
                                        .PriceData
                                        .ProductData
                                        .builder()
                                        .setName("Top up wallet")
                                        .build()
                                ).build()
                        ).build()
                ).build();

        Session session = Session.create(sessionCreateParams);
        System.out.println("Session created: " + session);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentURL(session.getUrl());
        return paymentResponse;
    }
}
