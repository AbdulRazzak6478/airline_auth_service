package com.airline.auth.services.implementation;

import com.airline.auth.config.CustomUserDetails;
import com.airline.auth.config.CustomUserDetailsService;
import com.airline.auth.config.JwtAuthenticationEntryPoint;
import com.airline.auth.dto.request.LoginUserRequest;
import com.airline.auth.dto.request.RefreshTokenRequest;
import com.airline.auth.dto.request.RegisterUserRequest;
import com.airline.auth.dto.response.LoginTokenResponse;
import com.airline.auth.dto.response.UserResponse;
import com.airline.auth.entity.RefreshToken;
import com.airline.auth.entity.User;
import com.airline.auth.enums.UserStatus;
import com.airline.auth.exception.DuplicateResourceException;
import com.airline.auth.exception.ResourceNotFoundException;
import com.airline.auth.mapper.UserMapper;
import com.airline.auth.repositories.RefreshTokenRepository;
import com.airline.auth.repositories.UserRepository;
import com.airline.auth.services.AuthService;
import com.airline.auth.services.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.ott.InvalidOneTimeTokenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtAuthenticationEntryPoint entryPoint;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

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
    public LoginTokenResponse loginUser(LoginUserRequest loginUserRequest, HttpServletRequest httpServletRequest) {

        System.out.println("login service");

        // Check Email Exist or not
        User user = userRepository.findByEmail(loginUserRequest.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("Email Not Exist"));

        System.out.println("user : "+user.getId());
        // Password Compare
//        boolean isMatch = passwordEncoder.matches(loginUserRequest.getPassword(), user.getPassword());
//
//        if(!isMatch){
//            throw new RuntimeException("Invalid Login Credentials");
//        }


        // Spring check and authenticate
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserRequest.getEmail(),
                        loginUserRequest.getPassword()
                )
        );

        System.out.println("User Authenticated");


        // Get User Details for token generation
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Check Refresh token Available

        Optional<RefreshToken> refreshTokenExist = refreshTokenRepository.findByUserId(user.getId());

        // If exist then Update it and return access token

        System.out.println("Authenticated User : "+userDetails.getUsername());
        if(refreshTokenExist.isPresent()){

            RefreshToken refreshToken = refreshTokenExist.get();
            refreshToken.setRevoked(false);
            refreshToken.setUserAgent(httpServletRequest.getHeader("User-Agent"));
            refreshToken.setIpAddress(httpServletRequest.getRemoteAddr());
            refreshToken.setExpiresAt(Instant.now().plusMillis(30 * 24 * 60 * 1000));

            String accessToken = jwtService.generateToken(userDetails);
            refreshTokenRepository.save(refreshToken);

            LoginTokenResponse tokenResponse = LoginTokenResponse.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .build();

            return tokenResponse;
        }

        String accessToken = jwtService.generateToken(userDetails);

        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // RefreshToken Response
        RefreshToken refreshTokenRecord = RefreshToken.builder()
                .userAgent(httpServletRequest.getHeader("User-Agent"))
                .ipAddress(httpServletRequest.getRemoteAddr())
                .token(refreshToken)
                .user(user)
                .revoked(false)
                .expiresAt(Instant.now().plusMillis(30 * 24 * 60 * 1000))
                .build();

        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshTokenRecord);

        // return response

        return LoginTokenResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public LoginTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        String token = refreshTokenRequest.getRefreshToken();


        String userEmail = jwtService.extractUsername(refreshTokenRequest.getRefreshToken());

        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new ResourceNotFoundException("Email Not Exist"));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

        if(!jwtService.isTokenValid(token, userDetails)){
            throw new RuntimeException("Token Expired");
        }

        RefreshToken refreshTokenRecord = refreshTokenRepository.findByToken(token).orElseThrow(()-> new ResourceNotFoundException("Token Not Found"));

        if(refreshTokenRecord.getRevoked())
        {
            throw new InvalidOneTimeTokenException(" Invalid Token Revoked, Please Login");
        }

        if(refreshTokenRecord.getExpiresAt().isBefore(Instant.now()))
        {
            throw new InvalidOneTimeTokenException("Token Expired,Please Login Again");
        }

        String accessToken = jwtService.generateToken(userDetails);

        refreshTokenRepository.save(refreshTokenRecord);

        LoginTokenResponse tokenResponse = LoginTokenResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(token)
                .build();

        return tokenResponse;
    }

    @Override
    public void logout(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User Not Not"));

        RefreshToken token = refreshTokenRepository.findByUserId(user.getId()).orElseThrow(()-> new ResourceNotFoundException("Token Not Found"));

        token.setRevoked(true);

        refreshTokenRepository.save(token);
    }

}
