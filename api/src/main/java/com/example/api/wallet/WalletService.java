package com.example.api.wallet;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.api.user.User;
import com.example.api.wallet.dto.TransferHbarRequest;
import com.example.api.wallet.dto.WalletDto;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.EvmAddress;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.hedera.hashgraph.sdk.TransferTransaction;
import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.HieroContext;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.AccountInfo;
import com.openelements.hiero.base.mirrornode.AccountRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletService {
  private final WalletRepository walletRepository;

  private final AccountClient accountClient;
  private final AccountRepository accountRepository;
  private final HieroContext hieroContext;

  public WalletDto getWallet(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    Wallet wallet = walletRepository.findByUser(user).orElseThrow(
      () -> new EntityNotFoundException("No wallet available for user")
    );

    WalletData walletData = fetchWalletData(wallet.getAccountId());
    return new WalletDto(user.getEmail(), wallet.getAccountId(), walletData.evmAddr(), walletData.balance()); 
  }

  public WalletDto transferHbar(TransferHbarRequest request, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    Wallet wallet = walletRepository.findByUser(user).orElseThrow(
      () -> new EntityNotFoundException("No wallet available for user")
    );

    transfer(wallet, request.pbKey(), request.amount());
    
    WalletData walletData = fetchWalletData(wallet.getAccountId());
    return new WalletDto(user.getEmail(), wallet.getAccountId(), walletData.evmAddr(), walletData.balance()); 
  }

  private WalletData fetchWalletData(String accountId) {
    try {
      Optional<AccountInfo> _accountInfo = accountRepository.findById(accountId);
      if (_accountInfo.isPresent()) {
        AccountInfo accountInfo = _accountInfo.get();
        
        return new WalletData(
          Hbar.fromTinybars(accountInfo.balance()).getValue().doubleValue(), 
          accountInfo.evmAddress()
        );
      }
      
      return new WalletData(
        accountClient.getAccountBalance(accountId).getValue().doubleValue(), 
        ""
      );
    } catch (HieroException e) {
      e.printStackTrace();
      throw new RuntimeException("Error getting wallet data");
    }
  }

  private void transfer(Wallet wallet, String recipent, double amount) {
    AccountId accountId = AccountId.fromString(wallet.getAccountId());
    PrivateKey senderPrKey = PrivateKey.fromString(wallet.getPrKey());

    BigDecimal hbar = BigDecimal.valueOf(amount);
    EvmAddress recipentPbKey = EvmAddress.fromString(recipent);

    TransferTransaction transactionTx = new TransferTransaction()
      .addHbarTransfer(accountId, Hbar.from(hbar.multiply(BigDecimal.valueOf(-1))))
      .addHbarTransfer(recipentPbKey, Hbar.from(hbar))
      .freezeWith(hieroContext.getClient());
    
    try {
      TransactionResponse txResponse = transactionTx.sign(senderPrKey)
        .execute(hieroContext.getClient());

      txResponse.getReceipt(hieroContext.getClient());
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getCause());
    }
  }
}

record WalletData(double balance, String evmAddr) {}
