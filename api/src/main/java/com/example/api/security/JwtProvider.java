package com.example.api.security;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {
  private String secretKey = "8cfe790badd33fc5655c7a1f713b7fde3142fe6c204e21a19ba64defb43edf46";
  private Integer expiration = 60*60;

  public String generateToken(String username, Integer expiration) {
    return Jwts.builder()
      .claims(new HashMap<>())
      .subject(username)
      .issuedAt(Date.from(Instant.now()))
      .expiration(Date.from(Instant.now().plusSeconds(expiration)))
      .signWith(getSecretKey())      
      .compact();
  }

  public String generateToken(String username) {
    return generateToken(username, this.expiration);
  }

  public Claims parseClaims(String token) {
    return Jwts.parser()
      .verifyWith(getSecretKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  public String getUsername(String token) {
    return parseClaims(token).getSubject();
  }

  public boolean tokenExpired(String token) {
    return parseClaims(token).getExpiration().before(new Date());
  }

  public boolean validToken(UserDetails user, String token) {
    return user.getUsername().equals(getUsername(token)) && !tokenExpired(token);
  }

  private SecretKey getSecretKey() {
    byte[] decodedKey = Decoders.BASE64.decode(this.secretKey);
    return Keys.hmacShaKeyFor(decodedKey);
  }
}
