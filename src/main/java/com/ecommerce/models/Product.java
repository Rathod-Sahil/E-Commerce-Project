package com.ecommerce.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("product")
public class Product {

    @Id
    private String id;
    private String name;
    private String color;
    private Double price;
    private Boolean topSelling;
    private Boolean stock;
    private Date establishedDate;
    private Date endDate;
    private Double discountRate;
    @JsonIgnore
    private boolean softDelete;
    @JsonIgnore
    private double productSellCount;
    
}
