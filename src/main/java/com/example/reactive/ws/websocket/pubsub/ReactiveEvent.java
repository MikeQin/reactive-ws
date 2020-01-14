package com.example.reactive.ws.websocket.pubsub;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReactiveEvent {
  private String id;
  private String timestamp;
}