package com.spring.todoApp.controller;

import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.todoApp.entity.User;
import com.spring.todoApp.exception.ValidationException;
import com.spring.todoApp.repository.UserRepository;
import com.spring.todoApp.service.EmailSender;
import com.spring.todoApp.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final EmailSender emailSender;

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    public AuthentictionResponse register(User request) {
  
        Map<String,String> errors = new HashMap<String,String>();
        Optional<User> isUserExist = userRepository.findByEmail(request.getEmail());
      
        if(!isUserExist.isEmpty()){
           errors.put("email", "User already exist!");
           throw new ValidationException(errors);
        }

            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();
            userRepository.save(user);
            String jwtToken = jwtService.generateToken(user);
            // String emailContent = "Thank you for registering. Please click the following link to verify your account: ";

            emailSender.send(request.getEmail(), "<html>" +
            "<body>" +
            "<h2>Dear "+ request.getFirstName() + " " + request.getLastName() + ",</h2>"
            + "<br/> We're excited to have you get started. " +
            "Please click on below link to confirm your account."
            + "<br/> "  + getRandomNumberString()+"" +
            "<br/> Regards,<br/>" +
            "MFA Registration team" +
            "</body>" +
            "</html>");
            return AuthentictionResponse.builder()
                    .token(jwtToken).build();
      
    }

    public AuthentictionResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthentictionResponse.builder().token(jwtToken).build();
    }

    
}
