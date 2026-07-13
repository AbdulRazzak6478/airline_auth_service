package com.airline.auth.services.implementation;


import com.airline.auth.controllers.UserController;
import com.airline.auth.dto.common.PageResponse;
import com.airline.auth.dto.request.UserSearchListRequest;
import com.airline.auth.dto.response.UserResponse;
import com.airline.auth.entity.User;
import com.airline.auth.exception.ResourceNotFoundException;
import com.airline.auth.mapper.UserMapper;
import com.airline.auth.repositories.UserRepository;
import com.airline.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public PageResponse<UserResponse> getUserList(UserSearchListRequest userSearchListRequest) {

        Sort.Direction direction =
                "DESC".equalsIgnoreCase(userSearchListRequest.getSortDirection())
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                userSearchListRequest.getPage(),
                userSearchListRequest.getLimit(),
                Sort.by(
                        direction,
                        userSearchListRequest.getSortBy()
                )
        );

        Page<UserResponse> userPage = userRepository.findAll(pageable).map(UserMapper::toResponse);

        return PageResponse.from(userPage);
    }
}
