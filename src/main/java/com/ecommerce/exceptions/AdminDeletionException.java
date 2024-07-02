package com.ecommerce.exceptions;


public class AdminDeletionException extends RuntimeException {

    public AdminDeletionException(String message){
        super(message);
    }

    public AdminDeletionException(){
        super("Admin or super admin can not be deleted");
    }
}
