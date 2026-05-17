package com.veteroch4k.crm.controllers.TransactionControllerTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.veteroch4k.crm.BaseIntegrationTest;
import com.veteroch4k.crm.models.PaymentType;
import com.veteroch4k.crm.models.Seller;
import com.veteroch4k.crm.models.Transaction;
import com.veteroch4k.crm.repositories.SellerRepository;
import com.veteroch4k.crm.repositories.TransactionRepository;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired
  TransactionRepository transactionRepository;

  @Autowired
  SellerRepository sellerRepository;

  @BeforeEach
  void setUp() {
    transactionRepository.deleteAll();
  }

  @Test
  void shouldGetTransactions() {

    Seller s1 = new Seller();
    s1.setName("test");
    sellerRepository.save(s1);

    Transaction t1 = new Transaction();
    t1.setSeller(sellerRepository.getReferenceById(s1.getId()));
    t1.setAmount(BigDecimal.valueOf(1.0));
    t1.setPaymentType(PaymentType.CARD);

    Transaction t2 = new Transaction();
    t2.setSeller(sellerRepository.getReferenceById(s1.getId()));
    t2.setAmount(BigDecimal.valueOf(2.0));
    t2.setPaymentType(PaymentType.CASH);

    transactionRepository.save(t1);
    transactionRepository.save(t2);


    given().
        contentType(ContentType.JSON)
    .when()
        .get("/api/transactions")
    .then()
        .statusCode(200)
        .body("content.size()", equalTo(2))
        .body("content[0].id", equalTo(t1.getId().intValue()))
        .body("content[0].amount", equalTo(t1.getAmount().floatValue()))
        .body("content[1].id", equalTo(t2.getId().intValue()))
        .body("content[1].amount", equalTo(t2.getAmount().floatValue()));

  }

  @Test
  void shouldGetSeller() {

    Seller s1 = new Seller();
    s1.setName("test");
    sellerRepository.save(s1);

    Transaction t = new Transaction();
    t.setSeller(sellerRepository.getReferenceById(s1.getId()));
    t.setAmount(BigDecimal.valueOf(1.0));
    t.setPaymentType(PaymentType.CARD);

    transactionRepository.save(t);

    given().
        contentType(ContentType.JSON)
    .when()
        .get("/api/transactions/{id}", t.getId())
    .then()
        .statusCode(200)
        .body("id", equalTo(t.getId().intValue()))
        .body("amount", equalTo(t.getAmount().floatValue()))
        .body("seller.id", equalTo(s1.getId().intValue()))
        .body("seller.name", equalTo(s1.getName()));

  }

  @Test
  void shouldReturn404WhenGetSeller() {
    Seller s1 = new Seller();
    s1.setName("test");
    sellerRepository.save(s1);

    Transaction t = new Transaction();
    t.setSeller(sellerRepository.getReferenceById(s1.getId()));
    t.setAmount(BigDecimal.valueOf(1.0));
    t.setPaymentType(PaymentType.CARD);

    transactionRepository.save(t);

    int notExistingId = 67;

    given().
        contentType(ContentType.JSON)
        .when()
        .get("/api/transactions/{id}", notExistingId)
    .then()
        .statusCode(404);

  }

  @Test
  void shouldGetTransactionsBySeller() {

    Seller s1 = new Seller();
    s1.setName("test");
    sellerRepository.save(s1);

    Transaction t1 = new Transaction();
    t1.setSeller(sellerRepository.getReferenceById(s1.getId()));
    t1.setAmount(BigDecimal.valueOf(1.0));
    t1.setPaymentType(PaymentType.CARD);

    Transaction t2 = new Transaction();
    t2.setSeller(sellerRepository.getReferenceById(s1.getId()));
    t2.setAmount(BigDecimal.valueOf(2.0));
    t2.setPaymentType(PaymentType.CASH);

    transactionRepository.save(t1);
    transactionRepository.save(t2);

    given().
        contentType(ContentType.JSON)
    .when()
        .get("/api/transactions/seller/{id}", s1.getId())
    .then()
        .statusCode(200)
        .body("content.size()", equalTo(2))
        .body("content[0].id", equalTo(t1.getId().intValue()))
        .body("content[0].sellerId", equalTo(s1.getId().intValue()))
        .body("content[0].amount", equalTo(t1.getAmount().floatValue()))
        .body("content[1].id", equalTo(t2.getId().intValue()))
        .body("content[1].sellerId", equalTo(s1.getId().intValue()))
        .body("content[1].amount", equalTo(t2.getAmount().floatValue()));




  }




}
