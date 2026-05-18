package com.veteroch4k.crm.controllers.TransactionControllerTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

import com.veteroch4k.crm.BaseIntegrationTest;
import com.veteroch4k.crm.models.PaymentType;
import com.veteroch4k.crm.models.Seller;
import com.veteroch4k.crm.models.Transaction;
import com.veteroch4k.crm.repositories.SellerRepository;
import com.veteroch4k.crm.repositories.TransactionRepository;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

  @Test
  void shouldGetMostProductiveSeller() {

    // Данные о первом продавце (топовый)

    Seller s1 = new Seller();
    s1.setName("top1");
    sellerRepository.save(s1);

    Transaction t1 = new Transaction();
    t1.setSeller(sellerRepository.getReferenceById(s1.getId()));
    t1.setAmount(BigDecimal.valueOf(10.0));
    t1.setPaymentType(PaymentType.CARD);

    Transaction t2 = new Transaction();
    t2.setSeller(sellerRepository.getReferenceById(s1.getId()));
    t2.setAmount(BigDecimal.valueOf(10.0));
    t2.setPaymentType(PaymentType.CASH);


    transactionRepository.save(t1);
    transactionRepository.save(t2);


    // Данные о втором продавце (не топовый)

    Seller s2 = new Seller();
    s2.setName("top2");
    sellerRepository.save(s2);

    Transaction t1_2 = new Transaction();
    t1_2.setSeller(sellerRepository.getReferenceById(s2.getId()));
    t1_2.setAmount(BigDecimal.valueOf(5.0));
    t1_2.setPaymentType(PaymentType.CARD);

    Transaction t2_2 = new Transaction();
    t2_2.setSeller(sellerRepository.getReferenceById(s2.getId()));
    t2_2.setAmount(BigDecimal.valueOf(5.0));
    t2_2.setPaymentType(PaymentType.CASH);

    transactionRepository.save(t1_2);
    transactionRepository.save(t2_2);

    // Данные о третьем продавце (тоже топовый)

    Seller s3 = new Seller();
    s3.setName("top1-1");
    sellerRepository.save(s3);

    Transaction t1_1 = new Transaction();
    t1_1.setSeller(sellerRepository.getReferenceById(s3.getId()));
    t1_1.setAmount(BigDecimal.valueOf(15.0));
    t1_1.setPaymentType(PaymentType.CARD);

    Transaction t2_1 = new Transaction();
    t2_1.setSeller(sellerRepository.getReferenceById(s3.getId()));
    t2_1.setAmount(BigDecimal.valueOf(5.0));
    t2_1.setPaymentType(PaymentType.CASH);

    transactionRepository.save(t1_1);
    transactionRepository.save(t2_1);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String startDate = LocalDateTime.now().minusMonths(1).format(formatter);
    String endDate = LocalDateTime.now().plusDays(1).format(formatter);

    given().
        contentType(ContentType.JSON)
        .queryParam("startDate", startDate)
        .queryParam("endDate", endDate)
    .when()
        .get("/api/transactions/analytics/top-seller")
    .then()
        .statusCode(200)
        .body("content.size()", equalTo(2))
        .body("content[0].sellerId", equalTo(s1.getId().intValue()))
        .body("content[0].sellerName", equalTo(s1.getName()))
        .body("content[0].totalAmount", equalTo(20.0f))
        .body("content[1].sellerId", equalTo(s3.getId().intValue()))
        .body("content[1].sellerName", equalTo(s3.getName()))
        .body("content[1].totalAmount", equalTo(20.0f));

  }

  @Test
  void shouldReturn404WhenGetMostProductiveSeller() {

    Seller s1 = new Seller();
    s1.setName("top1");
    sellerRepository.save(s1);

    Transaction t1 = new Transaction();
    t1.setSeller(sellerRepository.getReferenceById(s1.getId()));
    t1.setAmount(BigDecimal.valueOf(10.0));
    t1.setPaymentType(PaymentType.CARD);

    transactionRepository.save(t1);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String startDate = LocalDateTime.now().minusMonths(2).format(formatter);
    String endDate = LocalDateTime.now().minusMonths(1).format(formatter);

    given().
        contentType(ContentType.JSON)
        .queryParam("startDate", startDate)
        .queryParam("endDate", endDate)
    .when()
        .get("/api/transactions/analytics/top-seller")
    .then()
        .statusCode(404)
        .body("message", equalTo("За указанный период " + startDate + " - " + endDate
            + " не было никаких транзакций"));

  }

  @Test
  void shouldGetSellersOutsiders() {

    // Данные о первом продавце

    Seller s1 = new Seller();
    s1.setName("top1");
    sellerRepository.save(s1);

    Transaction t1 = new Transaction();
    t1.setSeller(sellerRepository.getReferenceById(s1.getId()));
    t1.setAmount(BigDecimal.valueOf(10.0));
    t1.setPaymentType(PaymentType.CARD);


    transactionRepository.save(t1);


    // Данные о втором продавце

    Seller s2 = new Seller();
    s2.setName("top2");
    sellerRepository.save(s2);

    Transaction t1_2 = new Transaction();
    t1_2.setSeller(sellerRepository.getReferenceById(s2.getId()));
    t1_2.setAmount(BigDecimal.valueOf(5.0));
    t1_2.setPaymentType(PaymentType.CARD);


    transactionRepository.save(t1_2);

    // Данные о третьем продавце (не аутсайдер)

    BigDecimal target = new BigDecimal("15.0");


    Seller s3 = new Seller();
    s3.setName("top1-1");
    sellerRepository.save(s3);

    Transaction t1_1 = new Transaction();
    t1_1.setSeller(sellerRepository.getReferenceById(s3.getId()));
    t1_1.setAmount(target);
    t1_1.setPaymentType(PaymentType.CARD);


    transactionRepository.save(t1_1);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String startDate = LocalDateTime.now().minusMonths(1).format(formatter);
    String endDate = LocalDateTime.now().plusDays(1).format(formatter);


    given().
        contentType(ContentType.JSON)
        .queryParam("startDate", startDate)
        .queryParam("endDate", endDate)
        .queryParam("target", target)
    .when()
        .get("/api/transactions/analytics/outsiders")
    .then()
        .statusCode(200)
        .body("content.size()", equalTo(2))
        .body("content[0].sellerId", equalTo(s1.getId().intValue()))
        .body("content[0].sellerName", equalTo(s1.getName()))
        .body("content[0].totalAmount", lessThan(target.floatValue()))
        .body("content[1].sellerId", equalTo(s2.getId().intValue()))
        .body("content[1].sellerName", equalTo(s2.getName()))
        .body("content[1].totalAmount",  lessThan(target.floatValue()));

  }

  @Test
  void shouldReturn404WhenGetSellersOutsiders() {

    BigDecimal target = new BigDecimal("15.0");

    Seller s3 = new Seller();
    s3.setName("top1-1");
    sellerRepository.save(s3);

    Transaction t1_1 = new Transaction();
    t1_1.setSeller(sellerRepository.getReferenceById(s3.getId()));
    t1_1.setAmount(target);
    t1_1.setPaymentType(PaymentType.CARD);


    transactionRepository.save(t1_1);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String startDate = LocalDateTime.now().minusMonths(2).format(formatter);
    String endDate = LocalDateTime.now().minusMonths(1).format(formatter);

    given().
        contentType(ContentType.JSON)
        .queryParam("startDate", startDate)
        .queryParam("endDate", endDate)
        .queryParam("target", target)
    .when()
        .get("/api/transactions/analytics/outsiders")
    .then()
        .statusCode(404)
        .body("message", equalTo("За указанный период " + startDate + " - " + endDate
            + " не было никаких транзакций"));
  }





}
