package com.example.api.website;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.shared.PagedEntity;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/websites")
@RequiredArgsConstructor
public class WebsiteController {
  private final WebsiteService websiteService;

  @PostMapping()
  public ResponseEntity<WebsiteResponse> createWebsite(@RequestBody WebsiteRequest request, Authentication authentication) {
    return ResponseEntity.status(HttpStatus.CREATED).body(websiteService.createWebsite(request, authentication));
  }

  @GetMapping()
  public ResponseEntity<PagedEntity<WebsiteResponse>> getWebsites(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Authentication authentication) {
    return ResponseEntity.status(HttpStatus.OK).body(websiteService.getWebsites(page, size, authentication));
  } 
}
