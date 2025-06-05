package com.example.api.payout;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.api.config.AppConfig;
import com.example.api.payout.dto.PayoutDto;
import com.example.api.user.User;
import com.example.api.wallet.Wallet;
import com.example.api.wallet.WalletRepository;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.hedera.hashgraph.sdk.TransferTransaction;
import com.openelements.hiero.base.HieroContext;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayoutService {
  private final PayoutRepository payoutRepository;
  private final WalletRepository walletRepository;
  private final HieroContext hieroContext;

  @Transactional
  public Map<String, Double> claimPayouts(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    Wallet wallet = walletRepository.findByUser(user).orElseThrow(
      () -> new EntityNotFoundException("No wallet available for user")
    );

    Payout payout = payoutRepository.findByUser(user).orElseThrow(
      () -> new EntityNotFoundException(String.format("No payouts available for %s", user.getUsername()))
    );

    double amount = (double) payout.getAmount() / (double) AppConfig.PAYOUT_FACTOR;

    if (amount <= 0) {
      throw new IllegalArgumentException("Payouts not available");
    }

    claim(wallet.getAccountId(), amount);

    payout.setAmount(0L);
    payoutRepository.save(payout);

    return Map.of("payout_recived", amount);
  }

  public PayoutDto getPayouts(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    Payout payout = payoutRepository.findByUser(user).orElseThrow(
      () -> new EntityNotFoundException(String.format("No payouts available for %s", user.getUsername()))
    );

    double amount = (double) payout.getAmount() / (double) AppConfig.PAYOUT_FACTOR;
    return new PayoutDto(amount);
  }

  private void claim(String userAccountId, double amount) {
    AccountId accountId = AccountId.fromString(userAccountId);
    BigDecimal hbar = BigDecimal.valueOf(amount);

    TransferTransaction transferTx = new TransferTransaction()
      .addHbarTransfer(hieroContext.getOperatorAccount().accountId(), new Hbar(hbar.multiply(BigDecimal.valueOf(-1))))
      .addHbarTransfer(accountId, new Hbar(hbar))
      .freezeWith(hieroContext.getClient());

    try {
      TransactionResponse txResponse = transferTx.sign(hieroContext.getOperatorAccount().privateKey())
        .execute(hieroContext.getClient());

      txResponse.getReceipt(hieroContext.getClient());
    } catch (Exception e) {
      throw new RuntimeException("Error claiming payouts"); 
    }
  }
}
