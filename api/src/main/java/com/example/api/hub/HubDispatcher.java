package com.example.api.hub;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.api.website.Website;
import com.example.api.website.WebsiteRepository;
import com.example.api.website.dto.WebsiteResponse;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class HubDispatcher {
  private final WebsiteRepository websiteRepository;
  private final SimpMessagingTemplate messagingTemplate;

  private static List<Website> page;
  private static int ptr = 0;
  
  @PostConstruct
  public void init() {
    page = websiteRepository.findAll();
    ptr = 0;
  }

  @Scheduled(fixedRate = 10000)
  public void dispatchUrl() {
    while (ptr < page.size()) {
      Website url = page.get(ptr);
      messagingTemplate.convertAndSend("/topic/public", new WebsiteResponse(url.getId(), url.getUrl()));
      ptr++;
    }
    ptr = 0;
  }

  public void addUrl(Website website) {
    page.add(website);
  }
}
