package com.example.api.auth.dto;

import com.example.api.user.Role;

public record AuthResponse(String email, Role role, String accessToken, String refreshToken) {
  
}
