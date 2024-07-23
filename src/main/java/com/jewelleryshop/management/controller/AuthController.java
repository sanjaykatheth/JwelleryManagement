package com.jewelleryshop.management.controller;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jewelleryshop.management.constants.ResponseMessage;
import com.jewelleryshop.management.jwt.JwtUtils;
import com.jewelleryshop.management.model.JwtResponse;
import com.jewelleryshop.management.model.LoginRequest;
import com.jewelleryshop.management.model.MessageResponse;
import com.jewelleryshop.management.model.Role;
import com.jewelleryshop.management.model.SignupRequest;
import com.jewelleryshop.management.model.User;
import com.jewelleryshop.management.repo.impl.UserServiceRepoImpl;

import jakarta.validation.Valid;
@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserServiceRepoImpl userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${encryption.key}")
    private String encryptionKey;

    @PostMapping("/signin")
    public ResponseEntity<?> signInUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Received sign-in request for user: {}", loginRequest.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtUtils.generateJwtToken(authentication);

            User userDetails = (User) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(roles, jwtToken, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail()));
        } catch (BadCredentialsException e) {
            logger.warn("User {} not found or invalid credentials", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(ResponseMessage.USERNAME_NOT_PERSENT));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        logger.info("Received sign-up request for new user: {}", signUpRequest.getEmail());
        if (!signUpRequest.getRole().equals(Role.ROLE_RANGER.toString())) {
            logger.warn("Invalid sign-up role specified: {}", signUpRequest.getRole());
            return ResponseEntity.badRequest().body(new MessageResponse(ResponseMessage.INVALID_SIGNUP_ROLE));
        }

        if (userRepository.existsByUsername(signUpRequest.getUserName())) {
            logger.warn("Invalid sign-up role specified: {}", signUpRequest.getRole());
            return ResponseEntity.badRequest().body(new MessageResponse(ResponseMessage.USERNAME_ALREADY_TAKEN));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            logger.warn("Email {} is already in use", signUpRequest.getEmail());
            return ResponseEntity.badRequest().body(new MessageResponse(ResponseMessage.EMAIL_ALREADY_IN_USE));
        }
        User user = new User(signUpRequest.getUserName(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();

        Role rangerRole = Role.ROLE_RANGER;
        roles.add(rangerRole);

        user.setRoles(roles);
       // userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse(ResponseMessage.USER_REGISTERED_SUCCESSFULLY));
    }


 

}