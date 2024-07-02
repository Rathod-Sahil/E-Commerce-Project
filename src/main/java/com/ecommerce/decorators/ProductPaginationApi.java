package com.ecommerce.decorators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPaginationApi {

    PaginationDto pagination;
    ProductFilterDto productFilter;
}
