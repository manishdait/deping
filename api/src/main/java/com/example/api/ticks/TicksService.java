package com.example.api.ticks;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.api.payout.Payout;
import com.example.api.payout.PayoutRepository;
import com.example.api.ticks.dto.TicksRequest;
import com.example.api.ticks.dto.TicksResponse;
import com.example.api.user.User;
import com.example.api.user.UserRepository;
import com.example.api.website.Website;
import com.example.api.website.WebsiteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicksService {
  private final TicksRepository ticksRepository;
  private final UserRepository userRepository;
  private final PayoutRepository payoutRepository;
  private final WebsiteRepository websiteRepository;

  @Transactional
  public void createTick(TicksRequest request) {
    User user = userRepository.findByEmail(request.validator()).orElseThrow();
    Payout payout = payoutRepository.findByUser(user).orElseThrow();
    Website website = websiteRepository.findById(request.websiteId()).orElseThrow();

    Ticks ticks = Ticks.builder()
      .status(request.status())
      .website(website)
      .validator(user)
      .timestamp(Instant.now())
      .build();

    long amount = payout.getAmount();

    payout.setAmount(amount + 1);

    payoutRepository.save(payout);
    ticksRepository.save(ticks);
  }

  public List<TicksResponse> getTicks(Long websiteId) {
    List<TicksResponse> response = new ArrayList<>();
    Instant end = Instant.now();

    int ptr = 0;
    for (int i = 0; i < 10; i++) {
      Instant start = end.minusSeconds(900);
      List<Ticks> ticks = ticksRepository.getTicksForWebsiteId(websiteId, start, end);
      
      if (ticks.isEmpty()) {
        response.add(null);
        continue;
      }

      int up = 0;
      int down = 0;
      for (Ticks t : ticks) {
        if (t.getStatus() == Status.UP) {
          up++;
        } else {
          down++;
        }
      }
      double avgUp = (double)up/(double)ticks.size();
      double avgDown = (double)down/(double)ticks.size();
      Status status = avgUp > avgDown? Status.UP : Status.DOWN;
      response.add(ptr++, new TicksResponse(status, avgUp, avgDown, up, down, websiteId));
      
      end = start;
    }

    Collections.reverse(response);

    return response;
  }
}
