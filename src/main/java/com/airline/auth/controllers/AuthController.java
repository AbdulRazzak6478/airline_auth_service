package com.airline.auth.controllers;


import com.airline.auth.config.CustomUserDetails;
import com.airline.auth.constants.ApiRoutes;
import com.airline.auth.dto.common.ApiResponse;
import com.airline.auth.dto.common.ApiResponseBuilder;
import com.airline.auth.dto.request.LoginUserRequest;
import com.airline.auth.dto.request.RefreshTokenRequest;
import com.airline.auth.dto.request.RegisterUserRequest;
import com.airline.auth.dto.response.LoginTokenResponse;
import com.airline.auth.dto.response.UserResponse;
import com.airline.auth.enums.Role;
import com.airline.auth.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiRoutes.AUTH)
public class AuthController {

    private final String route =  ApiRoutes.AUTH;

    @Autowired
    private AuthService authService;

    @PostMapping(ApiRoutes.REGISTER)
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(
           @Valid @RequestBody RegisterUserRequest registerUserRequest
    ){

        if(!List.of(Role.USER, Role.AIRLINE_MANAGER,Role.STAFF,Role.CUSTOMER).stream().anyMatch(r -> r.equals(registerUserRequest.getRole()))) {
            throw new RuntimeException("Invalid role");
        }

        // Call Service
        UserResponse response = authService.registerUser(registerUserRequest);


        // Return Response
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
                ApiResponseBuilder.success(
                        HttpStatus.CREATED.value(),
                        "User Registered Successfully",
                        response,
                        route + ApiRoutes.REGISTER
                )
        );
    }

    @PostMapping(ApiRoutes.LOGIN)
    public ResponseEntity<ApiResponse<LoginTokenResponse>> login(
           @Valid  @RequestBody LoginUserRequest loginUserRequest,
           HttpServletRequest httpServletRequest
    ){


        // Call Service
        System.out.println("login controller");
        LoginTokenResponse tokenResponse = authService.loginUser(loginUserRequest, httpServletRequest);

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
                ApiResponseBuilder.success(
                        HttpStatus.CREATED.value(),
                        "Login Credentials Verified",
                        tokenResponse,
                        route + ApiRoutes.LOGIN

                )
        );
    }


    @PostMapping(ApiRoutes.REFRESH)
    public ResponseEntity<ApiResponse<LoginTokenResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest
    )
    {

        System.out.println("refresh controller");
        LoginTokenResponse tokenResponse = authService.refreshToken(refreshTokenRequest);

        return ResponseEntity.status(HttpStatus.OK.value()).body(
                ApiResponseBuilder.success(
                        HttpStatus.OK.value(),
                        "Token Refresh Successfully",
                        tokenResponse,
                        route + ApiRoutes.REFRESH
                )
        );
    }

    @PostMapping(ApiRoutes.LOGOUT)
    public ResponseEntity<ApiResponse<String>> logout ()
    {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        System.out.println("inside logout controller");

        String email = user.getUsername();

        System.out.println("Authenticated user email : "+ email);

        authService.logout(email);

        return ResponseEntity.status(HttpStatus.OK.value()).body(
                ApiResponseBuilder.success(
                        HttpStatus.OK.value(),
                        "Successfully logout User",
                        "User Access Revoked",
                        route + ApiRoutes.LOGOUT
                )
        );
    }
}
