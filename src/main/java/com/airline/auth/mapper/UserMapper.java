package com.airline.auth.mapper;

import com.airline.auth.dto.request.RegisterUserRequest;
import com.airline.auth.dto.response.UserResponse;
import com.airline.auth.entity.User;

public class UserMapper {

    public static User toEntity(RegisterUserRequest registerUserRequest) {

        return User.builder()
                .email(registerUserRequest.getEmail())
                .firstName(registerUserRequest.getFirstName())
                .lastName(registerUserRequest.getLastName())
                .password(registerUserRequest.getPassword())
                .role(registerUserRequest.getRole())
                .mobileNumber(registerUserRequest.getMobileNumber())
                .build();
    }


    public static UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .mobilNumber(String.valueOf(user.getMobileNumber()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
