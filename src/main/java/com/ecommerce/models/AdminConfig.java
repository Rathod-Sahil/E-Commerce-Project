package com.ecommerce.models;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ecommerce.decorators.ProductCancellationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("admin_config")
public class AdminConfig {

    @Id
    String id;
    List<String> adminEmails;
    List<String> superAdminEmails;
    double topSellingNumber;
    Map<LocalDate , Map<String,List<String>>> dailySellProduct;
    Map<LocalDate , List<String>> dailyCancelProduct;
    List<ProductCancellationDto> cancelledProducts;
    
}
