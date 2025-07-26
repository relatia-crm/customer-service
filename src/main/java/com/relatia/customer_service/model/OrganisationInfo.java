package com.relatia.customer_service.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "organisation")
public record OrganisationInfo(
    @NotBlank(message = "Organization name is required")
    String name,
    
    @NotNull(message = "Address is required")
    @Valid
    Address address,
    
    @NotNull(message = "Contact information is required")
    @Valid
    Contact contact,
    
    @Pattern(regexp = "^[+]*[(]?[0-9]{1,4}[)]?[-\\s\\./0-9]*$", 
             message = "Invalid phone number format")
    String phone,
    
    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?$", 
             message = "Invalid website URL format")
    String website,
    
    String logo,
    
    @Valid
    Social social
) {
    public record Address(
        @NotBlank(message = "Street address is required")
        String street,
        
        @NotBlank(message = "City is required")
        String city,
        
        @NotBlank(message = "State is required")
        String state,
        
        @NotBlank(message = "ZIP code is required")
        @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Invalid ZIP code format")
        String zip
    ) {}

    public record Contact(
        @NotBlank(message = "Contact name is required")
        String name,
        
        @NotBlank(message = "Email is required")
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
        String email
    ) {}

    public record Social(
        @Pattern(regexp = "^(https?:\\/\\/)?(www\\.)?facebook\\.com\\/.*|$", 
                message = "Invalid Facebook URL")
        String facebook,
        
        @Pattern(regexp = "^(https?:\\/\\/)?(www\\.)?twitter\\.com\\/.*|$", 
                message = "Invalid Twitter URL")
        String twitter,
        
        @Pattern(regexp = "^(https?:\\/\\/)?(www\\.)?instagram\\.com\\/.*|$", 
                message = "Invalid Instagram URL")
        String instagram
    ) {}
}
