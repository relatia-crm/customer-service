package com.relatia.customer_service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standardized error response format for API error handling.
 * This DTO provides a consistent structure for all error responses across the application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standardized error response format for API error handling")
public class ErrorResponse {
    @Schema(
        description = "The timestamp when the error occurred",
        example = "2025-07-25T17:00:00.000Z"
    )
    private LocalDateTime timestamp;

    @Schema(
        description = "HTTP status code"
    )
    private int status;

    @Schema(
        description = "HTTP status reason phrase"
    )
    private String error;

    @Schema(
        description = "A brief error message describing what went wrong"
    )
    private String message;

    @Schema(
        description = "The API endpoint path where the error occurred"
    )
    private String path;

    @Schema(
        description = "Additional error details, typically used for field validation errors",
        nullable = true
    )
    private Map<String, String> details;

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder()
                .timestamp(LocalDateTime.now());
    }
}
