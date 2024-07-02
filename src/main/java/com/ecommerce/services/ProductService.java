package com.ecommerce.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecommerce.decorators.ProductDto;
import com.ecommerce.decorators.ProductCountDto;
import com.ecommerce.decorators.ProductPaginationApi;
import com.ecommerce.decorators.UserDetailsDto;
import com.ecommerce.models.Product;

public interface ProductService {

    Product addProduct(ProductDto productDto);
    void deleteProduct(String productId, boolean deleteFromUserProfile);
    Product updateProduct(String productId, ProductDto productDto);
    Product buyProduct(String productName, String color);
    Page<Product> filterSearchAndPagination(ProductPaginationApi productPaginationApi);
    ProductCountDto productDetails();
    void cancelProduct(String productId);
    List<UserDetailsDto> userDetails();

}
