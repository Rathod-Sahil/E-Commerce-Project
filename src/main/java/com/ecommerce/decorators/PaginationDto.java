package com.ecommerce.decorators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDto {

    int pageNo;
    int pageSize;
}
