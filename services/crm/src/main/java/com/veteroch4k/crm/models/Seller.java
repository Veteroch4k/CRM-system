package com.veteroch4k.crm.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sellers")
@Schema(description = "Сущность продавца")
public class Seller {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "Уникальный идентификатор продавца", example = "1")
  private Long id;

  @Schema(description = "Имя продавца",
      example = "Иваныч", nullable = false, maxLength = 50)
  private String name;

  @Column(name = "contact_info")
  @Schema(description = "Контактная информация продавца",
      example = "почта: ivanich@mail.ru", nullable = true, maxLength = 1000)
  private String contactInfo;

  @Column(name = "registration_date")
  @CurrentTimestamp
  @Schema(description = "Дата и время регистрации продавца в системе",
      example = "2007-12-03T10:15:30")
  private LocalDateTime registrationDate;

}
