package com.example.api.website;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.api.hub.HubDispatcher;
import com.example.api.shared.PagedEntity;
import com.example.api.ticks.TicksService;
import com.example.api.ticks.dto.TicksResponse;
import com.example.api.user.User;
import com.example.api.website.dto.WebsiteDto;
import com.example.api.website.dto.WebsiteRequest;
import com.example.api.website.dto.WebsiteResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebsiteService {
  private final WebsiteRepository websiteRepository;

  private final TicksService ticksService;
  private final HubDispatcher hubDispatcher;
 
  @Transactional
  public WebsiteResponse createWebsite(WebsiteRequest request, Authentication authentication) {
    User user = (User) authentication.getPrincipal();

    Website website = Website.builder()
      .url(request.url())
      .user(user)
      .build();
    
    websiteRepository.save(website);
    hubDispatcher.addUrl(website);
    return new WebsiteResponse(website.getId(), website.getUrl());
  }

  public PagedEntity<WebsiteResponse> getWebsites(int page, int size, Authentication authentication) {
    User user = (User) authentication.getPrincipal();

    Pageable pageable = PageRequest.of(page, size);
    Page<Website> websites = websiteRepository.findByUser(user, pageable);
    
    PagedEntity<WebsiteResponse> response = new PagedEntity<>();
    response.setTotalElements(websites.getTotalElements());
    response.setHasNext(websites.hasNext());
    response.setHasPrevious(websites.hasPrevious());
    
    response.setContent(
      websites.getContent().stream()
        .map(w -> new WebsiteResponse(w.getId(), w.getUrl()))
        .toList()
    );

    return response;
  }

  public WebsiteDto getWebsite(Long id) {
    Website website = websiteRepository.findById(id).orElseThrow();
    List<TicksResponse> ticks = ticksService.getTicks(id);

    return new WebsiteDto(id, website.getUrl(), ticks);
  }
}
