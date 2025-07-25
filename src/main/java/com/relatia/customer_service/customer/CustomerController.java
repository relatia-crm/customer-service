package com.relatia.customer_service.customer;

import com.relatia.customer_service.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.relatia.customer_service.constants.CustomerConstants.CUSTOMER_API;

/**
 * REST controller for managing customer operations.
 * Provides endpoints for creating, retrieving, updating, and deleting customers.
 */
@Tag(
    name = "Customer Management",
    description = "APIs for managing customer information including create, read, update, and delete operations"
)
@RequestMapping(
    path = CUSTOMER_API,
    produces = MediaType.APPLICATION_JSON_VALUE
)
@RestController
@RequiredArgsConstructor
@ApiResponses({
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
})
class CustomerController {
    
    private final CustomerService customerService;

    /**
     * Retrieves all customers.
     *
     * @return List of all customers with their details
     */
    @Operation(
        summary = "Get all customers",
        description = "Retrieves a list of all customers in the system"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved list of customers",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = CustomerResponse.class))
        )
    )
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    /**
     * Retrieves a specific customer by ID.
     *
     * @param id The ID of the customer to retrieve
     * @return The customer details if found
     */
    @Operation(
        summary = "Get customer by ID",
        description = "Retrieves a specific customer's details by their unique identifier"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved customer details",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CustomerResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Customer not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(
            @Parameter(
                description = "ID of the customer to be retrieved",
                required = true,
                example = "1"
            )
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    /**
     * Creates a new customer.
     *
     * @param request The customer details to create
     * @return The created customer details
     */
    @Operation(
        summary = "Create a new customer",
        description = "Creates a new customer with the provided details"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Customer successfully created",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CustomerResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CustomerResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Customer details to create",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = CustomerRequest.class)
                )
            )
            @Valid @RequestBody CustomerRequest request
    ) {
        return new ResponseEntity<>(customerService.create(request), HttpStatus.CREATED);
    }

    /**
     * Updates an existing customer.
     *
     * @param id The ID of the customer to update
     * @param request The updated customer details
     * @return The updated customer details
     */
    @Operation(
        summary = "Update an existing customer",
        description = "Updates the details of an existing customer identified by their ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Customer successfully updated",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CustomerResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Customer not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping(
        value = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CustomerResponse> update(
            @Parameter(
                description = "ID of the customer to be updated",
                required = true,
                example = "1"
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Updated customer details",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = CustomerRequest.class)
                )
            )
            @Valid @RequestBody CustomerRequest request
    ) {
        return ResponseEntity.ok(customerService.update(id, request));
    }

    /**
     * Deletes a customer by ID.
     *
     * @param id The ID of the customer to delete
     * @return No content (204) if successful
     */
    @Operation(
        summary = "Delete a customer",
        description = "Deletes a customer from the system by their ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Customer successfully deleted"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Customer not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(
                description = "ID of the customer to be deleted",
                required = true,
                example = "1"
            )
            @PathVariable Long id
    ) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
