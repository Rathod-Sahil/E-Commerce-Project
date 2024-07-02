package com.ecommerce.decorators;

import com.ecommerce.helper.Response;
import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {

    Page<T> page;
    Response response;
}
