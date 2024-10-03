package com.trade.coinwave.model;

import com.trade.coinwave.domain.WalletTransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Wallet wallet;

    private WalletTransactionType walletTransactionType;

    private LocalDate date;

    private String transferId;

    private String purpose;

    private BigDecimal amount;
}
