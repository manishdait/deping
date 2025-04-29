package com.example.api.user;

public enum Role {
  USER("ROLE_User"),
  VALIDATOR("ROLE_Validator");

  private String role;
  
  Role(String role) {
    this.role = role;
  }

  public String getRole() {
    return this.role;
  }
}
