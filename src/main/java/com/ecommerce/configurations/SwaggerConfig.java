package com.ecommerce.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    public static final String SCHEME_NAME = "BearerScheme";
    public static final String SCHEME = "Bearer";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME,new SecurityScheme()
                                .name(SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .bearerFormat("JWT")
                                .scheme(SCHEME))
                )
                .addSecurityItem(new SecurityRequirement()
                        .addList(SCHEME_NAME));
    }
}
