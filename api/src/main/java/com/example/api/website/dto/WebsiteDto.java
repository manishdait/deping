package com.example.api.website.dto;

import java.util.List;

import com.example.api.ticks.dto.TicksResponse;

public record WebsiteDto(Long id, String url, List<TicksResponse> ticks) {
  
}
