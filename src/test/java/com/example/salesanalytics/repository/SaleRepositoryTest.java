package com.example.salesanalytics.repository;

import com.example.salesanalytics.entity.Sale;
import com.example.salesanalytics.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SaleRepositoryTest {

    @Autowired
    private SaleRepository saleRepository;

    @Test
    public void testSaveAndFindSale() {
        Sale sale = new Sale();
        sale.setSalesperson("Alice");
        sale.setCountry("USA");
        sale.setProduct("Lipstick");
        sale.setDate(LocalDate.of(2025, 7, 24));
        sale.setAmount(29.99);
        sale.setBoxesShipped(3);

        saleRepository.save(sale);

        List<Sale> sales = saleRepository.findAll();

        assertThat(sales).isNotEmpty();
        assertThat(sales.get(0).getSalesperson()).isEqualTo("Alice");
    }
}