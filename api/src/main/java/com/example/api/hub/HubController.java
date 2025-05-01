package com.example.api.hub;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class HubController {
  @MessageMapping("/tbd")
  @SendTo("/topic/public")
  public Url tbd(Url message) {
    return new Url("from server : " + message.getUrl());
  }
}
