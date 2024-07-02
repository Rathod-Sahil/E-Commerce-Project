package com.ecommerce.exceptions;

public class UserNotExistedException extends RuntimeException{

    public UserNotExistedException(String message){
        super(message);
    }

    public UserNotExistedException(){
        super("User with given id is not existed");
    }


}
