package com.ecommerce.controllers;

import com.ecommerce.annotations.Access;
import com.ecommerce.constants.ResponseConstants;
import com.ecommerce.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.decorators.DataResponse;
import com.ecommerce.decorators.JwtResponse;
import com.ecommerce.decorators.LoginDto;
import com.ecommerce.decorators.PageResponse;
import com.ecommerce.helper.Response;
import com.ecommerce.decorators.UserDto;
import com.ecommerce.decorators.UserPaginationApi;
import com.ecommerce.models.User;
import com.ecommerce.utils.JwtUtils;
import com.ecommerce.services.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Tag(name = "User-Api")
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;
    private final JwtUtils jwtUtils;


    @Access(role = Role.ANONYMOUS)
    @PostMapping(value = "/register" , name = "registerUser")
    public DataResponse<User> registerUser(@RequestBody UserDto userDto) {
        return new DataResponse<>(userService.creatUser(userDto), Response.getOkResponse(ResponseConstants.REGISTER_USER));
    }

    @Access(role = {Role.ADMIN, Role.SUPER_ADMIN})
    @GetMapping(value = "/{id}" , name = "getSingleUser")
    public DataResponse<User> getUser(@PathVariable String id) {
        return new DataResponse<>(userService.getUser(id), Response.getOkResponse(ResponseConstants.GET_USER));
    }

    @Access(role = {Role.ADMIN, Role.SUPER_ADMIN})
    @GetMapping(name = "findAllUsers")
    public DataResponse<List<User>> getAllUsers(){
        return new DataResponse<>(userService.getAllUsers(),Response.getOkResponse(ResponseConstants.GET_ALL_USERS));
    }

    @Access(role ={Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @DeleteMapping(value = "/{id}",name = "deleteUser")
    public DataResponse<Object> deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return new DataResponse<>(null, Response.getOkResponse(ResponseConstants.DELETE_USER));
    }

    @Access(role ={Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @PutMapping(value = "/{id}",name = "updateUser")
    public DataResponse<User> updateUser(@PathVariable String id,@RequestBody UserDto userDto){
        return new DataResponse<>(userService.updateUser(id,userDto),Response.getOkResponse(ResponseConstants.UPDATE_USER));
    }

    @Access(role = {Role.ADMIN, Role.SUPER_ADMIN})
    @PostMapping(value = "/all",name = "filterSearchAndPagination")
    public PageResponse<User> filterSearchAndPagination(@RequestBody UserPaginationApi userPaginationApi){
        return new PageResponse<>(userService.filterSearchAndPagination(userPaginationApi),Response.getOkResponse(ResponseConstants.FIND_USERS));
    }

    @Access(role = Role.ANONYMOUS)
    @PostMapping(value = "/login" ,name = "login")
    public JwtResponse<User> login(@RequestBody LoginDto loginDTO){
        User user = userService.login(loginDTO);
        String token = jwtUtils.generateToken(user);
        return new JwtResponse<>(user,token,Response.getOkResponse(ResponseConstants.LOGIN));
    }
}
