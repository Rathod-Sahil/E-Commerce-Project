package com.ecommerce.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.models.User;

@Repository
public interface UserRepository extends MongoRepository<User,String>, UserRepositoryCustom {

    Optional<User> findByIdAndSoftDeleteFalse(String id);
    Optional<User> findByEmailAndSoftDeleteFalse(String email);
    Optional<List<User>> findBySoftDeleteFalse();
}
