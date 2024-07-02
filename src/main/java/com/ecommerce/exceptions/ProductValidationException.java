package com.ecommerce.exceptions;

public class ProductValidationException extends RuntimeException{

    public ProductValidationException(String message){
        super(message);
    }

}
