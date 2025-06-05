package com.example.api.shared;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PagedEntity<T> {
  private long totalElements;
  private boolean hasNext;
  private boolean hasPrevious;
  private List<T> content;
}
