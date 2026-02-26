package com.kulakwetu.common.exception;

import com.kulakwetu.common.api.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(build("NOT_FOUND", ex.getMessage(), List.of(), request));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomain(DomainException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(build("DOMAIN_ERROR", ex.getMessage(), List.of(), request));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleValidation(Exception ex, HttpServletRequest request) {
        List<String> details;
        if (ex instanceof MethodArgumentNotValidException invalid) {
            details = invalid.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
        } else {
            details = List.of(ex.getMessage());
        }
        return ResponseEntity.badRequest()
                .body(build("VALIDATION_ERROR", "Validation failed", details, request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(build("INTERNAL_SERVER_ERROR", "Unexpected server error", List.of(), request));
    }

    private ErrorResponse build(String code, String message, List<String> details, HttpServletRequest request) {
        return new ErrorResponse(code, message, details, request.getRequestURI(), OffsetDateTime.now());
    }
}
