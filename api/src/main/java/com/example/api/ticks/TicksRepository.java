package com.example.api.ticks;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicksRepository extends JpaRepository<Ticks, Long> {
  @Query("select t from Ticks t where t.website.id = :id and t.timestamp between :from and :to")
  List<Ticks> getTicksForWebsiteId(@Param("id") Long id, @Param("from") Instant from, @Param("to") Instant to);
}
