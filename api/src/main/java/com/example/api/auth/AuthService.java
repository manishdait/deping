package com.example.api.auth;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.api.security.JwtProvider;
import com.example.api.shared.AbstractUserEntity;
import com.example.api.shared.Role;
import com.example.api.user.User;
import com.example.api.user.UserRepository;
import com.example.api.validator.Validator;
import com.example.api.validator.ValidatorRepository;
import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Account;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final ValidatorRepository validatorRepository;

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  private final JwtProvider jwtProvider;

  private final AccountClient accountClient;

  public AuthResponse registerUser(RegistrationRequest request) {
    if (request.role().equals("User")) {
      return createUser(request); 
    }
    return createValidator(request);
  }
  
  public AuthResponse authenticateUser(AuthRequest request) {
    try {
      Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password())
      );

      AbstractUserEntity user = (AbstractUserEntity) authentication.getPrincipal();
      String accessToken = jwtProvider.generateToken(user.getEmail());
      
      return new AuthResponse(user.getEmail(), user.getRole().getRole(), accessToken, accessToken);
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("Invalid username or password");
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private AuthResponse createUser(RegistrationRequest request) {
    User user = User.builder()
      .email(request.email())
      .password(passwordEncoder.encode(request.password()))
      .role(Role.USER)
      .websites(List.of())
      .build();

    userRepository.save(user);

    String accessToken = jwtProvider.generateToken(user.getEmail());
    return new AuthResponse(user.getEmail(), "User", accessToken, accessToken);
  }

  private AuthResponse createValidator(RegistrationRequest request) {
    Account account;

    try {
      account = accountClient.createAccount();
    } catch (HieroException e) {
      e.printStackTrace();
      throw new RuntimeException("Error creating hiero account");
    }

    Validator validator = Validator.builder()
      .email(request.email())
      .password(passwordEncoder.encode(request.password()))
      .role(Role.VALIDATOR)
      .payout(0.0)
      .pubKey(account.publicKey().toString())
      .accountId(account.accountId().toString())
      .build();
    
    validatorRepository.save(validator);
    String accessToken = jwtProvider.generateToken(validator.getEmail());
    return new AuthResponse(validator.getEmail(), "Validator", accessToken, accessToken);
  }
}
