package com.veteroch4k.crm.repositories;

import com.veteroch4k.crm.models.Seller;
import java.util.Optional;
import jdk.dynalink.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {



}
