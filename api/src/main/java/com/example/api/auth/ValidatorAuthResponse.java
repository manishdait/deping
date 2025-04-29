package com.example.api.auth;

public record ValidatorAuthResponse(String email, String accountId, String pubKey, Double balance, String accessToken, String refreshToken) {
  
}
