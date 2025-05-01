package com.example.api.website;

import com.example.api.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "website")
public class Website {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "website_seq_generator")
  @SequenceGenerator(name = "website_seq_generator", sequenceName = "website_seq", allocationSize = 1, initialValue = 101)
  @Column(name = "id")
  private Long id;

  @Column(name = "url")
  private String url;

  @ManyToOne()
  @JoinColumn(name = "user_id")
  private User user;
  
  public Website() {}
  
  public Website(Long id, String url, User user) {
    this.id = id;
    this.url = url;
    this.user = user;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public static  WebsiteBuilder builder() {
    return new WebsiteBuilder();
  }

  public static class WebsiteBuilder {
    private Long id;
    private String url;
    private User user;
    
    public WebsiteBuilder id(Long id) {
      this.id = id;
      return this;
    }
    
    public WebsiteBuilder url(String url) {
      this.url = url;
      return this;
    }
    
    public WebsiteBuilder user(User user) {
      this.user = user;
      return this;
    }

    public Website build() {
      return new Website(id, url, user);
    }
  }
}
