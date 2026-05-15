package com.veteroch4k.crm.controllers;

import com.veteroch4k.crm.models.Seller;
import com.veteroch4k.crm.services.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sellers")
public class SellerController {

  private final SellerService service;

  @GetMapping("")
  public ResponseEntity<Page<Seller>>  getSellers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    return ResponseEntity.ok(service.getSellers(page,size));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Seller> getSeller(@PathVariable("id") Long id) {

    return ResponseEntity.ok(service.getSellerById(id));

  }


}
