package com.ecommerce.decorators;

import com.ecommerce.helper.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponse<T> {

    private T data;
    private String token;
    private Response response;
}
