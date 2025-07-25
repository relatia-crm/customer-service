package com.relatia.customer_service.customer;

import com.relatia.customer_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;

/**
 * Implementation of the {@link CustomerService} interface providing business logic
 * for customer-related operations including CRUD operations and business validations.
 * 
 * <p>This service manages customer data and ensures data consistency through
 * transaction management and proper error handling.</p>
 * 
 * @see CustomerService
 * @see Customer
 * @see CustomerRepository
 */
@Service
@RequiredArgsConstructor
class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final MessageSource messageSource;

    /**
     * Retrieves all customers from the system.
     *
     * <p>This method fetches all customer records from the database and converts them
     * to their corresponding DTO representation. The operation is read-only and does
     * not modify any data.</p>
     *
     * @return a list of {@link CustomerResponse} objects containing customer details.
     *         Returns an empty list if no customers are found.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
                .map(CustomerResponse::fromEntity)
                .toList();
    }

    /**
     * Retrieves a customer by their unique identifier.
     *
     * <p>This method looks up a customer by their ID and returns the corresponding
     * DTO. If no customer is found with the specified ID, a {@link ResourceNotFoundException}
     * is thrown with a localized error message.</p>
     *
     * @param id the unique identifier of the customer to retrieve
     * @return the {@link CustomerResponse} containing the customer details
     * @throws ResourceNotFoundException if no customer is found with the specified ID
     * @throws IllegalArgumentException if the provided ID is null
     */
    @Override
    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                messageSource.getMessage("error.id.null", null, LocaleContextHolder.getLocale()));
        }
        
        return customerRepository.findById(id)
                .map(CustomerResponse::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException(
                    messageSource.getMessage("error.customer.notfound", 
                    new Object[]{id}, 
                    LocaleContextHolder.getLocale())));
    }

    /**
     * Creates a new customer with the provided details.
     *
     * <p>This method validates that the email address is not already in use,
     * creates a new customer entity from the request DTO, and persists it to the database.
     * The operation is transactional and will be rolled back in case of any errors.</p>
     *
     * @param request the {@link CustomerRequest} containing the customer details to create
     * @return the created {@link CustomerResponse} with the generated ID and audit fields
     * @throws IllegalArgumentException if the email address is already in use or if the request is invalid
     * @throws NullPointerException if the request parameter is null
     */
    @Override
    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        if (request == null) {
            throw new NullPointerException("Customer request cannot be null");
        }
        
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                messageSource.getMessage("error.email.exists", 
                null, 
                LocaleContextHolder.getLocale()));
        }

        Customer customer = request.toEntity();
        return CustomerResponse.fromEntity(customerRepository.save(customer));
    }

    /**
     * Updates an existing customer's details.
     *
     * <p>This method updates the customer with the specified ID using the provided details.
     * It performs validation to ensure the email address (if changed) is not already in use.
     * The operation is transactional and will be rolled back in case of any errors.</p>
     *
     * @param id the ID of the customer to update
     * @param request the {@link CustomerRequest} containing the updated customer details
     * @return the updated {@link CustomerResponse}
     * @throws ResourceNotFoundException if no customer is found with the specified ID
     * @throws IllegalArgumentException if the email is being changed to one that's already in use
     * @throws NullPointerException if the request parameter is null
     */
    @Override
    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        if (id == null) {
            throw new IllegalArgumentException(
                messageSource.getMessage("error.id.null", null, LocaleContextHolder.getLocale()));
        }
        if (request == null) {
            throw new NullPointerException("Customer request cannot be null");
        }

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    messageSource.getMessage("error.customer.notfound", 
                    new Object[]{id}, 
                    LocaleContextHolder.getLocale())));

        // Check if email is being changed and if the new email already exists
        if (!customer.getEmail().equals(request.getEmail()) &&
            customerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                messageSource.getMessage("error.email.exists", 
                null, 
                LocaleContextHolder.getLocale()));
        }

        // Update customer details
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());

        return CustomerResponse.fromEntity(customerRepository.save(customer));
    }

    /**
     * Deletes a customer by their ID.
     *
     * <p>This method checks if a customer with the specified ID exists before attempting
     * deletion. If the customer doesn't exist, a {@link ResourceNotFoundException} is thrown.
     * The operation is transactional and will be rolled back in case of any errors.</p>
     *
     * @param id the ID of the customer to delete
     * @throws ResourceNotFoundException if no customer is found with the specified ID
     * @throws IllegalArgumentException if the provided ID is null
     */
    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                messageSource.getMessage("error.id.null", null, LocaleContextHolder.getLocale()));
        }
        
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                messageSource.getMessage("error.customer.notfound", 
                new Object[]{id}, 
                LocaleContextHolder.getLocale()));
        }
        customerRepository.deleteById(id);
    }
}
