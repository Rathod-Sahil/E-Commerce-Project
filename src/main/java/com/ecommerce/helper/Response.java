package com.ecommerce.helper;

import com.ecommerce.constants.MessageConstants;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    String description;
    HttpStatus status;
    String statusCode;

	public static Response getOkResponse(){
        return new Response("Ok",HttpStatus.OK, MessageConstants.SUCCESS);
    }
    public static Response getOkResponse(String description){
        return new Response(description,HttpStatus.OK,MessageConstants.SUCCESS);
    }
    public static Response getBadRequestResponse(String description){
        return new Response(description,HttpStatus.BAD_REQUEST,MessageConstants.INVALID);
    }
    public static Response getBadRequestResponse(String description,String statusCode){
        return new Response(description,HttpStatus.BAD_REQUEST,statusCode);
    }
    public static Response getConflictResponse(String description){
        return new Response(description,HttpStatus.CONFLICT,MessageConstants.ALREADY_EXISTED);
    }
    public static Response getNotFoundResponse(String description){
        return new Response(description,HttpStatus.NOT_FOUND,MessageConstants.NOT_FOUND);
    }
    public static Response getInternalServerErrorResponse(String description){
        return new Response(description,HttpStatus.INTERNAL_SERVER_ERROR,MessageConstants.INTERNAL_SERVER_ERROR);
    }
    public static Response getUnauthorizedResponse(String description){
        return new Response(description,HttpStatus.UNAUTHORIZED,MessageConstants.UNAUTHORIZED);
    }
}
