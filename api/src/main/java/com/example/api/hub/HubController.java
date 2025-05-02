package com.example.api.hub;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.api.ticks.TickRequest;
import com.example.api.ticks.TicksService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HubController {
  private final TicksService ticksService;

  @MessageMapping("/generate-ticks")
  @SendTo("/topic/public")
  public void generateTicks(TickRequest request) {
    ticksService.createTick(request);
  }
}
