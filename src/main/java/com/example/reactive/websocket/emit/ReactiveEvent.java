package com.example.reactive.websocket.emit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactiveEvent {
  private String id;
  private String timestamp;
}