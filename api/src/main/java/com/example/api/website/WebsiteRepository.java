package com.example.api.website;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.api.user.User;

public interface WebsiteRepository extends JpaRepository<Website, Long> {
  Page<Website> findByUser(User user, Pageable pageable);
}
