package com.airline.auth.services.implementation;


import com.airline.auth.controllers.UserController;
import com.airline.auth.dto.response.UserResponse;
import com.airline.auth.entity.User;
import com.airline.auth.exception.ResourceNotFoundException;
import com.airline.auth.repositories.UserRepository;
import com.airline.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponse getUserDetails(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found"));

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .mobilNumber(String.valueOf(user.getMobileNumber()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        return userResponse;
    }
}
