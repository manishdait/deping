package com.example.api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.auth.dto.AuthRequest;
import com.example.api.auth.dto.AuthResponse;
import com.example.api.auth.dto.RegistrationRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/sign-up")
  public ResponseEntity<AuthResponse> registerUser(@RequestBody RegistrationRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(request));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(authService.authenticateUser(request));
  }
}
