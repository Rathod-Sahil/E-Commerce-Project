package com.ecommerce.decorators;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterDto {

    String search;
    double minPrice;
    double maxPrice;
    double minDiscountRate;
    double maxDiscountRate;
    Date date;
    Boolean stock;
    Boolean topSelling;
}
