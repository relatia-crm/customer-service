package com.relatia.customer_service.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "organisation")
public class OrganisationInfo {
    @NotBlank(message = "Organization name is required")
    private String name;
    
    @NotNull(message = "Address is required")
    @Valid
    private Address address;
    
    @NotNull(message = "Contact information is required")
    @Valid
    private Contact contact;
    
    @Pattern(regexp = "^[+]*[(]?[0-9]{1,4}[)]?[-\\s\\./0-9]*$|^$", 
             message = "Invalid phone number format")
    private String phone;
    
    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?|^$", 
             message = "Invalid website URL format")
    private String website;
    
    private String logo;
    
    @Valid
    private Social social;

    // Lombok handles all getters and setters

    @Getter
    @Setter
    public static class Address {
        @NotBlank(message = "Street address is required")
        private String street;
        
        @NotBlank(message = "City is required")
        private String city;
        
        @NotBlank(message = "State is required")
        private String state;
        
        @NotBlank(message = "ZIP code is required")
        private String zip;

        // Lombok handles all getters and setters
    }

    @Getter
    @Setter
    public static class Contact {
        @NotBlank(message = "Contact name is required")
        private String name;
        
        @NotBlank(message = "Email is required")
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
        private String email;

        // Lombok handles all getters and setters
    }

    @Getter
    @Setter
    public static class Social {
        @Pattern(regexp = "^(https?:\\/\\/)?(www\\.)?facebook\\.com\\/.*|^$", 
                message = "Invalid Facebook URL")
        private String facebook;
        
        @Pattern(regexp = "^(https?:\\/\\/)?(www\\.)?twitter\\.com\\/.*|^$", 
                message = "Invalid Twitter URL")
        private String twitter;
        
        @Pattern(regexp = "^(https?:\\/\\/)?(www\\.)?instagram\\.com\\/.*|^$", 
                message = "Invalid Instagram URL")
        private String instagram;

        // Lombok handles all getters and setters
    }
}
