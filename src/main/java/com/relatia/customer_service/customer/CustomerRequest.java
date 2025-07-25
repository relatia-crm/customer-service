package com.relatia.customer_service.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(
    @NotBlank(message = "{validation.firstname.required}") String firstName,
    @NotBlank(message = "{validation.lastname.required}") String lastName,
    @Email(message = "{validation.email.invalid}")
    @NotBlank(message = "{validation.email.required}") String email,
    String phone,
    String address
) {}
