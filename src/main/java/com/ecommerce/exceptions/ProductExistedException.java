package com.ecommerce.exceptions;

public class ProductExistedException extends RuntimeException {

    public ProductExistedException(String message){
        super(message);
    }
}
