package com.relatia.customer_service.customer;

import com.relatia.customer_service.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {
    
    @NotBlank(message = "{validation.firstname.required}")
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @NotBlank(message = "{validation.lastname.required}")
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Email(message = "{validation.email.invalid}")
    @NotBlank(message = "{validation.email.required}")
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "phone_number")
    private String phone;
    
    @Column(columnDefinition = "TEXT")
    private String address;
}
