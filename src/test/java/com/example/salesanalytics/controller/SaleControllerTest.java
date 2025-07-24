package com.example.salesanalytics.controller;

import com.example.salesanalytics.controller.SaleController;
import com.example.salesanalytics.entity.Sale;
import com.example.salesanalytics.repository.SaleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SaleController.class)
public class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaleRepository saleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllSales() throws Exception {
        Sale sampleSale = new Sale();
        sampleSale.setId(1L);
        sampleSale.setSalesperson("Alice");
        sampleSale.setCountry("USA");
        sampleSale.setProduct("Lipstick");
        sampleSale.setDate(LocalDate.of(2025, 7, 24));
        sampleSale.setAmount(29.99);
        sampleSale.setBoxesShipped(3);

        when(saleRepository.findAll()).thenReturn(List.of(sampleSale));

        mockMvc.perform(get("/sales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].salesperson").value("Alice"));
    }

    @Test
    public void testCreateSale() throws Exception {
        Sale newSale = new Sale();
        newSale.setSalesperson("Bob");
        newSale.setCountry("UK");
        newSale.setProduct("Foundation");
        newSale.setDate(LocalDate.of(2025, 7, 24));
        newSale.setAmount(49.99);
        newSale.setBoxesShipped(2);

        Sale savedSale = new Sale();
        savedSale.setId(2L);
        savedSale.setSalesperson(newSale.getSalesperson());
        savedSale.setCountry(newSale.getCountry());
        savedSale.setProduct(newSale.getProduct());
        savedSale.setDate(newSale.getDate());
        savedSale.setAmount(newSale.getAmount());
        savedSale.setBoxesShipped(newSale.getBoxesShipped());

        when(saleRepository.save(any(Sale.class))).thenReturn(savedSale);

        mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSale)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.salesperson").value("Bob"));
    }

    @Test
    public void testDeleteSale() throws Exception {
        Long saleId = 3L;

        when(saleRepository.existsById(saleId)).thenReturn(true);

        mockMvc.perform(delete("/sales/{id}", saleId))
                .andExpect(status().isNoContent());

        Mockito.verify(saleRepository).existsById(saleId);
        Mockito.verify(saleRepository).deleteById(saleId);
    }

    @Test
    public void testDeleteSaleNotFound() throws Exception {
        Long saleId = 999L;

        when(saleRepository.existsById(saleId)).thenReturn(false);

        mockMvc.perform(delete("/sales/{id}", saleId))
                .andExpect(status().isNotFound());

        Mockito.verify(saleRepository).existsById(saleId);
        Mockito.verify(saleRepository, Mockito.never()).deleteById(saleId);
    }
}
