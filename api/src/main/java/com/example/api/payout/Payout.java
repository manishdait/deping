package com.example.api.payout;

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
@Table(name = "payout")
public class Payout {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payout_seq_generator")
  @SequenceGenerator(name = "payout_seq_generator", sequenceName = "payout_seq", initialValue = 101, allocationSize = 1)
  @Column(name = "id")
  private Long id;

  @Column(name = "amount")
  private long amount;

  @JsonIgnore
  @OneToOne()
  @JoinColumn(name = "user_id")
  private User user;
}
