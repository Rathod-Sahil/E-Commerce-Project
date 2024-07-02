package com.ecommerce.exceptions;

public class UserAlreadyExistedException extends RuntimeException{

    public UserAlreadyExistedException(String message){
        super(message);
    }

    public UserAlreadyExistedException(){
        super("User with given email is already existed");
    }

}
