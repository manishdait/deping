package com.example.api.validator;

import com.example.api.shared.AbstractUserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "validator")
public class Validator extends AbstractUserEntity {
  @Column(name = "account_id", unique = true)
  private String accountId;

  @Column(name = "pub_key", unique = true)
  private String pubKey;

  @Column(name = "payout")
  private Double payout;
}
