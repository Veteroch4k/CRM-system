package com.veteroch4k.crm.controllers;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {

  @LocalServerPort
  private Integer port;

  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

  static {
    postgres.start();
  }

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

}
