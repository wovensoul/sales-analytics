package com.example.salesanalytics.service;

import com.example.salesanalytics.entity.Sale;
import com.example.salesanalytics.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final SaleRepository saleRepository;

    public AnalyticsServiceImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public Map<String, Double> getTotalRevenueByCountry() {
        List<Sale> sales = saleRepository.findAll();
        return sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getCountry,
                        Collectors.summingDouble(Sale::getAmount)
                ));
    }

    @Override
    public Map<String, Map<String, Integer>> getTotalUnitsSoldByProductAndCountry() {
        List<Sale> sales = saleRepository.findAll();
        return sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getCountry,
                        Collectors.groupingBy(
                                Sale::getProduct,
                                Collectors.summingInt(Sale::getBoxesShipped)
                        )
                ));
    }

    @Override
    public Map<String, Double> getAverageOrderValueByProduct() {
        List<Sale> sales = saleRepository.findAll();
        return sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getProduct,
                        Collectors.averagingDouble(Sale::getAmount)
                ));
    }

    @Override
    public Map<String, Long> getSalesCountByProduct() {
        List<Sale> sales = saleRepository.findAll();
        return sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getProduct,
                        Collectors.counting()
                ));
    }

    @Override
    public Map<String, String> getTopSellingProductByCountry() {
        List<Sale> sales = saleRepository.findAll();
        Map<String, Map<String, Double>> revenueByCountryAndProduct = sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getCountry,
                        Collectors.groupingBy(
                                Sale::getProduct,
                                Collectors.summingDouble(Sale::getAmount)
                        )
                ));

        Map<String, String> topProductByCountry = new HashMap<>();
        for (var entry : revenueByCountryAndProduct.entrySet()) {
            String country = entry.getKey();
            Map<String, Double> productRevenueMap = entry.getValue();

            String topProduct = productRevenueMap.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("N/A");

            topProductByCountry.put(country, topProduct);
        }

        return topProductByCountry;
    }
}
