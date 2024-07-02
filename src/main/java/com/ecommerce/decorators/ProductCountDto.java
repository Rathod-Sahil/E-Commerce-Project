package com.ecommerce.decorators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCountDto {

    long availableProductCount;
    long expiredProductCount;
    long cominSoonProductCount;
    long dailySellProduct;
    long dailyCancelProduct;
}
