package com.veteroch4k.crm.models.DTO;

import com.veteroch4k.crm.models.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Schema(description = "Данные для создания или обновления транзакции")
public record TransactionRequestDTO(

    @Schema(description = "ID продавца", example = "1")
    @Positive @NotNull
    Long sellerId,

    @Schema(description = "Сумма транзакции", example = "67.67")
    @Min(0) @NotNull
    BigDecimal amount,

    @Schema(description = "Тип оплаты", allowableValues = {"CASH", "CARD", "TRANSFER"})
    @NotNull
    PaymentType paymentType
)
{}
