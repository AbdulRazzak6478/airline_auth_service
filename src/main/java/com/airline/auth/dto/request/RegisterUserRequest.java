package com.airline.auth.dto.request;

import com.airline.auth.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {

    @NotBlank( message = "First Name is required.")
    private String firstName;

    @NotBlank( message = "Last Name is required.")
    private String lastName;

    @NotBlank( message = "Mobile is required.")
    @Pattern(
            regexp = "^\\+[1-9]\\d{1,14}$",
            message = "Please provide a valid number in E.164 format (e.g. +919876543210)."
    )
    private String mobileNumber;

    @NotBlank( message = "email is required.")
    @Email(message = "Please provide a valid email address.")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Please provide a valid email address."
    )
    private String email;

    @NotBlank( message = "password Name is required.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$",
            message = "Password must be 8-20 characters and contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    private String password;

    @NotNull( message = "Role is required.")
    private Role role;
}
