package com.ecommerce.repositories;

import java.util.Date;

import org.springframework.data.domain.Page;

import com.ecommerce.decorators.ProductPaginationApi;
import com.ecommerce.models.Product;

public interface ProductRepositoryCustom {

    Page<Product> filterSearchAndPagination(ProductPaginationApi productPaginationApi);
    long getAvailableProduct(Date date);
    long getExpiredProduct(Date date);
    long getComingSoonProduct(Date date);

}
