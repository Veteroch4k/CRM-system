package com.veteroch4k.crm.controllers.TransactionControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.veteroch4k.crm.controllers.TransactionController;
import com.veteroch4k.crm.services.TransactionService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransactionController.class)
public class TransactionControllerWebTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  TransactionService service;

  @Test
  void shouldReturn400WhenGetTransactionsParamsAreNotValid() throws Exception {
    String page = "-1";
    String size = "0";

    mockMvc.perform(
            get("/api/transactions")
                .param("page", page)
                .param("size", size))
        .andExpectAll(
            status().isBadRequest(),
            jsonPath("$.message").value("must be greater than or equal to 0"));
  }

  @Test
  void shouldReturn400WhenGetTransactionPathVariableIdIsNotValid() throws Exception {

    String notValidId = "0";

    mockMvc.perform(
            get("/api/transactions/{id}", notValidId)
        )
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.message").value("must be greater than 0"));

  }

  @Test
  void shouldReturn400WhenGetTransactionsBySellerParamsAreNotValid() throws Exception {
    String page = "-1";
    String size = "0";
    String id = "1";

    mockMvc.perform(
            get("/api/transactions/seller/{id}", id)
                .param("page", page)
                .param("size", size))
        .andExpectAll(
            status().isBadRequest(),
            jsonPath("$.message").value("must be greater than or equal to 0"));
  }

  @Test
  void shouldReturn400WhenGetTransactionsBySellerPathVariableIdIsNotValid() throws Exception {
    String notValidId = "-1";

    mockMvc.perform(
            get("/api/transactions/seller/{id}", notValidId)
        )
        .andExpectAll(
            status().isBadRequest(),
            jsonPath("$.message").value("must be greater than 0"));
  }

  @Test
  void shouldReturn400WhenGetMostProductiveSellerParamsAreNotValid() throws Exception {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String endDate = LocalDateTime.now().minusMonths(1).format(formatter);
    String startDate = LocalDateTime.now().plusDays(1).format(formatter);


    mockMvc.perform(
        get("/api/transactions/analytics/top-seller")
            .param("startDate", startDate)
            .param("endDate", endDate)
        )
        .andExpectAll(
            status().isBadRequest(),
            jsonPath("$.message").value("must be a date in the past or in the present")
        );
  }

}
