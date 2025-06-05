package com.example.api.ticks.dto;

import com.example.api.ticks.Status;

public record TicksRequest(Status status, Long websiteId, String validator) {
  
}
