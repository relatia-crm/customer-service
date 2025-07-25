package com.relatia.customer_service.customer;

import com.relatia.customer_service.model.BaseDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest  {

    @NotBlank(message = "{validation.firstname.required}")
    @Size(max = 100, message = "{validation.firstname.size}")
    private String firstName;

    @NotBlank(message = "{validation.lastname.required}")
    @Size(max = 100, message = "{validation.lastname.size}")
    private String lastName;

    @Email(message = "{validation.email.invalid}")
    @NotBlank(message = "{validation.email.required}")
    @Size(max = 255, message = "{validation.email.size}")
    private String email;

    @Size(max = 20, message = "{validation.phone.size}")
    private String phone;

    @Size(max = 500, message = "{validation.address.size}")
    private String address;

    // You can add a method to convert to entity if needed
    public Customer toEntity() {
        return Customer.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .phone(this.phone)
                .address(this.address)
                .build();
    }
}