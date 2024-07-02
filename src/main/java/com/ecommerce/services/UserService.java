package com.ecommerce.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecommerce.decorators.LoginDto;
import com.ecommerce.decorators.UserDto;
import com.ecommerce.decorators.UserPaginationApi;
import com.ecommerce.models.User;


public interface UserService {

    User creatUser(UserDto user);
    User getUser(String id);
    void deleteUser(String id);
    User updateUser(String id,UserDto user);
    List<User> getAllUsers();
    User login(LoginDto loginDTO);
    public Page<User> filterSearchAndPagination(UserPaginationApi userPaginationApi);


}
