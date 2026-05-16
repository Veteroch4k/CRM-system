package com.veteroch4k.crm.services;

import com.veteroch4k.crm.exceptions.ResourceNotFoundException;
import com.veteroch4k.crm.models.DTO.SellerDTO;
import com.veteroch4k.crm.models.Seller;
import com.veteroch4k.crm.repositories.SellerRepository;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService {

  private final SellerRepository repository;

  public Page<Seller> getSellers(int page, int size) {

    return repository.findAll(PageRequest.of(page,size));

  }

  public Seller getSellerById(Long id) {

    return  repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Продавец с ID: " + id + " не найден"));


  }


  public Seller createSeller(SellerDTO sellerDTO) {

    Seller seller = new Seller();
    seller.setName(sellerDTO.name());
    seller.setContactInfo(sellerDTO.contactInfo());

    return repository.save(seller);

  }
}
