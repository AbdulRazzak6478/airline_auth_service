package com.airline.auth.services;

import com.airline.auth.dto.request.LoginUserRequest;
import com.airline.auth.dto.request.RefreshTokenRequest;
import com.airline.auth.dto.request.RegisterUserRequest;
import com.airline.auth.dto.response.LoginTokenResponse;
import com.airline.auth.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    public UserResponse registerUser(RegisterUserRequest registerUserRequest);

    public LoginTokenResponse loginUser(LoginUserRequest loginUserRequest, HttpServletRequest httpServletRequest);

    public LoginTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    public void logout(String Email);

}
