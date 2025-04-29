package com.example.api.auth;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.api.security.JwtProvider;
import com.example.api.user.Role;
import com.example.api.user.User;
import com.example.api.user.UserRepository;
import com.example.api.validator.Validator;
import com.example.api.validator.ValidatorRepository;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Account;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final ValidatorRepository validatorRepository;

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  private final JwtProvider jwtProvider;

  private final AccountClient accountClient;

  public AuthService(UserRepository userRepository, ValidatorRepository validatorRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtProvider jwtProvider, AccountClient accountClient) {
    this.userRepository = userRepository;
    this.validatorRepository = validatorRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtProvider = jwtProvider;
    this.accountClient = accountClient;
  }

  public UserAuthResponse registerUser(UserRegistrationRequest request) {
    User user = User.builder()
      .uname(request.uname())
      .email(request.email())
      .password(passwordEncoder.encode(request.password()))
      .role(Role.USER)
      .websites(List.of())
      .build();

    userRepository.save(user);

    String accessToken = jwtProvider.generateToken(user.getEmail());
    return new UserAuthResponse(user.getEmail(), accessToken, accessToken);
  }
  
  public UserAuthResponse authenticateUser(AuthRequest request) {
    try {
      Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password())
      );

      User user = (User) authentication.getPrincipal();
      String accessToken = jwtProvider.generateToken(user.getEmail());
      
      return new UserAuthResponse(user.getEmail(), accessToken, accessToken);
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("Invalid username or password");
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public ValidatorAuthResponse registerValidator(ValidatorRegistrationRequest request) {
    Account account;

    try {
      account = accountClient.createAccount();
    } catch (HieroException e) {
      e.printStackTrace();
      throw new RuntimeException("Error creating account");
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
    Hbar balance;
    try {
      balance = accountClient.getAccountBalance(account.accountId());
    } catch (HieroException e) {
      e.printStackTrace();
      throw new RuntimeException("Error getting accountBalance");
    }
    return new ValidatorAuthResponse(validator.getEmail(), validator.getAccountId(), validator.getPubKey(), balance.getValue().doubleValue(), accessToken, accessToken);
  }

  public ValidatorAuthResponse authenticateValidator(AuthRequest request) {
    try {
      Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password())
      );
      
      Validator validator = (Validator) authentication.getPrincipal();

      String accessToken = jwtProvider.generateToken(validator.getEmail());

      Hbar balance;
      
      try {
        balance = accountClient.getAccountBalance(validator.getAccountId());
      } catch (HieroException e) {
        e.printStackTrace();
        throw new RuntimeException("Error getting accountBalance");
      }
      
      return new ValidatorAuthResponse(validator.getEmail(), validator.getAccountId(), validator.getPubKey(), balance.getValue().doubleValue(), accessToken, accessToken);
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("Invalid username or password");
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
