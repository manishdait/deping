package com.example.api.ticks.dto;

import java.time.Instant;

import com.example.api.ticks.Status;

public record TicksResponse(Status status, double avgUp, double avgDown, int up, int down, Instant fromTimestamp, Instant toTimestamp, Long websiteId) { 
}
