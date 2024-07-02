package com.ecommerce.exceptions;

public class PasswordNotMatchException extends RuntimeException{

    public PasswordNotMatchException() {
        super("Password doesn't match");
    }

    public PasswordNotMatchException(String message) {
        super(message);
    }

}
