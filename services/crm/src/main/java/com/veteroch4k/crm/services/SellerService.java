package com.veteroch4k.crm.services;

import com.veteroch4k.crm.exceptions.ResourceNotFoundException;
import com.veteroch4k.crm.models.DTO.SellerDTO.SellerRequestDTO;
import com.veteroch4k.crm.models.DTO.SellerDTO.SellerResponseDTO;
import com.veteroch4k.crm.models.Seller;
import com.veteroch4k.crm.repositories.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerService {

  private final SellerRepository repository;

  public Page<SellerResponseDTO> getSellers(int page, int size) {

    Page<Seller> sellers = repository.findAll(PageRequest.of(page,size, Sort.by("id").ascending()));


    return sellers.map(SellerResponseDTO::new);

  }

  public SellerResponseDTO getSellerById(Long id) {

    Seller seller = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Продавец с ID: " + id + " не найден"));

    return  new SellerResponseDTO(seller);

  }


  public SellerResponseDTO createSeller(SellerRequestDTO sellerRequestDTO) {

    Seller seller = new Seller();
    seller.setName(sellerRequestDTO.name());
    seller.setContactInfo(sellerRequestDTO.contactInfo());

    return new SellerResponseDTO(repository.save(seller));

  }

  @Transactional
  public void updateSeller(Long id, SellerRequestDTO sellerRequestDTO) {

    Seller seller = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Продавец с ID: " + id + " не существует")
    );
    seller.setName(sellerRequestDTO.name());
    seller.setContactInfo(sellerRequestDTO.contactInfo());

    repository.save(seller);

  }

  @Transactional
  public void deleteSeller(Long id) {

    Seller seller = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Продавец с ID: " + id + " не существует"));

    seller.setDeleted(true);

    repository.save(seller);


  }
}
