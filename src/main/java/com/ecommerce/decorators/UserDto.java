package com.ecommerce.decorators;

import java.util.List;


import com.ecommerce.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private List<Role> roles;
}
