package com.example.salesanalytics.service;

import com.example.salesanalytics.entity.Sale;
import com.example.salesanalytics.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceImplTest {

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    private List<Sale> mockSales;

    @BeforeEach
    public void setUp() {
        mockSales = List.of(
                new Sale("John", "USA", "Lipstick", LocalDate.of(2024, 1, 1), 100.0, 10),
                new Sale("Jane", "USA", "Lipstick", LocalDate.of(2024, 1, 2), 150.0, 15),
                new Sale("Bob", "USA", "Foundation", LocalDate.of(2024, 1, 3), 200.0, 20),
                new Sale("Alice", "Canada", "Lipstick", LocalDate.of(2024, 1, 4), 300.0, 30),
                new Sale("Charlie", "Canada", "Blush", LocalDate.of(2024, 1, 5), 400.0, 40)
        );
        when(saleRepository.findAll()).thenReturn(mockSales);
    }

    @Test
    public void testGetTotalRevenueByCountry() {
        Map<String, Double> expected = Map.of(
                "USA", 450.0,
                "Canada", 700.0
        );
        assertEquals(expected, analyticsService.getTotalRevenueByCountry());
    }

    @Test
    public void testGetTotalUnitsSoldByProductAndCountry() {
        Map<String, Map<String, Integer>> expected = new HashMap<>();

        expected.put("USA", Map.of(
                "Lipstick", 25,
                "Foundation", 20
        ));

        expected.put("Canada", Map.of(
                "Lipstick", 30,
                "Blush", 40
        ));

        assertEquals(expected, analyticsService.getTotalUnitsSoldByProductAndCountry());
    }

    @Test
    public void testGetAverageOrderValueByProduct() {
        Map<String, Double> actual = analyticsService.getAverageOrderValueByProduct();
        
        // Lipstick: (100 + 150 + 300) / 3 = 183.33...
        assertEquals(183.33, actual.get("Lipstick"), 0.01);
        // Foundation: 200 / 1 = 200.0
        assertEquals(200.0, actual.get("Foundation"), 0.01);
        // Blush: 400 / 1 = 400.0
        assertEquals(400.0, actual.get("Blush"), 0.01);
    }

    @Test
    public void testGetSalesCountByProduct() {
        Map<String, Long> expected = Map.of(
                "Lipstick", 3L,  // 3 sales (2 in USA, 1 in Canada)
                "Foundation", 1L, // 1 sale in USA
                "Blush", 1L      // 1 sale in Canada
        );
        assertEquals(expected, analyticsService.getSalesCountByProduct());
    }

    @Test
    public void testGetTopSellingProductByCountry() {
        Map<String, String> expected = Map.of(
                "USA", "Lipstick",  // USA: Lipstick=250 (100+150), Foundation=200 -> Lipstick wins
                "Canada", "Blush"   // Canada: Lipstick=300, Blush=400 -> Blush wins
        );
        assertEquals(expected, analyticsService.getTopSellingProductByCountry());
    }
}
