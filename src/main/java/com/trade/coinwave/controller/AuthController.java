package com.trade.coinwave.controller;

import com.trade.coinwave.config.JwtProvider;
import com.trade.coinwave.model.User;
import com.trade.coinwave.repository.UserRepository;
import com.trade.coinwave.response.AuthResponse;
import com.trade.coinwave.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody User user) {

        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        User newUser = new User();
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());

        User savedUser = userRepository.save(newUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );

        String jwtToken = JwtProvider.generateToken(auth);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtToken);
        authResponse.setStatus(true);
        authResponse.setMessage("Successfully registered user");

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) {

        String userEmail = user.getEmail();
        String password = user.getPassword();

        Authentication auth = Authenticate(userEmail, password );

        String jwtToken = JwtProvider.generateToken(auth);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtToken);
        authResponse.setStatus(true);
        authResponse.setMessage("Successfully logged in user");

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    private Authentication Authenticate(String userEmail, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }
        if (!password.equals(userDetails.getPassword())) {
            throw new BadCredentialsException("invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

}
