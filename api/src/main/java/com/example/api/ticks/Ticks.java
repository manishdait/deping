package com.example.api.ticks;

import java.time.Instant;

import com.example.api.validator.Validator;
import com.example.api.website.Website;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "ticks")
public class Ticks {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticks_seq_generator")
  @SequenceGenerator(name = "ticks_seq_generator", sequenceName = "ticks_seq", allocationSize = 1, initialValue = 101)
  @Column(name = "id")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status")
  private Status status;

  @Column(name = "timestamp")
  private Instant timestamp;

  @ManyToOne()
  @JoinColumn(name = "website_id")
  private Website website;

  @ManyToOne()
  @JoinColumn(name = "validator_id")
  private Validator validator;
}
