package com.example.api.security;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.example.api.validator.ValidatorService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthHandshakeInterceptor implements HandshakeInterceptor {
  private final JwtProvider jwtProvider;
  private final ValidatorService validatorService;

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
    String token = ((ServletServerHttpRequest) request).getServletRequest().getParameter("token");
    
    if (token == null) {
      return false;
    }

    String username = jwtProvider.getUsername(token);
    if (username != null) {
      UserDetails userDetails = validatorService.loadUserByUsername(username);

      if (jwtProvider.validToken(userDetails, token)) {
        attributes.put("username", userDetails.getUsername());
        return true;
      }
    }

    return false;
  }

  @Override
  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {}
}
