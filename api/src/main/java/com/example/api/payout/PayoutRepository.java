package com.example.api.payout;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.api.user.User;

public interface PayoutRepository extends JpaRepository<Payout, Long> {
  Optional<Payout> findByUser(User user);
}
