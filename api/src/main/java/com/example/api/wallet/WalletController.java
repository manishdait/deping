package com.example.api.wallet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
  private final WalletService walletService;

  @GetMapping()
  public ResponseEntity<WalletDto> getWallet(Authentication authentication) {
    return ResponseEntity.status(HttpStatus.OK).body(walletService.getWallet(authentication));
  }

  @PostMapping("/transfer")
  public ResponseEntity<WalletDto> postMethodName(@RequestBody TransferHbarRequest request, Authentication authentication) {
    return ResponseEntity.status(HttpStatus.OK).body(walletService.transferHbar(request, authentication));
  }
}
