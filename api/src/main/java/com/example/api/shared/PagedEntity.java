package com.example.api.shared;

import java.util.List;

public class PagedEntity<T> {
  private boolean hasNext;
  private boolean hasPrev;
  private List<T> content;

  public PagedEntity() {}
  
  public PagedEntity(boolean hasNext, boolean hasPrev, List<T> content) {
    this.hasNext = hasNext;
    this.hasPrev = hasPrev;
    this.content = content;
  }

  public boolean isHasNext() {
    return hasNext;
  }

  public void setHasNext(boolean hasNext) {
    this.hasNext = hasNext;
  }

  public boolean isHasPrev() {
    return hasPrev;
  }

  public void setHasPrev(boolean hasPrev) {
    this.hasPrev = hasPrev;
  }

  public List<T> getContent() {
    return content;
  }

  public void setContent(List<T> content) {
    this.content = content;
  }
}
