package com.example.reactive.ws.websocket.stream;

import lombok.Data;

@Data
public class EmployeeCreationEvent {
  private String employeeId;
  private String creationTime;

  public EmployeeCreationEvent(String employeeId, String creationTime) {
    this.employeeId = employeeId;
    this.creationTime = creationTime;
  }
}
