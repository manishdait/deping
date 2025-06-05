package com.example.api.auth.dto;

import com.example.api.user.Role;

public record RegistrationRequest(String email, String password, Role role) {
  
}
