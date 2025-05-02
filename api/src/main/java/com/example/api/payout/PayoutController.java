package com.example.api.payout;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/payouts")
@RequiredArgsConstructor
public class PayoutController {
  private final PayoutService payoutService;

  @GetMapping()
  public ResponseEntity<PayoutDto> getPayouts(Authentication authentication) {
    return ResponseEntity.status(HttpStatus.OK).body(payoutService.getPayouts(authentication));
  }

  @GetMapping("/claim")
  public ResponseEntity<Map<String, Double>> claimPayouts(Authentication authentication) {
    return ResponseEntity.status(HttpStatus.OK).body(payoutService.claimPayouts(authentication));
  }
}
