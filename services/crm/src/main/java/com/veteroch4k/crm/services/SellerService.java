package com.veteroch4k.crm.services;

import com.veteroch4k.crm.models.Seller;
import com.veteroch4k.crm.repositories.SellerRepository;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService {

  private final SellerRepository repository;

  public Page<Seller> getSellers(int page, int size) {

    return repository.findAll(PageRequest.of(page,size));

  }

  public Seller getSellerById(Long id) {


    return  repository.findById(id).orElseThrow();


  }


}
