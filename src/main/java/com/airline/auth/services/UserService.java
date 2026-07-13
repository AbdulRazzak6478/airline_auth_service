package com.airline.auth.services;


import com.airline.auth.dto.response.UserResponse;

public interface UserService {

    public UserResponse getUserDetails(String email);
}
