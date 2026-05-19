package com.veteroch4k.crm.models.DTO.SellerDTO;

import com.veteroch4k.crm.models.Seller;
import com.veteroch4k.crm.models.Transaction;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import org.hibernate.annotations.CurrentTimestamp;

@Schema(description = "Ответ с данными о продавце")
public record SellerResponseDTO (

  @Schema(description = "Уникальный идентификатор продавца", example = "1")
  Long id,

  @Schema(description = "Имя продавца",
      example = "Иваныч", nullable = false, maxLength = 50)
  String name,

  @Schema(description = "Контактная информация продавца",
      example = "почта: ivanich@mail.ru", nullable = true, maxLength = 1000)
  String contactInfo,

  @Schema(description = "Дата и время регистрации продавца в системе",
      example = "2007-12-03T10:15:30")
  LocalDateTime registrationDate,

  @Schema(description = "Флаг удалена ли запись",
      allowableValues = {"true", "false"})
  boolean deleted

)
{
  public SellerResponseDTO(Seller seller) {
    this(seller.getId(),seller.getName(), seller.getContactInfo(),
       seller.getRegistrationDate(), seller.isDeleted());

  }

}
