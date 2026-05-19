package com.veteroch4k.crm.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import com.veteroch4k.crm.models.DTO.analytics.BestPeriodResult;
import com.veteroch4k.crm.repositories.SellerRepository;
import com.veteroch4k.crm.repositories.TransactionRepository;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private SellerRepository sellerRepository;

  @InjectMocks
  private TransactionService transactionService;

  @Test
  void shouldCalculateBestPeriodCorrectly() {
    Long sellerId = 1L;
    when(sellerRepository.existsById(sellerId)).thenReturn(true);

    List<Timestamp> mockDates = List.of(
        Timestamp.valueOf(LocalDateTime.of(2026, 5, 1, 10, 0)),
        Timestamp.valueOf(LocalDateTime.of(2026, 5, 2, 12, 0)),
        Timestamp.valueOf(LocalDateTime.of(2026, 5, 3, 15, 0)),
        Timestamp.valueOf(LocalDateTime.of(2026, 5, 20, 10, 0))
    );
    when(transactionRepository.findDatesBySellerId(sellerId)).thenReturn(mockDates);

    BestPeriodResult result = transactionService.getBestPeriodOfSeller(sellerId, Duration.ofDays(5));

    assertEquals(3, result.count().intValue());
    assertEquals(LocalDateTime.of(2026, 5, 1, 10, 0), result.startPeriod());
    assertEquals(LocalDateTime.of(2026, 5, 3, 15, 0), result.endPeriod());
  }

}
