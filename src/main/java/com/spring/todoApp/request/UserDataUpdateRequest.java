package com.spring.todoApp.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDataUpdateRequest {
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    @NotBlank(message = "Email cannot be null")
    private String email;
    
    @NotEmpty(message = "firstName cannot be null")
    private String firstName;

    @NotEmpty(message = "firstName cannot be null")
    private String lastName;
}
