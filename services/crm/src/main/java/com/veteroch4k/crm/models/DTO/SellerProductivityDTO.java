package com.veteroch4k.crm.models.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Данные о самом эффективном продавце")
public record SellerProductivityDTO(

    @Schema(description = "ID продавца", example = "1")
    Long sellerId,

    @Schema(description = "Имя продавца", example = "Иван")
    String sellerName,

    @Schema(description = "Сумма транзакций", example = "1267.67")
    BigDecimal totalAmount
) {

}
