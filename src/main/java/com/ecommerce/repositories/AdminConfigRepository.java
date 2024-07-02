package com.ecommerce.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.models.AdminConfig;

@Repository
public interface AdminConfigRepository extends MongoRepository<AdminConfig,String> {

}
