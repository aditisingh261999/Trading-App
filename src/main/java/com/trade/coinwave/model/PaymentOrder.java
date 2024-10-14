package com.trade.coinwave.model;


import com.trade.coinwave.domain.PaymentMethod;
import com.trade.coinwave.domain.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal amount;

    private PaymentOrderStatus paymentOrderStatus;

    private PaymentMethod paymentMethod;

    @ManyToOne
    private User user;


}
