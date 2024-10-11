package com.trade.coinwave.service;

import com.trade.coinwave.model.PaymentDetails;
import com.trade.coinwave.model.User;
import org.springframework.security.core.parameters.P;

public interface PaymentDetailsService {

    PaymentDetails getUserPaymentDetails(User user);
    PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName,
                                     String bankName, String ifscCode, User user);
}
