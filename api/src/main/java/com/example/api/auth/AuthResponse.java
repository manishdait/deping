package com.example.api.auth;

public record AuthResponse(String email, String role, String accessToken, String refreshToken) {
  
}
