package com.example.salesanalytics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Salesperson must not be empty")
    private String salesperson;
    @NotBlank(message = "Country must not be empty")
    private String country;
    @NotBlank(message = "Product must not be empty")
    private String product;
    @NotNull(message = "Date must not be null")
    private LocalDate date;
    @NotNull(message = "Amount must not be null")
    @PositiveOrZero(message = "Amount must be zero or positive")
    private Double amount;
    @NotNull(message = "Boxes shipped must not be null")
    @Min(value = 0, message = "Boxes shipped must be zero or more")
    @Column(name = "boxesShipped")
    private Integer boxesShipped;

    public Sale() {}

    public Sale(String salesperson, String country, String product, LocalDate date, Double amount, Integer boxesShipped) {
        this.salesperson = salesperson;
        this.country = country;
        this.product = product;
        this.date = date;
        this.amount = amount;
        this.boxesShipped = boxesShipped;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSalesperson() {
        return salesperson;
    }

    public void setSalesperson(String salesperson) {
        this.salesperson = salesperson;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getBoxesShipped() {
        return boxesShipped;
    }

    public void setBoxesShipped(Integer boxesShipped) {
        this.boxesShipped = boxesShipped;
    }
}