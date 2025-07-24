package com.example.salesanalytics.controller;

import com.example.salesanalytics.entity.Sale;
import com.example.salesanalytics.repository.SaleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleRepository saleRepository;

    public SaleController(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @GetMapping
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @PostMapping
    public Sale createSale(@Valid @RequestBody Sale sale) {
        return saleRepository.save(sale);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        return saleRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        if (!saleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        saleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}