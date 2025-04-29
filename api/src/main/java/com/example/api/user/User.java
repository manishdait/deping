package com.example.api.user;

import java.util.List;

import com.example.api.website.Website;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "_user")
public class User extends AbstractUserEntity {
  @Column(name = "uname")
  private String uname;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Website> websites;
  
  public User() {
    super();
  }

  public User(Long id, String uname, String email, String password, Role role, List<Website> websites) {
    super(id, email, password, role);
    this.uname = uname;
    this.websites = websites;
  }

  public String getUname() {
    return uname;
  }

  public void setUname(String uname) {
    this.uname = uname;
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
    private String uname;
    private String email;
    private String password;
    private Role role;
    private List<Website> websites;

    public UserBuilder id(Long id) {
      this.id = id;
      return this;
    }
    
    public UserBuilder uname(String uname) {
      this.uname = uname;
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
      return new User(id, uname, email, password, role, websites);
    }
  }
}

