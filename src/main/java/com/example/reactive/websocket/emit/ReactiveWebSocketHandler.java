package com.example.reactive.websocket.emit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

@Component("ReactiveWebSocketHandler")
@Slf4j
public class ReactiveWebSocketHandler implements WebSocketHandler {

  private static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("HH:mm:ss");

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private Flux<String> eventFlux = Flux.generate(sink -> {

    ReactiveEvent event = new ReactiveEvent(randomUUID().toString(), now().format(PATTERN));
    try {
      sink.next(objectMapper.writeValueAsString(event));
    } catch (JsonProcessingException e) {
      sink.error(e);
    }
  });

  private Flux<String> intervalFlux = Flux.interval(Duration.ofSeconds(1))
      .zipWith(eventFlux, (time, event) -> event);

  @Override
  public Mono<Void> handle(WebSocketSession webSocketSession) {

    return webSocketSession.send(intervalFlux
        .map(webSocketSession::textMessage))
        .and(webSocketSession.receive()
            .map(WebSocketMessage::getPayloadAsText).log());
  }
}