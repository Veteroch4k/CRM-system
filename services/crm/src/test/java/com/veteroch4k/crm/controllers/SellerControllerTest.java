package com.veteroch4k.crm.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import com.veteroch4k.crm.BaseTest;
import com.veteroch4k.crm.models.Seller;
import com.veteroch4k.crm.repositories.SellerRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SellerControllerTest extends BaseTest {

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
        .body("content[0].contactInfo", equalTo(seller1.getContactInfo()))
        .body("content[0].registrationDate", equalTo(seller1.getRegistrationDate().toString()))
        .body("content[1].id", equalTo(seller2.getId().intValue()))
        .body("content[1].name", equalTo(seller2.getName()))
        .body("content[1].contactInfo", equalTo(seller2.getContactInfo()))
        .body("content[1].registrationDate", equalTo(seller2.getRegistrationDate().toString()));

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
        .body("name", equalTo(seller.getName()))
        .body("contactInfo", equalTo(seller.getContactInfo()))
        .body("registrationDate", equalTo(seller.getRegistrationDate().toString()));

  }

  @Test
  void shouldReturn400WhenNoSuchSeller() {

    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/api/sellers/{id}", 67)
        .then()
        .statusCode(404);


  }

}
