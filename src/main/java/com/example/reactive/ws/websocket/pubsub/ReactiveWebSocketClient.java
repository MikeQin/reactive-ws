package com.example.reactive.ws.websocket.pubsub;

import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;

public class ReactiveWebSocketClient {
  public static void main(String[] args) throws InterruptedException {

    WebSocketClient client = new ReactorNettyWebSocketClient();
    client.execute(
        URI.create("ws://localhost:8080/event-emitter"),
        session -> session.send(
            Mono.just(session.textMessage("reactive-client-msg hello")))
            .thenMany(session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .log())
            .then())
        .block(Duration.ofSeconds(10L));
  }
}
