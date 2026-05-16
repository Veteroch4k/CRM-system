package com.veteroch4k.crm.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "transaction")
@Schema(description = "Транзакции, осуществленные продавцами")
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "Уникальный идентификатор транзакции", example = "1")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id", nullable = false)
  @Schema(description = "Ссылка на продавца, к которому относится транзакция")
  private Seller seller;

  @Schema(description = "Сумма транзакции", example = "25652.67", nullable = false, minContains = 0)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_type", nullable = false)
  @Schema(description = "ТИип оплаты",
      allowableValues = {"CASH", "CARD", "TRANSFER"})
  private PaymentType paymentType;

  @Column(name = "transaction_date", nullable = false)
  @CurrentTimestamp
  @Schema(description = "Дата и время совершения транзакции",
      example = "2007-12-03T10:15:30")
  private LocalDateTime transactionDate;

}

enum PaymentType {
  CASH,
  CARD,
  TRANSFER
}
