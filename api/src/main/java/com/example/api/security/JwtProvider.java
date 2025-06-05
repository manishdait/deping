package com.example.api.security;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {
  @Value("${spring.security.access-token-key}")
  private String accessTokenKey;

  @Value("${spring.security.refresh-token-key}")
  private String refreshTokenKey;

  private Integer expiration = 60*60;

  public String generateToken(String username, Integer expiration, String key) {
    return Jwts.builder()
      .claims(new HashMap<>())
      .subject(username)
      .issuedAt(Date.from(Instant.now()))
      .expiration(Date.from(Instant.now().plusSeconds(expiration)))
      .signWith(getSecretKey(key))      
      .compact();
  }

  public String generateAccessToken(String username) {
    return generateToken(username, this.expiration, accessTokenKey);
  }

  public String generateRefreshToken(String username) {
    return generateToken(username, 7*24*60*60, refreshTokenKey);
  }

  public Claims parseClaims(String token) {
    return Jwts.parser()
      .verifyWith(getSecretKey(accessTokenKey))
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

  private SecretKey getSecretKey(String key) {
    byte[] decodedKey = Decoders.BASE64.decode(key);
    return Keys.hmacShaKeyFor(decodedKey);
  }
}
