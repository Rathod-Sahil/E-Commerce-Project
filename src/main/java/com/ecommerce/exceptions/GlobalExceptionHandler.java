package com.ecommerce.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.decorators.DataResponse;
import com.ecommerce.constants.MessageConstants;
import com.ecommerce.helper.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserValidationException.class)
    DataResponse<Object> userNotValidated(UserValidationException exception) {
        return new DataResponse<>(null, Response.getBadRequestResponse(exception.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistedException.class)
    DataResponse<Object> userAlreadyExisted(UserAlreadyExistedException exception) {
        return new DataResponse<>(null, Response.getConflictResponse(exception.getMessage()));
    }

    @ExceptionHandler(UserNotExistedException.class)
    DataResponse<Object> userNotExisted(UserNotExistedException exception) {
        return new DataResponse<>(null, Response.getNotFoundResponse(exception.getMessage()));
    }

    @ExceptionHandler(AdminDeletionException.class)
    DataResponse<Object> adminOrSuperAdminCanNotBeDeleted(AdminDeletionException exception){
        return new DataResponse<>(null,Response.getBadRequestResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    DataResponse<Object> exception(Exception exception) {
        log.info("Error : {}", exception.getMessage(), exception);
        return new DataResponse<>(null, Response.getInternalServerErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    DataResponse<Object> unauthorizedException(UnauthorizedException exception) {
        return new DataResponse<>(null, Response.getUnauthorizedResponse(exception.getMessage()));
    }

    @ExceptionHandler(TokenExpiredException.class)
    DataResponse<Object> tokenExpiredException(TokenExpiredException exception) {
        return new DataResponse<>(null, Response.getBadRequestResponse(exception.getMessage(), MessageConstants.TOKEN_EXPIRED));
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    DataResponse<Object> passwordNotMatchedException(PasswordNotMatchException exception) {
        return new DataResponse<>(null, Response.getBadRequestResponse(exception.getMessage()));
    }

    @ExceptionHandler(ProductNotExistedException.class)
    DataResponse<Object> productNotExistedException(ProductNotExistedException exception) {
        return new DataResponse<>(null, Response.getNotFoundResponse(exception.getMessage()));
    }

    @ExceptionHandler(ProductNotAvailableException.class)
    DataResponse<Object> productNotAvailableException(ProductNotAvailableException exception) {
        return new DataResponse<>(null, Response.getNotFoundResponse(exception.getMessage()));
    }

    @ExceptionHandler(ProductValidationException.class)
    DataResponse<Object> productValidationException(ProductValidationException exception) {
        return new DataResponse<>(null, Response.getBadRequestResponse(exception.getMessage()));
    }

    @ExceptionHandler(ProductPurchaseException.class)
    DataResponse<Object> productPurchaseException(ProductPurchaseException exception){
        return new DataResponse<>(null, Response.getBadRequestResponse(exception.getMessage()));
    }

    @ExceptionHandler(ProductExistedException.class)
    DataResponse<Object> productExistedException(ProductExistedException exception){
        return new DataResponse<>(null, Response.getConflictResponse(exception.getMessage()));
    }
}
