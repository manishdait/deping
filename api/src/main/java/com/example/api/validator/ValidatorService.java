package com.example.api.validator;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ValidatorService implements UserDetailsService {
  private final ValidatorRepository validatorRepository;

  public ValidatorService(ValidatorRepository validatorRepository) {
    this.validatorRepository = validatorRepository;
  } 

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return validatorRepository.findByEmail(username).orElseThrow(
      () -> new UsernameNotFoundException(String.format("User with email `%s` does not exists", username))
    );
  }
}
