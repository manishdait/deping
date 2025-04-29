package com.example.api.validator;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidatorRepository extends JpaRepository<Validator, Long> {
  Optional<Validator> findByEmail(String email);
}
