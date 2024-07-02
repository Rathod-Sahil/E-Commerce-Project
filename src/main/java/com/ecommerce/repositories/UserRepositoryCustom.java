package com.ecommerce.repositories;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecommerce.decorators.UserPaginationApi;
import com.ecommerce.models.User;

public interface UserRepositoryCustom {

    List<User> filterByRole(List<String> roles);
    Page<User> filterSearchAndPagination(UserPaginationApi userPaginationApi);
}
