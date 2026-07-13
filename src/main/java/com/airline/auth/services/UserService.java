package com.airline.auth.services;


import com.airline.auth.dto.common.PageResponse;
import com.airline.auth.dto.request.UserSearchListRequest;
import com.airline.auth.dto.response.UserResponse;

public interface UserService {

    public UserResponse getUserDetails(String email);

    public PageResponse<UserResponse> getUserList(UserSearchListRequest userSearchListRequest);
}
