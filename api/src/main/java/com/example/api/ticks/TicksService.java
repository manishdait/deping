package com.example.api.ticks;

import java.time.Instant;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;

import com.example.api.validator.Validator;
import com.example.api.validator.ValidatorRepository;
import com.example.api.website.Website;
import com.example.api.website.WebsiteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicksService {
  private final TicksRepository ticksRepository;
  private final ValidatorRepository validatorRepository;
  private final WebsiteRepository websiteRepository;

  @Transactional
  public void createTick(TicksDto request) {
    Validator validator = validatorRepository.findByEmail(request.validator()).orElseThrow();
    Website website = websiteRepository.findById(request.websiteId()).orElseThrow();

    Ticks ticks = Ticks.builder()
      .status(request.status())
      .website(website)
      .validator(validator)
      .timestamp(Instant.now())
      .build();

    long payout = validator.getPayout();

    validator.setPayout(payout + 1);

    validatorRepository.save(validator);
    ticksRepository.save(ticks);
  }

  public List<TicksDto> getTicks(Long websiteId) {
    System.out.println("App: " + websiteId);
    Website website = websiteRepository.findById(websiteId).orElseThrow(
      () -> new RuntimeException("Not found")
    );
    return ticksRepository.findByWebsite(website).stream()
      .map(w -> new TicksDto(w.getStatus(), websiteId, w.getValidator().getEmail()))
      .toList();
  }
}
