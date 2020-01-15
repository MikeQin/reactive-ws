package com.example.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class HomeController {

  @GetMapping(path="/", produces = MediaType.TEXT_HTML_VALUE)
  public Mono<String> home(@RequestParam(value = "name", required = false, defaultValue = "")
                                 String name) {

    log.info("[*] home, name: {}", name);

    String res = null;
    if(name.isEmpty()) {
      res = "<h1>Hello, Guest, Welcome to Spring Reactive WebFlux Controller</h1>";
    }
    else {
      res = String.format("<h1>Hello, %s, Welcome to Spring Reactive WebFlux Controller</h1>", name);
    }

    return Mono.just(res);
  }
}
