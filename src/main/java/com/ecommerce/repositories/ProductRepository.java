package com.ecommerce.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.models.Product;


@Repository
public interface ProductRepository extends MongoRepository<Product ,String>, ProductRepositoryCustom {

    Optional<Product> findByIdAndSoftDeleteFalse(String productId);
    Optional<Product> findByNameAndColorAndSoftDeleteFalse(String productName,String color);
    List<Product> findAllByIdAndSoftDeleteFalse(List<String> productIdList);
}
