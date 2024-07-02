package com.ecommerce.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.ecommerce.exceptions.ProductValidationException;
import com.ecommerce.exceptions.UserValidationException;

@Component
public class ValidationUtils {

    //Validation for null fields
    public void nullFieldValidation(String email,String password,String phoneNo){
        if (email == null || password == null || phoneNo == null) {
            throw new UserValidationException("One or more fields are empty");
        }
    }

    //Password validation
    public void passwordValidation(String password){
        if(!Pattern.matches("^.{8,15}$", password)){
            throw new UserValidationException("Invalid Password, Length must be between 8 characters and 15 characters");
        }else if (!Pattern.matches(".*[a-z].*", password)) {
            throw new UserValidationException("Invalid Password, Must contain at least 1 lowercase letter");
        }else if(!Pattern.matches(".*[A-Z].*", password)){
            throw new UserValidationException("Invalid Password, Must contain at least 1 uppercase letter");
        }else if (!Pattern.matches(".*[0-9].*", password)) {
            throw new UserValidationException("Invalid Password, Must contain at least 1 digit");
        }else if (!Pattern.matches(".*\\W.*", password)) {
            throw new UserValidationException("Invalid Password, Must contain at least 1 special character");
        }
   }

   //Email validation
    public void emailValidate(String email) {
        if (!Pattern.matches("[a-z0-9_\\-.]+@[a-z_\\-]+[.][a-z]{2,3}[.]?[a-z]{0,3}", email)) {
            throw new UserValidationException("Provide Valid Email Address");
        }
    }

    //PhoneNo validation
    public void phoneNumberValidate(String phoneNO) {
        if (!Pattern.matches("[0-9]{10}", phoneNO)) {
            throw new UserValidationException("Provide 10 digit Phone Number");
        }
    }

    //product price validation
    public void productPriceValidation(double price){
        if(price<=0){
            throw new ProductValidationException("Product price must be greater than zero");
        }
    }

    //product established date and end date validation
    public void productDateValidation(Date establishedDate, Date endDate){
        if(establishedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(LocalDateTime.now().toLocalDate())){
            throw new ProductValidationException("Product established date must not be past date");
        }else if(endDate.before(establishedDate)){
            throw new ProductValidationException("Product end date must come after established date");
        }
    }

    //product discount rate validation
    public void productDiscountRateValidation(double discountRate){
        if(discountRate<0){
            throw new ProductValidationException("Product discount rate should be greater than or equal to 0");
        }
    }

}
