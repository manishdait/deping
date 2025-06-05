package com.example.api.ticks.dto;

import com.example.api.ticks.Status;

public record TicksResponse(Status status, double avgUp, double avgDown, int up, int down, Long websiteId) {
  
}
