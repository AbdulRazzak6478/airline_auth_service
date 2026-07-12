package com.airline.auth.services;

import com.airline.auth.dto.request.LoginUserRequest;
import com.airline.auth.dto.request.RegisterUserRequest;
import com.airline.auth.dto.response.UserResponse;

public interface AuthService {

    public UserResponse registerUser(RegisterUserRequest registerUserRequest);

    public UserResponse loginUser(LoginUserRequest loginUserRequest);

}
