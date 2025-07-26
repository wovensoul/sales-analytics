package com.example.salesanalytics.service;

import java.util.Map;

public interface AnalyticsService {
    Map<String, Double> getTotalRevenueByCountry(); // shows which countries bring in the most revenue
    Map<String, Map<String, Integer>> getTotalUnitsSoldByProductAndCountry(); // identifies which products sell the most in each country
    Map<String, Double> getAverageOrderValueByProduct(); // helps find which products have higher purchase values on average
    Map<String, Long> getSalesCountByProduct(); // measures product popularity by sales frequency
    Map<String, String> getTopSellingProductByCountry();  // identify the product with the highest total revenue or volume by country
}
