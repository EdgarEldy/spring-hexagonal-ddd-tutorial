package com.edgareldy.infrastructure.in.web.controller;

import com.edgareldy.domain.model.customer.Customer;
import com.edgareldy.domain.port.in.CreateCustomerUseCase;
import com.edgareldy.domain.port.in.GetCustomerUseCase;
import com.edgareldy.infrastructure.in.web.dto.common.ApiResponse;
import com.edgareldy.infrastructure.in.web.dto.customer.CustomerRequest;
import com.edgareldy.infrastructure.in.web.dto.customer.CustomerResponse;
import com.edgareldy.infrastructure.in.web.exception.ResourceNotFoundException;
import com.edgareldy.infrastructure.in.web.mapper.CustomerWebMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * REST controller for {@code /api/v1/customers}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;

    public CustomerController(CreateCustomerUseCase createCustomerUseCase, GetCustomerUseCase getCustomerUseCase) {
        this.createCustomerUseCase = Objects.requireNonNull(createCustomerUseCase,
                "createCustomerUseCase must not be null");
        this.getCustomerUseCase = Objects.requireNonNull(getCustomerUseCase, "getCustomerUseCase must not be null");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> create(@RequestBody CustomerRequest request) {
        Customer customer = createCustomerUseCase.createCustomer(CustomerWebMapper.toCommand(request));
        CustomerResponse response = CustomerWebMapper.toResponse(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response, "Customer created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> get(@PathVariable Long id) {
        Customer customer = getCustomerUseCase.getCustomer(id)
                .orElseThrow(() -> new ResourceNotFoundException("no customer found with id " + id));
        return ResponseEntity.ok(ApiResponse.success(CustomerWebMapper.toResponse(customer), "Customer retrieved successfully"));
    }
}
