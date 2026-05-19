package com.veteroch4k.crm.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Данные об ошибке")
public record ErrorResponse (
    @Schema(description = "Время, когда произошла ошибка")
    LocalDateTime time,

    @Schema(description = "Статус ошибки", example = "404")
    int status,

    @Schema(description = "Причина ошибки", example = "Not Found")
    String error,

    @Schema(description = "Сообщение ошибки", example = "У продавца с ID: 10 нет транзакций")
    String message
)
{}
