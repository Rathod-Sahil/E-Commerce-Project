package com.ecommerce.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ecommerce.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private String password;
    private String phoneNumber;
    private List<Role> roles;
    private String fullName;
    @JsonIgnore
    private boolean softDelete;
    private List<String> productList;
    private boolean specialUser;
}
