package com.veteroch4k.crm.controllers;

import com.veteroch4k.crm.services.SellerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SellerController.class)
public class SellerControllerWebTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  SellerService service;

  @Test
  void shouldReturn400WhenGetSellersParamsAreNotValid() throws Exception {

    String page = "-1";
    String size = "0";

    mockMvc.perform(
            get("/api/sellers")
                .param("page", page)
                .param("size", size))
        .andExpectAll(
            status().isBadRequest(),
            jsonPath("$.message").value("must be greater than or equal to 0"));

  }

  @Test
  void shouldReturn400WhenGetSellerPathVariableIdIsNotValid() throws Exception {

    String notValidId = "0";

    mockMvc.perform(
        get("/api/sellers/{id}", notValidId)
        )
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.message").value("must be greater than 0"));

  }

  @Test
  void shouldReturn400WhenCreateSellerBodySellerDTOIsNotValid() throws Exception {

    String notValidSeller = """
        {
          "name": "",
          "contactInfo": ""
        }
        """;

    mockMvc.perform(
        post("/api/sellers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(notValidSeller)
        )
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.message").value("Имя продавца не может быть пустым"));

  }

  @Test
  void shouldReturn400WhenUpdateSellerPathVariableIdIsNotValid() throws Exception {

    String ValidSeller = """
        {
          "name": "Test",
          "contactInfo": ""
        }
        """;

    String notValidId = "0";

    mockMvc.perform(
        put("/api/sellers/{id}", notValidId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(ValidSeller)
        )
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.message").value("must be greater than 0"));

  }

  @Test
  void shouldReturn400WhenUpdateSellerSellerDTOIsNotValid() throws Exception {

    String notValidSeller = """
        {
          "name": "",
          "contactInfo": ""
        }
        """;

    String validId = "1";

    mockMvc.perform(
        put("/api/sellers/{id}", validId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(notValidSeller)
        )
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.message").value("Имя продавца не может быть пустым"));

  }

  @Test
  void shouldReturn400WhenDeleteSellerPathVariableIdIsNotValid() throws Exception {

    String notValidId = "0";

    mockMvc.perform(
            delete("/api/sellers/{id}", notValidId)
        )
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.message").value("must be greater than 0"));


  }



}
