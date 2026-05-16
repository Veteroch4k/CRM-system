package com.veteroch4k.crm.controllers;

import com.veteroch4k.crm.exceptions.ErrorResponse;
import com.veteroch4k.crm.models.DTO.SellerDTO;
import com.veteroch4k.crm.models.Seller;
import com.veteroch4k.crm.services.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sellers")
@Tag(name = "Seller API", description = "API для управления продавцами")
public class SellerController {

  private final SellerService service;

  @Operation(summary = "Получить список всех продавцов",
  description = "Возвращает список продавцов")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Продавцы получены"),
      @ApiResponse(
          responseCode = "400",
          description = "Переданы некорректные параметры запроса",
          content = @Content (schema = @Schema (implementation = ErrorResponse.class))
      )
  })
  @GetMapping("")
  public ResponseEntity<Page<Seller>>  getSellers(
      @Parameter(description = "Номер страницы")
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @Parameter(description = "Размер страницы")
      @RequestParam(defaultValue = "20") @Min(1) int size) {

    return ResponseEntity.ok(service.getSellers(page,size));
  }

  @Operation(summary = "Получить инфо о конкретном продавце",
      description = "Возвращает конкретном продавца по его ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Продавцы получены"),
      @ApiResponse(
          responseCode = "400",
          description = "Переданы некорректные параметры запроса",
          content = @Content (schema = @Schema (implementation = ErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Продавца по заданному ID не существует",
          content = @Content (schema =  @Schema (implementation = ErrorResponse.class))
      )
  })
  @GetMapping("/{id}")
  public ResponseEntity<Seller> getSeller(
      @Parameter(description = "ID продавца")
      @PathVariable("id") @Positive Long id) {

    return ResponseEntity.ok(service.getSellerById(id));

  }

  @Operation(summary = "Создать нового продавца")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Продавец успешно создан"),
      @ApiResponse(
          responseCode = "400",
          description = "Ошибка валидации входных данных",
          content = @Content (schema =  @Schema (implementation = ErrorResponse.class))
      )
  })
  @PostMapping("")
  public ResponseEntity<Seller> createSeller(
      @Parameter(description = "Данные для создания продавца")
      @Valid @RequestBody SellerDTO sellerDTO) {

    return ResponseEntity.status(HttpStatus.CREATED).body(service.createSeller(sellerDTO));


  }


}
