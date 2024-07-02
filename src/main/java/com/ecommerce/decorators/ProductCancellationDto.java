package com.ecommerce.decorators;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCancellationDto {

    private String productId;
    private String cancelledBy;
    private LocalDateTime cancelledTime;
}
