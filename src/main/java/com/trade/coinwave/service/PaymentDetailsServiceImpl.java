package com.trade.coinwave.service;

import com.trade.coinwave.model.PaymentDetails;
import com.trade.coinwave.model.User;
import com.trade.coinwave.repository.PaymentDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService {

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public PaymentDetails getUserPaymentDetails(User user) {
        return paymentDetailsRepository.findByUserId(user.getId());
    }

    @Override
    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName,
                                            String bankName, String ifscCode, User user) {
        PaymentDetails newPaymentDetails = new PaymentDetails();
        newPaymentDetails.setAccountNumber(accountNumber);
        newPaymentDetails.setAccountHolderName(accountHolderName);
        newPaymentDetails.setBankName(bankName);
        newPaymentDetails.setIfscCode(ifscCode);
        newPaymentDetails.setUser(user);
        return paymentDetailsRepository.save(newPaymentDetails);

    }
}
