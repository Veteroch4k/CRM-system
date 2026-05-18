package com.veteroch4k.crm.services;

import com.veteroch4k.crm.exceptions.ResourceNotFoundException;
import com.veteroch4k.crm.models.DTO.analytics.BestPeriodResult;
import com.veteroch4k.crm.models.DTO.analytics.SellerProductivityDTO;
import com.veteroch4k.crm.models.DTO.TransactionRequestDTO;
import com.veteroch4k.crm.models.DTO.TransactionResponseDTO;
import com.veteroch4k.crm.models.Transaction;
import com.veteroch4k.crm.repositories.SellerRepository;
import com.veteroch4k.crm.repositories.TransactionRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final SellerRepository sellerRepository;


  public Page<TransactionResponseDTO> getTransactions(int page, int size) {

    Page<Transaction> sellerPage = transactionRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));

    return sellerPage.map(TransactionResponseDTO::new);

  }

  /**
   * Получить инфо о конкретной транзакции.
   *
   * @param id айди транзакции
   * @return Возвращает сущность, а не её DTO, так как сама сущность Seller не хранит какой-то
   * важной/конфиденциальной информации. В идеале возвращать какую-нибудь DTO типа TransactionResponseWithSellerDTO,
   * и создать SellerResponseDTO, хранящий только имя + контактную информацию. Но на данном этапе это избыточно.
   *
   */
  public Transaction getTransaction(Long id) {

    return transactionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Транзакция с ID: " + id + " не найдена"));

  }

  public TransactionResponseDTO createTransaction(TransactionRequestDTO dto) {
    
    Transaction t = new Transaction();
    t.setSeller(sellerRepository.getReferenceById(dto.sellerId()));
    t.setAmount(dto.amount());
    t.setPaymentType(dto.paymentType());

    transactionRepository.save(t);

    return new TransactionResponseDTO(t);
    
  }

  /**
   * Получить пагинированный список транзакций, принадлежащих заданному продавцу по его ID
   *
   * @param id айди искомого продавца
   * @param page номер страниц (начиная от 0)
   * @param size размер страницы (минимум 1)
   * @return Возвращает пагинированный список, отсортированный по возрастанию ID транзакций
   */
  public Page<TransactionResponseDTO> getTransactionsBySeller(Long id, int page, int size) {

    Page<Transaction> transactions = transactionRepository.findAllBySellerId(id, PageRequest.of(page, size, Sort.by("id").ascending()));
    return transactions.map(TransactionResponseDTO::new);

  }


  /**
   * Получить самого эффективного продавца за указанный диапазон (самый продуктивный тот, у которого
   * сумма всех транзакций больше всех других продавцов).
   *
   * @param startDate начало диапазона
   * @param endDate   конец диапазона
   * @param page      номер страницы
   * @param size      размер страницы
   * @return Возвращает пагинированный список, так как продавцов может быть несколько (с одинаковыми
   * суммами транзакций)
   */
  public Page<SellerProductivityDTO> getMostProductiveSeller(LocalDateTime startDate, LocalDateTime endDate,
      int page, int size)
  {

    if(startDate.isAfter(endDate)) throw new IllegalArgumentException("Дата начала не может быть позже даты окончания");

    Page<SellerProductivityDTO> top = transactionRepository.findMostProductiveSeller(startDate, endDate,
        PageRequest.of(page, size));

    if(top.isEmpty()) throw new ResourceNotFoundException("За указанный период " + startDate + " - " + endDate
        + " не было никаких транзакций");

    return top;

  }


  /**
   * Получить список продавцов с суммой меньше указанной за выбранный период
   *
   * @param startDate начало диапазона
   * @param endDate конец диапазона
   * @param target переданный параметр суммы
   * @param page номер страницы
   * @param size размер страницы
   * @return Возвращает пагинированный список подходящих под условие продавцов
   */
  public Page<SellerProductivityDTO> getSellersOutsiders(LocalDateTime startDate, LocalDateTime endDate,
      BigDecimal target, int page, int size)
  {
    if(startDate.isAfter(endDate)) throw new IllegalArgumentException("Дата начала не может быть позже даты окончания");

    Page<SellerProductivityDTO> top = transactionRepository.findOutsiders(startDate, endDate, target,
        PageRequest.of(page, size));

    if(top.isEmpty()) throw new ResourceNotFoundException("За указанный период " + startDate + " - " + endDate
        + " не было никаких транзакций");

    return top;

  }

  /**
   * Получить самое продуктивное время продавца (диапазон дат).
   * Самое продуктивное время то, где продавец совершил наибольшее кол-во транзакций.
   *
   * @param sellerId ID рассматриваемого продавца
   * @param duration Длительность наилучшего периода времени (в днях)
   * @return Возвращает лучший период продавца
   */
  @Transactional
  public BestPeriodResult getBestPeriodOfSeller(Long sellerId, Duration duration) {

    if(!sellerRepository.existsById(sellerId)) throw new ResourceNotFoundException("Продавца с ID: " + sellerId + " не существует");

    List<LocalDateTime> dates = transactionRepository.findDatesBySellerId(sellerId)
        .stream()
        .map(Timestamp::toLocalDateTime)
        .toList();

    if(dates.isEmpty()) throw new ResourceNotFoundException("У продавца с ID: " + sellerId + " нет транзакций");

    int maxCount = 0;
    LocalDateTime startPeriod = null, endPeriod = null;

    int left = 0, right = 0;

    while (right < dates.size()) {

      while (Duration.between(dates.get(left), dates.get(right)).compareTo(duration) > 0) left++;

      int curCount = right - left + 1;

      if (curCount > maxCount) {
        maxCount = curCount;
        startPeriod = dates.get(left);
        endPeriod = dates.get(right);
      }
      right++;
    }
    return new BestPeriodResult(startPeriod, endPeriod, maxCount);

  }
}
