package com.veteroch4k.crm.services;

import com.veteroch4k.crm.exceptions.ResourceNotFoundException;
import com.veteroch4k.crm.models.DTO.TransactionResponseDTO;
import com.veteroch4k.crm.models.Transaction;
import com.veteroch4k.crm.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionRepository repository;


  public Page<TransactionResponseDTO> getTransactions(int page, int size) {

    Page<Transaction> sellerPage = repository.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));

    return sellerPage.map(TransactionResponseDTO::new);

  }

  public Transaction getTransaction(Long id) {

    return repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Транзакция с ID: " + id + " не найдена"));

  }
}
