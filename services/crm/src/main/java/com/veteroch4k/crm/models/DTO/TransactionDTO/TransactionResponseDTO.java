package com.veteroch4k.crm.models.DTO.TransactionDTO;

import com.veteroch4k.crm.models.PaymentType;
import com.veteroch4k.crm.models.Transaction;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Ответ с данными о транзакции")
public record TransactionResponseDTO(

    @Schema(description = "ID транзакции", example = "1")
    Long id,

    @Schema(description = "ID продавца", example = "1")
    Long sellerId,

    @Schema(description = "Сумма транзакции", example = "67.67")
    BigDecimal amount,

    @Schema(description = "Тип оплаты", allowableValues = {"CASH", "CARD", "TRANSFER"})
    PaymentType paymentType,

    @Schema(description = "Дата и время совершения транзакции",
        example = "2007-12-03T10:15:30")
    LocalDateTime transactionDate
) {

  public TransactionResponseDTO(Transaction transaction) {
    this(transaction.getId(),transaction.getSeller().getId(), transaction.getAmount(),
        transaction.getPaymentType(), transaction.getTransactionDate());

  }
}
