package com.airline.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginTokenResponse {

    private UUID userId;
    private String email;
    private String accessToken;
    private String refreshToken;
}
