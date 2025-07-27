package com.relatia.customer_service.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.relatia.customer_service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    private CustomerResponse testCustomerResponse;

    @BeforeEach
    void setUp() {
        testCustomerResponse = CustomerResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .build();
    }

    @Test
    void getCustomerById_ShouldReturnCustomer() throws Exception {
        // Given
        when(customerService.findById(1L)).thenReturn(testCustomerResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void getCustomerById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        when(customerService.findById(999L))
                .thenThrow(new ResourceNotFoundException("Customer not found"));

        // When & Then
        mockMvc.perform(get("/api/v1/customers/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllCustomers_ShouldReturnList() throws Exception {
        // Given
        when(customerService.findAll()).thenReturn(Collections.singletonList(testCustomerResponse));

        // When & Then
        mockMvc.perform(get("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));
    }
}
