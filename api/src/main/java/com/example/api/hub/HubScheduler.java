package com.example.api.hub;

import java.time.Instant;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class HubScheduler {
  private final SimpMessagingTemplate messagingTemplate;

  public HubScheduler(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  @Scheduled(fixedRate = 10000)
  public void dispatchUrl() {
    messagingTemplate.convertAndSend("/topic/public", new Url("test" + Instant.now()));
  }
}
