package com.spring.todoApp.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.todoApp.entity.User;
import com.spring.todoApp.repository.UserRepository;
import com.spring.todoApp.request.ForgetPasswordRequest;
import com.spring.todoApp.request.UserDataUpdateRequest;
import com.spring.todoApp.request.UserProfileUpdateRequest;
import com.spring.todoApp.service.EmailSender;
import com.spring.todoApp.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private UserService userService;

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    @GetMapping("/forgetPassword")
    public ResponseEntity<String> resendCode(@RequestBody User request){
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalStateException("User not found"));
        String randomCode = getRandomNumberString();
        emailSender.send(request.getEmail(),
        "<html>" +
                            "<body>" +
                            "<h2>Dear, </h2>"
                            + "<br/> Code for reset password. <br/>" +
                    "Your Reset code is ."
                    + "<br/> "  + "<u>" + randomCode
        +"</u>" +
                    "<br/> Regards,<br/>" +
                    "MFA Registration team" +
                    "</body>" +
                    "</html> ");
        user.setEmailVerifiedAt(new Date());
        user.setRandomCode(randomCode);
        userRepository.save(user);
        return ResponseEntity.ok("Code send successfully");   
    }

    @PutMapping("/renewPassword")
    public ResponseEntity<AuthentictionResponse> reset(@Valid @RequestBody ForgetPasswordRequest request){
        return ResponseEntity.ok(userService.reset(request));
    }

    @PutMapping("/updateUserData")
    public ResponseEntity<String> updateData(@Valid @RequestBody UserDataUpdateRequest request){
        userService.updateRequest(request);
        return ResponseEntity.ok("Update successfully ");
    }

    @PostMapping("/updateUserProfile")
    public ResponseEntity<String> updateProfile( @ModelAttribute UserProfileUpdateRequest request) throws IOException{
        userService.updateProfile(request);
        return ResponseEntity.ok("Profile Update successfully");
    }
}
