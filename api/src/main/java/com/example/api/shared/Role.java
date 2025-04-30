package com.example.api.shared;

public enum Role {
  USER("User"),
  VALIDATOR("Validator");

  private String role;
  
  Role(String role) {
    this.role = role;
  }

  public String getRole() {
    return this.role;
  }
}
