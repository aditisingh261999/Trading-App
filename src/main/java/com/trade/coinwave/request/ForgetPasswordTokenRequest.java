package com.trade.coinwave.request;

import com.trade.coinwave.domain.VerificationType;
import lombok.Data;

@Data
public class ForgetPasswordTokenRequest {
    private String sendTo;
    private VerificationType verificationType;
}
