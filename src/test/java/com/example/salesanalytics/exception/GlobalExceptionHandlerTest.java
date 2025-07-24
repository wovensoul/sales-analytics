package com.example.salesanalytics.exception;

import com.example.salesanalytics.controller.SaleController;
import com.example.salesanalytics.entity.Sale;
import com.example.salesanalytics.repository.SaleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {SaleController.class, GlobalExceptionHandler.class})
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaleRepository saleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSaleNotFound() throws Exception {
        when(saleRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/sales/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRuntimeExceptionHandling() throws Exception {
        when(saleRepository.findAll()).thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(get("/sales")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Database connection failed"));
    }

    @Test
    public void testValidationExceptionHandling() throws Exception {
        // Create invalid sale data to trigger MethodArgumentNotValidException
        Sale invalidSale = new Sale();
        invalidSale.setSalesperson(""); // Invalid - blank
        invalidSale.setCountry(""); // Invalid - blank
        invalidSale.setProduct(""); // Invalid - blank
        invalidSale.setDate(LocalDate.now());
        invalidSale.setAmount(-10.0); // Invalid - negative amount
        invalidSale.setBoxesShipped(-1); // Invalid - negative boxes

        mockMvc.perform(post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidSale)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.salesperson").value("Salesperson must not be empty"))
                .andExpect(jsonPath("$.country").value("Country must not be empty"))
                .andExpect(jsonPath("$.product").value("Product must not be empty"))
                .andExpect(jsonPath("$.amount").value("Amount must be zero or positive"))
                .andExpect(jsonPath("$.boxesShipped").value("Boxes shipped must be zero or more"));
    }

    @Test
    public void testIllegalArgumentExceptionHandling() throws Exception {
        // Simulate repository throwing IllegalArgumentException
        when(saleRepository.save(org.mockito.ArgumentMatchers.any(Sale.class)))
                .thenThrow(new IllegalArgumentException("Invalid sale data"));

        Sale validSale = new Sale();
        validSale.setSalesperson("John");
        validSale.setCountry("USA");
        validSale.setProduct("Lipstick");
        validSale.setDate(LocalDate.now());
        validSale.setAmount(25.0);
        validSale.setBoxesShipped(2);

        mockMvc.perform(post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validSale)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid sale data"));
    }
}
