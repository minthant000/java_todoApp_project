package com.spring.todoApp.controller;

import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.todoApp.entity.User;
import com.spring.todoApp.repository.UserRepository;
import com.spring.todoApp.request.RandomCodeRequest;
import com.spring.todoApp.service.EmailSender;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class VerificationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSender emailSender; 

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@Valid @RequestBody RandomCodeRequest request){
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalStateException("User not found"));
    
        if(user.getRandomCode().equalsIgnoreCase(request.getRandomCode())){
            user.setEmailVerifiedAt(new Date());
            userRepository.save(user);
            return ResponseEntity.ok("Email verified successfully" + 
                "\n{\ntFirstName: " + user.getFirstName() +
                ",\ntLastName: " + user.getLastName() +
                ",\n\tEmail: " + user.getEmail() +
                ",\n\tVerified At : " + user.getEmailVerifiedAt() +
                "\n}");
        }else{
            return ResponseEntity.ok("Verification is not successfully " + request.getRandomCode() + request.getEmail());
        }
    }

    @GetMapping("/resendCode")
    public ResponseEntity<String> resendCode(@RequestBody User request){
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalStateException("User not found"));
        String randomCode = getRandomNumberString();
        emailSender.send(request.getEmail(),
        "<html>" +
                            "<body>" +
                            "<h2>Dear, </h2>"
                            + "<br/> Your email is not verified yet.Please verify. <br/>" +
                    "Your Verification code is ."
                    + "<br/> "  + "<u>" + randomCode
        +"</u>" +
                    "<br/> Regards,<br/>" +
                    "MFA Registration team" +
                    "</body>" +
                    "</html> ");
        user.setEmailVerifiedAt(new Date());
        user.setRandomCode(randomCode);
        userRepository.save(user);
            return ResponseEntity.ok("Email verified successfully" + 
                "\n{\ntFirstName: " + user.getFirstName() +
                ",\ntLastName: " + user.getLastName() +
                ",\n\tEmail: " + user.getEmail() +
                ",\n\tVerified At : " + user.getEmailVerifiedAt() +
                "\n}");       
    }
}
