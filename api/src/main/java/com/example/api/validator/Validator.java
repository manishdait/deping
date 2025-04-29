package com.example.api.validator;

import com.example.api.user.AbstractUserEntity;
import com.example.api.user.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "validator")
public class Validator extends AbstractUserEntity {
  @Column(name = "account_id", unique = true)
  private String accountId;

  @Column(name = "pub_key", unique = true)
  private String pubKey;

  @Column(name = "payout")
  private Double payout;

  public Validator() {
    super();
  }

  public Validator(Long id, String email, String password, Role role, String accountId, String pubKey, Double payout) {
    super(id, email, password, role);
    this.accountId = accountId;
    this.pubKey = pubKey;
    this.payout = payout;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getPubKey() {
    return pubKey;
  }

  public void setPubKey(String pubKey) {
    this.pubKey = pubKey;
  }

  public Double getPayout() {
    return payout;
  }

  public void setPayout(Double payout) {
    this.payout = payout;
  }

  public static ValidatorBuilder builder() {
    return new ValidatorBuilder();
  }

  public static class ValidatorBuilder {    
    private Long id;
    private String email;
    private String password;
    private Role role;
    private String accountId;
    private String pubKey;
    private Double payout;

    public ValidatorBuilder id(Long id) {
      this.id = id;
      return this;
    }

    public ValidatorBuilder email(String email) {
      this.email = email;
      return this;
    }

    public ValidatorBuilder password(String password) {
      this.password = password;
      return this;
    }

    public ValidatorBuilder role(Role role) {
      this.role = role;
      return this;
    }

    public ValidatorBuilder accountId(String accountId) {
      this.accountId = accountId;
      return this;
    }

    public ValidatorBuilder pubKey(String pubKey) {
      this.pubKey = pubKey;
      return this;
    }

    public ValidatorBuilder payout(Double payout) {
      this.payout = payout;
      return this;
    }

    public Validator build() {
      return new Validator(id, email, password, role, accountId, pubKey, payout);
    }
  }
}
