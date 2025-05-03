package com.example.api.ticks;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.api.website.Website;


public interface TicksRepository extends JpaRepository<Ticks, Long> {
  List<Ticks> findByWebsite(Website website);
}
