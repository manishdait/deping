package com.example.api.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.api.auth.dto.AuthRequest;
import com.example.api.auth.dto.AuthResponse;
import com.example.api.auth.dto.RegistrationRequest;
import com.example.api.payout.Payout;
import com.example.api.payout.PayoutRepository;
import com.example.api.security.JwtProvider;
import com.example.api.user.Role;
import com.example.api.user.User;
import com.example.api.user.UserRepository;
import com.example.api.wallet.Wallet;
import com.example.api.wallet.WalletRepository;
import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Account;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final PayoutRepository payoutRepository;
  private final WalletRepository walletRepository;

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  private final JwtProvider jwtProvider;

  private final AccountClient accountClient;

  @Transactional
  public AuthResponse registerUser(RegistrationRequest request) {
    userRepository.findByEmail(request.email()).ifPresent(u -> {
      throw new IllegalStateException(String.format("User already exists with email %s", u.getEmail()));
    });

    User user = User.builder()
      .email(request.email())
      .password(passwordEncoder.encode(request.password()))
      .role(request.role())
      .build();

    userRepository.save(user);

    if (user.getRole() == Role.VALIDATOR) {
      createWallet(user);
    }

    String accessToken = jwtProvider.generateAccessToken(user.getUsername());
    String refreshToken = jwtProvider.generateRefreshToken(user.getUsername());
    return new AuthResponse(user.getEmail(), user.getRole(), accessToken, refreshToken);
  }

  public AuthResponse authenticateUser(AuthRequest request) {
    try {
      Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password())
      );

      User user = (User) authentication.getPrincipal();

      String accessToken = jwtProvider.generateAccessToken(user.getUsername());
      String refreshToken = jwtProvider.generateRefreshToken(user.getUsername());
      
      return new AuthResponse(user.getEmail(), user.getRole(), accessToken, refreshToken);
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("Invalid username or password");
    }
  }

  private void createWallet(User user) {
    try {
      Account account = accountClient.createAccount();
      
      Wallet wallet = Wallet.builder()
        .accountId(account.accountId().toString())
        .pbKey(account.publicKey().toString())
        .prKey(account.privateKey().toString())
        .user(user)
        .build();
      
      walletRepository.save(wallet);
      
      Payout payout = Payout.builder().amount(0L).user(user).build();
      payoutRepository.save(payout);
    } catch (HieroException e) {
      e.printStackTrace();
      throw new RuntimeException(String.format("Error creating wallet for user %s", user.getEmail()));
    }
  }
}
