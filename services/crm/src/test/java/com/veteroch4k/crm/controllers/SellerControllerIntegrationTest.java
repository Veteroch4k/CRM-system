package com.veteroch4k.crm.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import com.veteroch4k.crm.BaseIntegrationTest;
import com.veteroch4k.crm.models.DTO.SellerDTO;
import com.veteroch4k.crm.models.Seller;
import com.veteroch4k.crm.repositories.SellerRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SellerControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired
  SellerRepository repository;

  @BeforeEach
  public void setUp() {
    repository.deleteAll();
  }

  @Test
  void shouldGetAllSellers() {

    Seller seller1 = new Seller();
    seller1.setName("Test1");
    Seller seller2 = new Seller();
    seller2.setName("Test2");

    repository.save(seller1);
    repository.save(seller2);

    given()
        .contentType(ContentType.JSON)
    .when()
        .get("/api/sellers")
    .then()
        .statusCode(200)
        .body("content.size()", equalTo(2))
        .body("content[0].id", equalTo(seller1.getId().intValue()))
        .body("content[0].name", equalTo(seller1.getName()))
        .body("content[1].id", equalTo(seller2.getId().intValue()))
        .body("content[1].name", equalTo(seller2.getName()));

  }

  @Test
  void shouldGetSeller() {

    Seller seller = new Seller();
    seller.setName("test");

    repository.save(seller);

    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/api/sellers/{id}", seller.getId())
        .then()
        .statusCode(200)
        .body("id", equalTo(seller.getId().intValue()))
        .body("name", equalTo(seller.getName()));

  }

  @Test
  void shouldReturn404WhenGetSeller() {

    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/api/sellers/{id}", 67)
        .then()
        .statusCode(404);


  }

  @Test
  void shouldCreateSeller() {

    SellerDTO dto = new SellerDTO(
        "testName",
        "testInfo"
    );

    given()
        .contentType(ContentType.JSON)
        .body(dto)
    .when()
        .post("/api/sellers")
    .then()
        .statusCode(201);

  }

  @Test
  void shouldUpdateSeller() {

    Seller seller = new Seller();
    seller.setName("testName");

    SellerDTO dto = new SellerDTO(
        "UpdatedName",
        ""
    );

    repository.save(seller);

    given()
        .contentType(ContentType.JSON)
        .body(dto)
    .when()
        .put("api/sellers/{id}", seller.getId())
    .then()
        .statusCode(204);

  }

  @Test
  void shouldReturn404WhenUpdateSeller() {

    SellerDTO dto = new SellerDTO(
        "UpdatedName",
        ""
    );

    given()
        .contentType(ContentType.JSON)
        .body(dto)
    .when()
        .put("api/sellers/{id}", 67)
    .then()
        .statusCode(404);
  }

  @Test
  void shouldDeleteSeller() {

    Seller seller = new Seller();
    seller.setName("testName");

    repository.save(seller);

    given()
        .contentType(ContentType.JSON)
    .when()
        .delete("/api/sellers/{id}", seller.getId())
    .then()
        .statusCode(204);

  }

  @Test
  void shouldReturn404WhenDeleteSeller() {

    int notExistingId = 67;

    given()
        .contentType(ContentType.JSON)
    .when()
        .delete("/api/sellers/{id}", notExistingId)
    .then()
        .statusCode(404);


  }

}
