package com.ecommerce.exceptions;

public class ProductNotAvailableException extends RuntimeException{

    public ProductNotAvailableException(String message){
        super(message);
    }
}
