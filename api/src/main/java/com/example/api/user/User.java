package com.example.api.user;

import java.util.List;

import com.example.api.shared.AbstractUserEntity;
import com.example.api.website.Website;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
@Table(name = "_user")
public class User extends AbstractUserEntity {
  @OneToMany(mappedBy = "user")
  private List<Website> websites;
}

