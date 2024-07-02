package com.ecommerce.decorators;

import com.ecommerce.helper.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataResponse<T> {

    private T data;
    private Response response;
}
