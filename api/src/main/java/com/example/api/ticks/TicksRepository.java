package com.example.api.ticks;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicksRepository extends JpaRepository<Ticks, Long> {
}
