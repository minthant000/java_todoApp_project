package com.spring.todoApp.service;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.todoApp.entity.Token;
import com.spring.todoApp.entity.User;
import com.spring.todoApp.exception.ValidationException;
import com.spring.todoApp.repository.TokenRepository;
import com.spring.todoApp.repository.UserRepository;
import com.spring.todoApp.request.AuthenticationRequest;
import com.spring.todoApp.response.AuthentictionResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final EmailSender emailSender;

    private final TokenRepository tokenRepository;

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

        String randomCode = getRandomNumberString();

            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .randomCode(randomCode)
                    .build();
            userRepository.save(user);
            String jwtToken = jwtService.generateToken(user);

            saveToken(user, jwtToken);

            emailSender.send(request.getEmail(), "<html>" +
            "<body>" +
            "<h2>Dear "+ request.getFirstName() + " " + request.getLastName() + ",</h2>"
            + "<br/> We're excited to have you get started. " +
            "Please click on below link to confirm your account."
            + "<br/> "  + randomCode + "" +
            "<br/> Regards,<br/>" +
            "MFA Registration team" +
            "</body>" +
            "</html>");
            return AuthentictionResponse.builder()
                    .token(jwtToken).build();
      
    }

    private void revokeAllokenByUser(User user) {
        List<Token> validTokenListByUser = tokenRepository.findAllTokenByUser(user.getId());

        if(!validTokenListByUser.isEmpty()){
            validTokenListByUser.forEach(t->{
                t.set_logged_out(true);
            });
        }
        tokenRepository.saveAll(validTokenListByUser);
    }

    public AuthentictionResponse authenticate(AuthenticationRequest request) {
       try{
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        revokeAllokenByUser(user);
            
        // save generated token
        saveToken(user, jwtToken);

       return AuthentictionResponse.builder().token(jwtToken).build();
       }catch(Exception $err){
        System.out.println($err.getMessage());
        return null;
       }

    }

    private void saveToken(User user, String jwtToken) {
        Token token = Token.builder()
                .token(jwtToken)
                .is_logged_out(false)
                .userId(user)
                .build();
        tokenRepository.save(token);
    }

    
}
