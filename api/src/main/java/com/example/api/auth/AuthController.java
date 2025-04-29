package com.example.api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/user/sign-up")
  public ResponseEntity<UserAuthResponse> registerUser(@RequestBody UserRegistrationRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(request));
  }

  @PostMapping("/validator/sign-up")
  public ResponseEntity<ValidatorAuthResponse> registerValidator(@RequestBody ValidatorRegistrationRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerValidator(request));
  }
}
