package com.airline.auth.controllers;


import com.airline.auth.config.CustomUserDetails;
import com.airline.auth.constants.ApiRoutes;
import com.airline.auth.dto.common.ApiResponse;
import com.airline.auth.dto.common.ApiResponseBuilder;
import com.airline.auth.dto.response.UserResponse;
import com.airline.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.USERS)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getUserDetails(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String email = customUserDetails.getUsername();

        UserResponse userResponse = userService.getUserDetails(email);

        System.out.println("inside user details");

        return ResponseEntity.status(HttpStatus.OK.value()).body(
                ApiResponseBuilder.success(
                        HttpStatus.OK.value(),
                        "User Details Fetch Successfully",
                        userResponse,
                        ApiRoutes.USERS
                )
        );
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<?>> getAllUsers(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String email = customUserDetails.getUsername();

        UserResponse userResponse = userService.getUserDetails(email);

        System.out.println("inside user details");

        return ResponseEntity.status(HttpStatus.OK.value()).body(
                ApiResponseBuilder.success(
                        HttpStatus.OK.value(),
                        "User Details Fetch Successfully",
                        userResponse,
                        ApiRoutes.USERS
                )
        );
    }
}
