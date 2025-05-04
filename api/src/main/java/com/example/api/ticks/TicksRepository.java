package com.example.api.ticks;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicksRepository extends JpaRepository<Ticks, Long> {
  @Query("select t from Ticks t where t.website.id = :id order by t.timestamp desc limit 10")
  List<Ticks> findByWebsiteId(@Param("id") Long id);
}
