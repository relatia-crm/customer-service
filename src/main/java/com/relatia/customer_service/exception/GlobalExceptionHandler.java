package com.relatia.customer_service.exception;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler that processes exceptions and returns standardized error responses.
 * This handler ensures consistent error responses across the entire application.
 */
@RestControllerAdvice
@ApiResponse(
    responseCode = "500",
    description = "Internal Server Error",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ErrorResponse.class),
        examples = @ExampleObject(
            value = """
                {
                  "timestamp": "2025-07-25T17:30:00.000Z",
                  "status": 500,
                  "error": "Internal Server Error",
                  "message": "An unexpected error occurred"
                }
                """
        )
    )
)
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException and returns a 404 Not Found response.
     *
     * @param ex The caught ResourceNotFoundException
     * @param request The web request that caused the exception
     * @return ResponseEntity containing the error details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(
        responseCode = "404",
        description = "Resource Not Found",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject(
                value = """
                    {
                      "timestamp": "2025-07-25T17:30:00.000Z",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Customer with id 123 not found",
                      "path": "/api/customers/123"
                    }
                    """
            )
        )
    )
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles IllegalArgumentException and returns a 400 Bad Request response.
     *
     * @param ex The caught IllegalArgumentException
     * @param request The web request that caused the exception
     * @return ResponseEntity containing the error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(
        responseCode = "400",
        description = "Bad Request - Invalid input",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject(
                value = """
                    {
                      "timestamp": "2025-07-25T17:30:00.000Z",
                      "status": 400,
                      "error": "Bad Request",
                      "message": "Invalid customer ID format",
                      "path": "/api/customers/invalid-id"
                    }
                    """
            )
        )
    )
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentNotValidException and returns a 400 Bad Request response with validation errors.
     *
     * @param ex The caught MethodArgumentNotValidException
     * @param request The web request that caused the exception
     * @return ResponseEntity containing the validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(
        responseCode = "400",
        description = "Bad Request - Validation failed",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject(
                value = """
                    {
                      "timestamp": "2025-07-25T17:30:00.000Z",
                      "status": 400,
                      "error": "Bad Request",
                      "message": "Validation failed",
                      "path": "/api/customers",
                      "details": {
                        "email": "must be a well-formed email address",
                        "name": "must not be blank"
                      }
                    }
                    """
            )
        )
    )
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
                
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .details(errors)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
