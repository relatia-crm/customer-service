package com.relatia.customer_service.customer;

public record CustomerResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    String phone,
    String address
) {
    public static CustomerResponse fromEntity(Customer customer) {
        return new CustomerResponse(
            customer.getId(),
            customer.getFirstName(),
            customer.getLastName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getAddress()
        );
    }
}
