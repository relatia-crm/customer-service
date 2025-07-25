package com.relatia.customer_service.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.relatia.customer_service.model.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for customer response.
 * Represents the customer data that is sent to the client.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Response object containing customer information")
public class CustomerResponse extends BaseDto {
    @Schema(
        description = "The first name of the customer",
        example = "John",
        required = true,
        maxLength = 100
    )
    private String firstName;

    @Schema(
        description = "The last name of the customer",
        example = "Doe",
        required = true,
        maxLength = 100
    )
    private String lastName;

    @Schema(
        description = "The email address of the customer",
        example = "john.doe@example.com",
        required = true,
        format = "email",
        maxLength = 255
    )
    private String email;

    @Schema(
        description = "The phone number of the customer",
        example = "+1234567890",
        required = false,
        maxLength = 20
    )
    private String phone;

    @Schema(
        description = "The physical address of the customer",
        example = "123 Main St, Anytown, USA",
        required = false,
        maxLength = 500
    )
    private String address;
    
    public static CustomerResponse fromEntity(Customer customer) {
        CustomerResponse response = CustomerResponse.builder()
            .firstName(customer.getFirstName())
            .lastName(customer.getLastName())
            .email(customer.getEmail())
            .phone(customer.getPhone())
            .address(customer.getAddress())
            .build();
            
        // Set audit fields from the entity
        response.setAuditFields(customer);
        return response;
    }
}
