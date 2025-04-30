package com.example.api.shared;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.api.user.UserService;
import com.example.api.validator.ValidatorService;

@Service
public class AbstractUserService implements UserDetailsService {
  private final UserService userService;
  private final ValidatorService validatorService;

  private final List<UserDetailsService> services;

  public AbstractUserService(UserService userService, ValidatorService validatorService) {
    this.userService = userService;
    this.validatorService = validatorService;
    this.services = List.of(this.userService, this.validatorService);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    for (UserDetailsService service : services) {
      try {
        UserDetails user = service.loadUserByUsername(username);
        return user;
      } catch (UsernameNotFoundException e) {
        continue;
      }
    }
    throw new UsernameNotFoundException(String.format("User with email `%s` does not exists", username));
  }  
}
