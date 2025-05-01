package com.example.api.user;

import java.util.List;

import com.example.api.shared.AbstractUserEntity;
import com.example.api.shared.Role;
import com.example.api.website.Website;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "_user")
public class User extends AbstractUserEntity {
  @OneToMany(mappedBy = "user")
  private List<Website> websites;
  
  public User() {
    super();
  }

  public User(Long id, String email, String password, Role role, List<Website> websites) {
    super(id, email, password, role);
    this.websites = websites;
  }

  public List<Website> getWebsites() {
    return websites;
  }

  public void setWebsites(List<Website> websites) {
    this.websites = websites;
  }

  public static UserBuilder builder() {
    return new UserBuilder();
  }
  
  public static class UserBuilder {
    private Long id;
    private String email;
    private String password;
    private Role role;
    private List<Website> websites;

    public UserBuilder id(Long id) {
      this.id = id;
      return this;
    }
    
    public UserBuilder email(String email) {
      this.email = email;
      return this;
    }
    
    public UserBuilder password(String password) {
      this.password = password;
      return this;
    }
    
    public UserBuilder role(Role role) {
      this.role = role;
      return this;
    }

    public UserBuilder websites(List<Website> websites) {
      this.websites = websites;
      return this;
    }

    public User build() {
      return new User(id, email, password, role, websites);
    }
  }
}

