package com.veteroch4k.crm.models.DTO.analytics;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Данные о лучшем периоде продавца по количеству транзакций")
public record BestPeriodResult(

    @Schema(description = "Начало периода")
    LocalDateTime startPeriod,

    @Schema(description = "Конец периода")
    LocalDateTime endPeriod,

    @Schema(description = "Количество транзакций за данный период")
    Integer count

) {}
