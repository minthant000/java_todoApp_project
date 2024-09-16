package com.spring.todoApp.controller;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

// import org.hibernate.validator.constraints.UniqueElements;

// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotEmpty;
// import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequest {
    @NotEmpty(message = "Email cannot be null")
    private String firstName;

    @NotEmpty(message = "Email cannot be null")
    private String lastName;
    
    @NotEmpty(message = "Email cannot be null")
    @Email(message = "Can't access duplicate")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Password cannot be null")
    private String password;

}
