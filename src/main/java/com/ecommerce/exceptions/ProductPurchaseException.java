package com.ecommerce.exceptions;

public class ProductPurchaseException extends RuntimeException{

    public ProductPurchaseException(String message){
        super(message);
    }
}
