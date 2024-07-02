package com.ecommerce.decorators;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ProductDto {

    private String name;
    private String color;
    private Double price;
    private Boolean stock;
    private Date establishedDate;
    private Date endDate;
    private Double discountRate;
}
