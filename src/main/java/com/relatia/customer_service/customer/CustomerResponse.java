package com.relatia.customer_service.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.relatia.customer_service.model.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerResponse extends BaseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
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
