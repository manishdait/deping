package com.example.api.validator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/validators")
public class ValidatorController {
  private final ValidatorHelperService validatorHelperService;

  public ValidatorController(ValidatorHelperService validatorHelperService) {
    this.validatorHelperService = validatorHelperService;
  }

  @GetMapping()
  public ResponseEntity<ValidatorDto> getValidator(Authentication authentication) {
    return ResponseEntity.status(HttpStatus.OK).body(validatorHelperService.getValidator(authentication));
  }
}
