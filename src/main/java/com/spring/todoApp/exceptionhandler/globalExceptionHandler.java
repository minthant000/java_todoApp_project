package com.spring.todoApp.exceptionhandler;

import java.util.Map;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.spring.todoApp.exception.ValidationException;
import com.spring.todoApp.response.ErrorResponse;


@RestControllerAdvice
public class globalExceptionHandler{

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex){
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex){
         Map<String,String> errors = new HashMap<String,String>();
         ex.getBindingResult()
        .getFieldErrors()
        .forEach(err -> {errors.put(err.getField(), err.getDefaultMessage());});
        ErrorResponse errorResponse = new ErrorResponse(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);


    }
}
