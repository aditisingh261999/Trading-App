package com.trade.coinwave.model;

import com.trade.coinwave.domain.WithdrawalStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private WithdrawalStatus withdrawalStatus;

    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    private LocalDateTime date = LocalDateTime.now();
}
