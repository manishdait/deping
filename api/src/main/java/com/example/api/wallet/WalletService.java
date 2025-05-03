package com.example.api.wallet;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.api.validator.Validator;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletService {
  private final AccountClient accountClient;
  private final AccountRepository accountRepository;

  private final HieroContext hieroContext;

  public WalletDto getWallet(Authentication authentication) {
    Validator validator = (Validator) authentication.getPrincipal();
    WalletData walletData = fetchWalletData(validator.getAccountId());
    return new WalletDto(validator.getEmail(), validator.getAccountId(), walletData.pubKey(), walletData.balance()); 
  }

  public WalletDto transferHbar(TransferHbarRequest request, Authentication authentication) {
    Validator validator = (Validator) authentication.getPrincipal();
    
    TransferTransaction transactionTx = new TransferTransaction()
    .addHbarTransfer(AccountId.fromString(validator.getAccountId()), Hbar.from(BigDecimal.valueOf(request.amount()).multiply(BigDecimal.valueOf(-1))))
    .addHbarTransfer(EvmAddress.fromString(request.pubKey()), Hbar.from(BigDecimal.valueOf(request.amount())))
    .freezeWith(hieroContext.getClient());
    
    try {
      TransactionResponse txResponse = transactionTx.sign(PrivateKey.fromString(validator.getPrvKey())).execute(hieroContext.getClient());
      txResponse.getReceipt(hieroContext.getClient());
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getCause());
    }
    
    WalletData walletData = fetchWalletData(validator.getAccountId());
    return new WalletDto(validator.getEmail(), validator.getAccountId(), walletData.pubKey(), walletData.balance()); 
  }

  private WalletData fetchWalletData(String id) {
    try {
      Optional<AccountInfo> _accountInfo = accountRepository.findById(id);
      if (_accountInfo.isPresent()) {
        AccountInfo accountInfo = _accountInfo.get();
        
        return new WalletData(
          Hbar.fromTinybars(accountInfo.balance()).getValue().doubleValue(), 
          accountInfo.evmAddress()
        );
      }
      
      return new WalletData(
        accountClient.getAccountBalance(id).getValue().doubleValue(), 
        ""
      );
    } catch (HieroException e) {
      e.printStackTrace();
      throw new RuntimeException("Error getting wallet data");
    }
  }
}

record WalletData(double balance, String pubKey) {}
