package com.veteroch4k.crm.controllers;

import com.veteroch4k.crm.models.DTO.analytics.BestPeriodResult;
import com.veteroch4k.crm.models.DTO.analytics.SellerProductivityDTO;
import com.veteroch4k.crm.models.DTO.TransactionDTO.TransactionRequestDTO;
import com.veteroch4k.crm.models.DTO.TransactionDTO.TransactionResponseDTO;
import com.veteroch4k.crm.models.Transaction;
import com.veteroch4k.crm.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction API", description = "API для управления транзакциями")
public class TransactionController {

  private final TransactionService service;

  @Operation(summary = "Получить список всех транзакций",
  description = "Возвращает пагинированный список транзакций")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Транзакции получены"),
      @ApiResponse(responseCode = "400", description = "Переданы некорректные параметры запроса", content = @Content (schema = @Schema (implementation = ErrorResponse.class))
      )
  })
  @GetMapping("")
  public ResponseEntity<Page<TransactionResponseDTO>> getTransactions(
      @Parameter(description = "Номер страницы")
      @RequestParam(defaultValue = "0") @PositiveOrZero int page,

      @Parameter(description = "Размер страницы")
      @RequestParam(defaultValue = "20") @Positive int size
  ) {

    return ResponseEntity.ok(service.getTransactions(page, size));

  }

  @Operation(summary = "Получить инфо о конкретной транзакции",
  description = "Возвращает транзакцию по её ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Транзакция получена"),
      @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных", content = @Content (schema = @Schema (implementation = ErrorResponse.class))
      ),
      @ApiResponse(responseCode = "404", description = "Транзакции по заданному ID не существует", content = @Content (schema = @Schema (implementation = ErrorResponse.class))
      )
  })
  @GetMapping("/{id}")
  public ResponseEntity<Transaction> getTransaction(
      @Parameter(description = "ID искомой транзакции")
      @PathVariable @Positive Long id
  ) {

    return ResponseEntity.ok(service.getTransaction(id));

  }

  @Operation(summary = "Создать новую транзакцию")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Транзакция успешно создана"),
      @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных", content = @Content (schema = @Schema (implementation = ErrorResponse.class))

      )
  })
  @PostMapping("")
  public ResponseEntity<TransactionResponseDTO> createTransaction(
      @Parameter(description = "Данные для создания транзакции")
      @Valid @RequestBody TransactionRequestDTO dto
  ) {

    return ResponseEntity.status(HttpStatus.CREATED).body(service.createTransaction(dto));

  }

  @Operation(summary = "Получить все транзакции продавца",
  description = "Возвращает пагинированный список транзакций конкретного продавца по его ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Транзакции получены"),
      @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных", content = @Content (schema = @Schema (implementation = ErrorResponse.class))
      )
  })
  @GetMapping("/seller/{id}")
  public ResponseEntity<Page<TransactionResponseDTO>> getTransactionsBySeller(
      @Parameter(description = "ID продавца")
      @PathVariable("id") @Positive Long id,

      @Parameter(description = "Номер страницы")
      @RequestParam(defaultValue = "0") @PositiveOrZero int page,

      @Parameter(description = "Размер страницы")
      @RequestParam(defaultValue = "20") @Positive int size

  ) {
    return ResponseEntity.ok(service.getTransactionsBySeller(id, page, size));
  }


  @Operation(summary = "Получить самого эффективного продавца",
  description = "Возвращает пагинированный список лучших продавцов за указанный период")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Данные успешно получены"),
      @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных", content = @Content (schema = @Schema (implementation = ErrorResponse.class))
      ),
      @ApiResponse(responseCode = "404", description = "За указанный период не было никаких транзакций", content = @Content (schema = @Schema (implementation = ErrorResponse.class))
      )
  })
  @GetMapping("/analytics/top-seller")
  public ResponseEntity<Page<SellerProductivityDTO>> getMostProductiveSeller(
      @Parameter(description = "Начало диапазона", example = "2025-05-15T13:40:25")
      @RequestParam @PastOrPresent  @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startDate,

      @Parameter(description = "Конец диапазона", example = "2026-05-17T15:11:49")
      @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime endDate,

      @Parameter(description = "Номер страницы")
      @RequestParam(defaultValue = "0") @PositiveOrZero int page,

      @Parameter(description = "Размер страницы")
      @RequestParam(defaultValue = "20") @Positive int size

  ) {

    return ResponseEntity.ok(service.getMostProductiveSeller(startDate, endDate, page, size));
  }

  @Operation(summary = "Получить список продавцов с суммой меньше указанной за выбранный период",
  description = "Выводит пагинированный список продавцов, у которых сумма всех транзакций за выбранные период"
      + " меньше переданного параметра суммы")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Данные успешно получены"),
      @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных", content = @Content (schema = @Schema (implementation = ErrorResponse.class))
      ),
      @ApiResponse(responseCode = "404", description = "За указанный период не было никаких транзакций", content = @Content (schema = @Schema (implementation = ErrorResponse.class))
      )
  })
  @GetMapping("/analytics/outsiders")
  public ResponseEntity<Page<SellerProductivityDTO>> getSellersOutsiders(
      @Parameter(description = "Начало диапазона", example = "2025-05-15T13:40:25")
      @RequestParam @PastOrPresent  @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startDate,

      @Parameter(description = "Конец диапазона", example = "2026-05-17T15:11:49")
      @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime endDate,

      @Parameter(description = "переданный параметр суммы")
      @RequestParam(defaultValue = "6767.67") @PositiveOrZero BigDecimal target,

      @Parameter(description = "Номер страницы")
      @RequestParam(defaultValue = "0") @PositiveOrZero int page,

      @Parameter(description = "Размер страницы")
      @RequestParam(defaultValue = "20") @Positive int size
  ) {

    return ResponseEntity.ok(service.getSellersOutsiders(startDate, endDate, target, page, size));
  }

  @Operation(summary = "Получить самое продуктивное время продавца",
  description = "Возвращает наилучший период времени продавца по количеству совершенных транзакций")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Данные успешно получены"),
      @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных", content = @Content (schema = @Schema (implementation = ErrorResponse.class))
      ),
      @ApiResponse(responseCode = "404", description = "Искомого продавца не существует", content = @Content (schema = @Schema (implementation = ErrorResponse.class))
      )
  })
  @GetMapping("/analytics/best-period-seller/{sellerId}")
  public ResponseEntity<BestPeriodResult> getBestPeriodOfSeller(
      @Parameter(description = "ID рассматриваемого продавца")
      @PathVariable("sellerId") @Positive Long id,

      @Parameter(description = "Искомый период времени", example = "30")
      @RequestParam(defaultValue = "30") @Positive Integer days

  ) {

    return ResponseEntity.ok(service.getBestPeriodOfSeller(id, Duration.ofDays(days)));

  }


}
