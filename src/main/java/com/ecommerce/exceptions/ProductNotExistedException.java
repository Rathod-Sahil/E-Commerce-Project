package com.ecommerce.exceptions;

public class ProductNotExistedException extends RuntimeException{

    public ProductNotExistedException(String message){
        super(message);
    }
}
