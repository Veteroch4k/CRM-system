package com.veteroch4k.crm.repositories;

import com.veteroch4k.crm.models.Transaction;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  @EntityGraph(attributePaths = {"seller"})
  Optional<Transaction> findById(Long id);

  Page<Transaction> findAllBySellerId(Long sellerId, Pageable pageable);

}
