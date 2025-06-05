package com.example.api.wallet;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.api.user.User;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
  Optional<Wallet> findByUser(User user);
}
