package com.airline.auth.exception;

import com.airline.auth.dto.common.ApiResponse;
import com.airline.auth.dto.common.ApiResponseBuilder;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GlobalExceptionHandler {

    // 1. Exception : Default Exception Handler for generic Exception
    // 2. MethodArgumentNotValidException : For DTO validation Exception Handler
    // 3. HttpMessageNotReadableException : For Request Body data Validation Exception
    // 4. For Invalid Request, path variable, query params, numbers Validation Exception
    // 5. DataIntegrityViolationException : For Database Constraint Exception
    // 6. ResourceNotFoundException : For business Logic -> Resource Not Exception
    // 7. DuplicateResourceException : For business logic -> Duplicate Resource Exception

    // 1. Exception : Default Exception Handler for generic Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<List<String>>> handleGenricException(
            Exception ex,
            HttpServletRequest request
    ) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ApiResponseBuilder.failed(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Something went wrong, try Again",
                                List.of(ex.getMessage()),
                                request.getRequestURI()
                        )
                );
    }

    // 2. MethodArgumentNotValidException : For DTO validation Exception Handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + " : " + error.getDefaultMessage())
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponseBuilder.failed(
                        HttpStatus.BAD_REQUEST.value(),
                        "Validation Failed",
                        errors,
                        request.getRequestURI()
                )
        );
    }

    // 3. HttpMessageNotReadableException : For Request Body data Validation Exception
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException invalidFormatException) {

            Class<?> targetType = invalidFormatException.getTargetType();

            // Handle Enum Validation
            if (targetType.isEnum()) {

                String field = invalidFormatException.getPath()
                        .stream()
                        .map(JsonMappingException.Reference::getFieldName)
                        .collect(Collectors.joining("."));

                String invalidValue = String.valueOf(invalidFormatException.getValue());

                String allowedValues = Arrays.stream(targetType.getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ApiResponseBuilder.failed(
                                HttpStatus.BAD_REQUEST.value(),
                                "Validation Failed",
                                List.of(String.format(
                                        "Invalid value '%s' for '%s'. Allowed values are: %s.",
                                        invalidValue,
                                        field,
                                        allowedValues
                                )),
                                request.getRequestURI()
                        )
                );
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponseBuilder.failed(
                        HttpStatus.BAD_REQUEST.value(),
                        "Malformed Request",
                        List.of(ex.getMessage()),
                        request.getRequestURI()
                )
        );
    }

    // 4. For Invalid Request, path variable, query params, numbers Validation Exception
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String message = String.format(
                "Invalid value '%s' for parameter '%s'.",
                ex.getValue(),
                ex.getName()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponseBuilder.failed(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid Request Parameter",
                        List.of(message),
                        request.getRequestURI()
                )
        );
    }

    // 5. DataIntegrityViolationException : For Database Constraint Exception
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponseBuilder.failed(
                        HttpStatus.CONFLICT.value(),
                        "Database Constraint Violation",
                        List.of(ex.getMessage()),
                        request.getRequestURI()
                )
        );
    }


    // 6. ResourceNotFoundException : For business Logic -> Resource Not Exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponseBuilder.failed(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Resource Not Found",
                        List.of(ex.getMessage()),
                        request.getRequestURI()
                )
        );
    }

    // 7. DuplicateResourceException : For business logic -> Duplicate Resource Exception
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleDuplicateResourceException(
            DuplicateResourceException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponseBuilder.failed(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Duplicate Resource",
                        List.of(ex.getMessage()),
                        request.getRequestURI()
                )
        );
    }
}
