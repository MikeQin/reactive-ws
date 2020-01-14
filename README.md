# Reactive Web Service (WebFlux) 

Building A Reactive REST EmployeeManagement application
 – using Spring WebFlux.
 
* We'll use a simple domain model – Employee with an id and a name field
* We'll build REST APIs for publishing and retrieve Single as well as Collection Employee resources using RestController and WebClient
* And we will also be creating a secured reactive endpoint using WebFlux and Spring Security

### WebFlux Using Annotation Controller

GET:
```
http://localhost:8080
http://localhost:8080/?name=Nick
http://localhost:8080/employees
http://localhost:8080/employees/{id}
```

POST:
```
http://localhost:8080/employees/update
```

Java Code:
```java
@RestController
@RequestMapping(path = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController {

  private final EmployeeRepository employeeRepository;

  public EmployeeController(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @GetMapping("/{id}")
  private Mono<Employee> getEmployeeById(@PathVariable String id) {
    return employeeRepository.findEmployeeById(id);
  }

  @GetMapping
  private Flux<Employee> getAllEmployees() {
    return employeeRepository.findAllEmployees();
  }

  @PostMapping("/update")
  private Mono<Employee> updateEmployee(@RequestBody Employee employee) {
    return employeeRepository.updateEmployee(employee);
  }
}
```

```java
public class EmployeeWebClient {

  private WebClient client = WebClient.create("http://localhost:8080");

  public void consume() {

    Mono<Employee> employeeMono = client.get()
        .uri("/employees/{id}", "1")
        .retrieve()
        .bodyToMono(Employee.class);

    employeeMono.subscribe(System.out::println);

    Flux<Employee> employeeFlux = client.get()
        .uri("/employees")
        .retrieve()
        .bodyToFlux(Employee.class);

    employeeFlux.subscribe(System.out::println);
  }

  public static void main(String[] args) {
    EmployeeWebClient webClient = new EmployeeWebClient();
    webClient.consume();
  }
}
```

### WebFlux Using Reactive WebSocket

WebSocket Endpoint:
```
ws://localhost:8080/employee-feed
```

Java Code:
```java
@Configuration
@EnableWebFlux
public class EmployeeConfig {

  @Bean
  public HandlerMapping handlerMapping() {
    Map<String, WebSocketHandler> map = new HashMap<>();
    map.put("/employee-feed", new EmployeeWebSocketHandler());

    SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
    mapping.setUrlMap(map);
    mapping.setOrder(10);
    return mapping;
  }

  @Bean
  public WebSocketHandlerAdapter handlerAdapter() {
    return new WebSocketHandlerAdapter();
  }
}
```

```java
@Component("EmployeeWebSocketHandler")
public class EmployeeWebSocketHandler implements WebSocketHandler {

  ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public Mono<Void> handle(WebSocketSession webSocketSession) {

    Flux<String> employeeCreationEvent = Flux.generate(sink -> {
      EmployeeCreationEvent event = new EmployeeCreationEvent(randomUUID().toString(), now().toString());
      try {
        sink.next(objectMapper.writeValueAsString(event));
      } catch (JsonProcessingException e) {
        sink.error(e);
      }
    });

    return webSocketSession.send(employeeCreationEvent
        .map(webSocketSession::textMessage)
        .delayElements(Duration.ofSeconds(1)));
  }
}
```

```java
public class EmployeeWebSocketClient {

  public static void main(String[] args) {

    WebSocketClient client = new ReactorNettyWebSocketClient();

    client.execute(URI.create("ws://localhost:8080/employee-feed"),
        session -> session.receive()
          .map(WebSocketMessage::getPayloadAsText)
          .doOnNext(System.out::println)
          .then())
          .block(); // to subscribe and return the value
  }
}
```

### References
* Guide to Spring 5 WebFlux, https://www.baeldung.com/spring-webflux
* Reactive WebSocket with Spring 5, https://www.baeldung.com/spring-5-reactive-websockets
* Spring Security 5 for Reactive Applications, https://www.baeldung.com/spring-security-5-reactive