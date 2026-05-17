package com.veteroch4k.crm.controllers.TransactionControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.veteroch4k.crm.controllers.TransactionController;
import com.veteroch4k.crm.services.TransactionService;
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

}
