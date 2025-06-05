package com.example.api.wallet;

import com.example.api.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "wallet")
public class Wallet {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallet_seq_generator")
  @SequenceGenerator(name = "wallet_seq_generator", sequenceName = "wallet_seq", initialValue = 101, allocationSize = 1)
  @Column(name = "id")
  private Long id;

  @Column(name = "account_id", unique = true)
  private String accountId;

  @JsonIgnore
  @Column(name = "pb_key")
  private String pbKey;

  @JsonIgnore
  @Column(name = "pr_key")
  private String prKey;

  @JsonIgnore
  @OneToOne()
  @JoinColumn(name = "user_id")
  private User user;
}
