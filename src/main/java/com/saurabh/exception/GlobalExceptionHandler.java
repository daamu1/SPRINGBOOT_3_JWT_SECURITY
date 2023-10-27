package com.saurabh.exception;


import com.saurabh.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import  org.springframework.security.core.AuthenticationException;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handle(UserAlreadyExistsException exception) {
        return new ApiResponse<Object>(false, exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handle(UserNotFoundException exception) {
        return new ApiResponse<Object>(false, exception.getMessage());
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Object> handle(BadCredentialsException exception) {
        return new ApiResponse<Object>(false, exception.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Object> handle(TokenException exception) {
        return new ApiResponse<Object>(false, exception.getMessage());
    }
}
