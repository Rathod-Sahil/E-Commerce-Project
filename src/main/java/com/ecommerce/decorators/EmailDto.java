package com.ecommerce.decorators;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {

    String productName;
    String status;
    String userName;
    String adminName;
    String superAdminName;
    double price;
    double discount;
    double finalPrice;
    long availableProducts;
    long expiredProducts;
    long comingSoonProducts;
    long selledProducts;
    long canceledProducts;

}
