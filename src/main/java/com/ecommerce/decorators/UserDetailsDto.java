package com.ecommerce.decorators;

import java.util.List;

import com.ecommerce.enums.Role;
import com.ecommerce.models.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private List<Role> roles;
    private String fullName;
    private List<Product> productList;
}
