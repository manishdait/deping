package com.example.api.auth;

public record RegistrationRequest(String email, String password, String role) {
  
}
