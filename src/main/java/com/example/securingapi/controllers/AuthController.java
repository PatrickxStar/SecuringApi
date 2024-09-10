package com.example.securingapi.controllers;

import com.example.securingapi.config.JwtTokenUtil;
import com.example.securingapi.dto.RegistrationDto;
import com.example.securingapi.dto.RegistrationResponse;
import com.example.securingapi.entities.User;
import com.example.securingapi.models.Roles;
import com.example.securingapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationDto registrationDto) {
        // Create a new user
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(registrationDto.getPassword());
        user.setRole(Roles.valueOf(registrationDto.getRole().toUpperCase()));
        userService.saveUser(user);

        // Generate a JWT token after successful registration
        final UserDetails userDetails = userService.loadUserByUsername(registrationDto.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return response with message and token
        RegistrationResponse response = new RegistrationResponse("User registered successfully.", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<RegistrationResponse> login(@RequestBody RegistrationDto credentialsDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentialsDto.getUsername(), credentialsDto.getPassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new RegistrationResponse("Invalid credentials", null));
        }

        final UserDetails userDetails = userService.loadUserByUsername(credentialsDto.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new RegistrationResponse("Login successful", token));
    }
}
