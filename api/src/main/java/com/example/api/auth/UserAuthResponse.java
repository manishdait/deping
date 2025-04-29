package com.example.api.auth;

public record UserAuthResponse(String email, String accessToken, String refreshToken) {
  
}
