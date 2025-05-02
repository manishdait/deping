package com.example.api.payout;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.api.validator.Validator;
import com.example.api.validator.ValidatorRepository;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.hedera.hashgraph.sdk.TransferTransaction;
import com.openelements.hiero.base.HieroContext;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayoutService {
  private final ValidatorRepository validatorRepository;
  private final HieroContext hieroContext;

  @Transactional
  public Map<String, Double> claimPayouts(Authentication authentication) {
    Validator validator = (Validator) authentication.getPrincipal();

    double payout = validator.getPayout();
    if (payout <= 0) {
      throw new RuntimeException("Payouts not available");
    }

    TransferTransaction transferTx = new TransferTransaction()
      .addHbarTransfer(hieroContext.getOperatorAccount().accountId(), new Hbar(BigDecimal.valueOf(payout).multiply(BigDecimal.valueOf(-1))))
      .addHbarTransfer(AccountId.fromString(validator.getAccountId()), new Hbar(BigDecimal.valueOf(payout)))
      .freezeWith(hieroContext.getClient());

    try {
      TransactionResponse txResponse = transferTx.sign(hieroContext.getOperatorAccount().privateKey())
        .execute(hieroContext.getClient());

      txResponse.getReceipt(hieroContext.getClient());
    } catch (Exception e) {
      throw new RuntimeException("Error transfering payouts"); 
    }

    validator.setPayout(0.0);
    validatorRepository.save(validator);

    return Map.of("payout_recived", payout);
  }

  public PayoutDto getPayouts(Authentication authentication) {
    Validator validator = (Validator) authentication.getPrincipal();
    return new PayoutDto(validator.getPayout());
  }
}
