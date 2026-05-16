package com.veteroch4k.crm.models.DTO;

import com.veteroch4k.crm.models.PaymentType;
import com.veteroch4k.crm.models.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
    Long id,
    Long sellerId,
    BigDecimal amount,
    PaymentType paymentType,
    LocalDateTime transactionDate
) {

  public TransactionResponseDTO(Transaction transaction) {
    this(transaction.getId(),transaction.getSeller().getId(), transaction.getAmount(),
        transaction.getPaymentType(), transaction.getTransactionDate());

  }
}
