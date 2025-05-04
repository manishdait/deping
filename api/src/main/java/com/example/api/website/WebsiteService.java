package com.example.api.website;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.api.hub.HubDispatcher;
import com.example.api.shared.PagedEntity;
import com.example.api.user.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebsiteService {
  private final WebsiteRepository websiteRepository;
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
    response.setHasNext(websites.hasNext());
    response.setHasPrev(websites.hasPrevious());
    response.setContent(websites.getContent().stream().map(w -> new WebsiteResponse(w.getId(), w.getUrl())).toList());

    return response;
  }
}
