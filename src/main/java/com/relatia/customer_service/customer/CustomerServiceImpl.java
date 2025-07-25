package com.relatia.customer_service.customer;

import com.relatia.customer_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;

@Service
@RequiredArgsConstructor
class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final MessageSource messageSource;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
                .map(CustomerResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id) {
        return customerRepository.findById(id)
                .map(CustomerResponse::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException(
                    messageSource.getMessage("error.customer.notfound", 
                    new Object[]{id}, 
                    LocaleContextHolder.getLocale())));
    }

    @Override
    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                messageSource.getMessage("error.email.exists", 
                null, 
                LocaleContextHolder.getLocale()));
        }

        Customer customer = request.toEntity();

        return CustomerResponse.fromEntity(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    messageSource.getMessage("error.customer.notfound", 
                    new Object[]{id}, 
                    LocaleContextHolder.getLocale())));

        if (!customer.getEmail().equals(request.getEmail()) &&
            customerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                messageSource.getMessage("error.email.exists", 
                null, 
                LocaleContextHolder.getLocale()));
        }

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());

        return CustomerResponse.fromEntity(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                messageSource.getMessage("error.customer.notfound", 
                new Object[]{id}, 
                LocaleContextHolder.getLocale()));
        }
        customerRepository.deleteById(id);
    }
}
