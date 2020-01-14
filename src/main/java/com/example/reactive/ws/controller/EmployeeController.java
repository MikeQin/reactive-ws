package com.example.reactive.ws.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
