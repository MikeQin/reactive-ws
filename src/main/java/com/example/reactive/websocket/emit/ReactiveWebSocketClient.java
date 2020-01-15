package com.example.reactive.websocket.emit;

import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;

public class ReactiveWebSocketClient {
  public static void main(String[] args) throws InterruptedException {

    WebSocketClient client = new ReactorNettyWebSocketClient();
    client.execute(
        URI.create("ws://localhost:8080/event-emitter"),
        session -> session.send(
            Mono.just(session.textMessage("reactive-emit-msg hello")))
            .thenMany(session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .log())
            .then())
        .block(); // to subscribe and return the value
  }
}
