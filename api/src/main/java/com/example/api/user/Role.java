package com.example.api.user;

import lombok.Getter;

public enum Role {
  USER("User"),
  VALIDATOR("Validator");

  @Getter
  private String role;
  
  Role(String role) {
    this.role = role;
  }
}
