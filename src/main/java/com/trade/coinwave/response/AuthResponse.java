package com.trade.coinwave.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private boolean status;
    private String message;
    private boolean isTwoFactorEnabled;
    private String session;
}
