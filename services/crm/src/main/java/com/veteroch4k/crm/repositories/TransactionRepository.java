package com.veteroch4k.crm.repositories;

import com.veteroch4k.crm.models.DTO.SellerProductivityDTO;
import com.veteroch4k.crm.models.Seller;
import com.veteroch4k.crm.models.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  @EntityGraph(attributePaths = {"seller"})
  Optional<Transaction> findById(Long id);

  Page<Transaction> findAllBySellerId(Long sellerId, Pageable pageable);


  @Query(value = """
      WITH seller_totals AS (
                  SELECT
                      s.id as id,
                      s.name as name,
                      SUM(t.amount) as total_amount,
                      RANK() OVER (ORDER BY SUM(t.amount) DESC) as rank_place
                  FROM transactions t
                  JOIN sellers s ON t.seller_id = s.id
                  WHERE t.transaction_date >= :startDate AND t.transaction_date <= :endDate
                  GROUP BY s.id, s.name
              )
              SELECT id, name, total_amount
              FROM seller_totals
              WHERE rank_place = 1
              ORDER BY name;
      """, nativeQuery = true)
  Page<SellerProductivityDTO> findMostProductiveSeller(
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate,
      Pageable pageable
  );

}
