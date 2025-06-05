package com.example.api.user;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "_user")
public class User implements UserDetails, Principal {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_generator")
  @SequenceGenerator(name = "user_seq_generator", sequenceName = "user_seq", allocationSize = 1, initialValue = 101)
  @Column(name = "id")
  private Long id;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "password")
  private String password;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "role")
  private Role role;

  @Override
  public String getName() {
    return this.email;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.getRole()));
  }

  @Override
  public String getUsername() {
    return this.email;
  }
}
