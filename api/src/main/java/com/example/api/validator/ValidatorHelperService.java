package com.example.api.validator;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.HieroException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidatorHelperService {
  private final AccountClient accountClient;

  public ValidatorDto getValidator(Authentication authentication) {
    Validator validator = (Validator) authentication.getPrincipal();

    Hbar balance;
    try {
      balance = accountClient.getAccountBalance(validator.getAccountId());
    } catch (HieroException e) {
      throw new RuntimeException("Error getting account balance");
    }
    
    return new ValidatorDto(validator.getEmail(), validator.getAccountId(), validator.getPubKey(), balance.getValue().doubleValue(), validator.getPayout());
  }
}
