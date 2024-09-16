package com.spring.todoApp.controller;

import org.springframework.http.HttpStatus;
// import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.todoApp.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor

public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthentictionResponse> register(@RequestBody @Valid User request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    // @ExceptionHandler
    // public ResponseEntity<String> handleResourceNotFoundException(Exception e) {
    //     return new ResponseEntity<>("Email can't be duplicate: " + e.getMessage(), HttpStatus.CONFLICT);
    // }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthentictionResponse> authenticate(@RequestBody @Valid AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }
}
