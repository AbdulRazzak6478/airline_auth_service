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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        // Check Email Exist or not
        User user = userRepository.findByEmail(loginUserRequest.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("Email Not Exist"));

        // Password Compare
        boolean isMatch = passwordEncoder.matches(loginUserRequest.getPassword(), user.getPassword());

        if(!isMatch){
            throw new RuntimeException("Invalid Login Credentials");
        }

        // Check Refresh token Available

        // If exist then Update it and return access token

        // If no Token exist then prepare refresh and access token

        // return response

        return null;
    }

}
