package com.example.api.ticks;

import org.springframework.stereotype.Service;

import com.example.api.validator.Validator;
import com.example.api.validator.ValidatorRepository;
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
  public TicksResponse createTick(TickRequest request) {
    Validator validator = validatorRepository.findByEmail(request.validator()).orElseThrow();
    // Website website = websiteRepository.findById(request.websiteId()).orElseThrow();

    Ticks ticks = Ticks.builder()
      .status(request.status())
      .validator(validator)
      .build();

    double increment = 0.001;
    int precisionFactor = 1000;

    validator.setPayout((double) (Math.round(validator.getPayout() * precisionFactor) + Math.round(increment * precisionFactor)) / precisionFactor);

    validatorRepository.save(validator);
    ticksRepository.save(ticks);
    
    return new TicksResponse(validator.getPayout());
  }
}
