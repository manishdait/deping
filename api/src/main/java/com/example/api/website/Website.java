package com.example.api.website;

import com.example.api.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "website")
public class Website {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "website_seq_generator")
  @SequenceGenerator(name = "website_seq_generator", sequenceName = "website_seq", allocationSize = 1, initialValue = 101)
  @Column(name = "id")
  private Long id;

  @Column(name = "url")
  private String url;

  @ManyToOne()
  @JoinColumn(name = "user_id")
  private User user;
}
