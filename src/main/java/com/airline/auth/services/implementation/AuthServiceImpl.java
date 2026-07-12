package com.airline.auth.services.implementation;

import com.airline.auth.dto.request.LoginUserRequest;
import com.airline.auth.dto.request.RegisterUserRequest;
import com.airline.auth.dto.response.UserResponse;
import com.airline.auth.entity.User;
import com.airline.auth.enums.UserStatus;
import com.airline.auth.exception.DuplicateResourceException;
import com.airline.auth.exception.ResourceNotFoundException;
import com.airline.auth.mapper.UserMapper;
import com.airline.auth.repositories.UserRepository;
import com.airline.auth.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserResponse registerUser(RegisterUserRequest registerUserRequest) {

        // Check Duplicate Email
        boolean emailExist = userRepository.existsByEmail(registerUserRequest.getEmail());
        if(emailExist){
            throw new DuplicateResourceException("Email already exists");
        }


        // Check for Duplicate mobile

        boolean mobileNumberExist = userRepository.existsByMobileNumber(registerUserRequest.getMobileNumber());

        if(mobileNumberExist){
            throw new DuplicateResourceException("Mobile number already exists");
        }

        // Entity Mapper
        User user = UserMapper.toEntity(registerUserRequest);
        user.setRole(registerUserRequest.getRole());
        user.setStatus(UserStatus.ACTIVE);

        String encodedPassword = passwordEncoder.encode(registerUserRequest.getPassword());
        user.setPassword(encodedPassword);

        // Create user

        User savedUser = userRepository.save(user);

        // Response Mapper

        UserResponse userResponse = UserMapper.toResponse(savedUser);

        return userResponse;
    }

    @Override
    public UserResponse loginUser(LoginUserRequest loginUserRequest) {

        System.out.println("login service");
        // Check Email Exist or not
        User user = userRepository.findByEmail(loginUserRequest.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("Email Not Exist"));

        System.out.println("user : "+user.getId());
        // Password Compare
//        boolean isMatch = passwordEncoder.matches(loginUserRequest.getPassword(), user.getPassword());
//
//        if(!isMatch){
//            throw new RuntimeException("Invalid Login Credentials");
//        }


        // Spring check and authenticate
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserRequest.getEmail(),
                        loginUserRequest.getPassword()
                )
        );

        System.out.println("User Authenticated");


        // Get User Details for token generation
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Check Refresh token Available

        // If exist then Update it and return access token

        // If no Token exist then prepare refresh and access token

        // return response

        UserResponse userResponse = UserMapper.toResponse(user);

        return userResponse;
    }

}
