package com.ecommerce.decorators;

import java.util.Date;
import java.util.List;

import com.ecommerce.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtUser {
    String userId;
    List<Role> role;
    Date issuedTime;
    Date expirationTime;
}
