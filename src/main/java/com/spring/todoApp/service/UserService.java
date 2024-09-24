package com.spring.todoApp.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.todoApp.controller.AuthentictionResponse;
import com.spring.todoApp.repository.TokenRepository;
import com.spring.todoApp.repository.UserRepository;
import com.spring.todoApp.request.ForgetPasswordRequest;
import com.spring.todoApp.request.UserDataUpdateRequest;
import com.spring.todoApp.request.UserProfileUpdateRequest;

import lombok.RequiredArgsConstructor;

import com.spring.todoApp.entity.Token;
import com.spring.todoApp.entity.User;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokenRepository;


    @Autowired
    private FileUploadService fileUploadService;
    
    private void saveToken(User user, String jwtToken) {
        Token token = Token.builder()
                .token(jwtToken)
                .is_logged_out(false)
                .userId(user)
                .build();
        tokenRepository.save(token);
    }

    public AuthentictionResponse reset(ForgetPasswordRequest request){

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalStateException("User not found"));

        if(!user.getRandomCode().equalsIgnoreCase(request.getRandomCode())){
            throw new IllegalArgumentException("invali code");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Password doesn't match");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        saveToken(user, jwtToken);

        emailSender.send(request.getEmail(), "<html>" +
                        "<body>" +
                        "Your password reset successfully. " +
                        "</body>" +
                        "</html>");

        return AuthentictionResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    // update user data
    public User updateRequest(UserDataUpdateRequest request){
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalStateException("User not found"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return userRepository.save(user);
    }

    // update profile
    public User updateProfile(UserProfileUpdateRequest request) throws IOException{
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalStateException("User not found"));
        String filePath = fileUploadService.uploadImage(request.getProfilePath(), "user");
        user.setProfilePath(filePath);
        return userRepository.save(user);
    }
}
