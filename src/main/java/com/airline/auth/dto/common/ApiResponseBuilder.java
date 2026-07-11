package com.airline.auth.dto.common;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class ApiResponseBuilder {

    public static <T> ApiResponse<T> success(int statusCode,String message, T data, String path)
    {
        return ApiResponse.<T>builder()
                .success(true)
                .status(statusCode)
                .message(message)
                .data(data)
                .errors(Collections.emptyList())
                .timestamp(LocalDateTime.now())
                .path(path)
                .traceId("RequestId")
                .build();
    }

    public static ApiResponse<List<String>> failed(int statusCode, String message, List<String> errors, String path)
    {

        return ApiResponse.<List<String>>builder()
                .success(false)
                .status(statusCode)
                .message(message)
                .data(Collections.emptyList())
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .path(path)
                .traceId("RequestId")
                .build();
    }
}
