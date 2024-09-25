package com.trade.coinwave.model;

import com.trade.coinwave.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String Id;

    private String otp;

    @OneToOne
    private User user;

    private String email;

    private String phone;

    private VerificationType verificationType;
}
