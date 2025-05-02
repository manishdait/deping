package com.example.api.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.api.shared.AbstractUserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  private final JwtProvider jwtProvider;
  private final AbstractUserService abstractUserService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String token = request.getHeader(HttpHeaders.AUTHORIZATION);
    
    if (token == null || !token.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    token = token.substring(7);
    String username = jwtProvider.getUsername(token);
    
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails user = abstractUserService.loadUserByUsername(username);
      if (jwtProvider.validToken(user, token)) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }
    
    filterChain.doFilter(request, response);
  }
}
