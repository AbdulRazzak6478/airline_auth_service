package com.airline.auth.controllers;


import com.airline.auth.constants.ApiRoutes;
import com.airline.auth.dto.common.ApiResponse;
import com.airline.auth.dto.common.ApiResponseBuilder;
import com.airline.auth.dto.request.LoginUserRequest;
import com.airline.auth.dto.request.RegisterUserRequest;
import com.airline.auth.dto.response.UserResponse;
import com.airline.auth.enums.Role;
import com.airline.auth.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ApiResponse<UserResponse>> login(
           @Valid  @RequestBody LoginUserRequest loginUserRequest
    ){


        // Call Service
        System.out.println("login controller");
        // response
        UserResponse userResponse = authService.loginUser(loginUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
                ApiResponseBuilder.success(
                        HttpStatus.CREATED.value(),
                        "Login Credentials Verified",
                        userResponse,
                        route + ApiRoutes.LOGIN

                )
        );
    }
}
