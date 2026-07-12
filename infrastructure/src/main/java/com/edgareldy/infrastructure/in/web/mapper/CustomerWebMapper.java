package com.edgareldy.infrastructure.in.web.mapper;

import com.edgareldy.domain.model.customer.Customer;
import com.edgareldy.domain.port.in.command.CreateCustomerCommand;
import com.edgareldy.infrastructure.in.web.dto.customer.CustomerRequest;
import com.edgareldy.infrastructure.in.web.dto.customer.CustomerResponse;

/**
 * Manual mapper between the customer web DTOs and the domain.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class CustomerWebMapper {

    private CustomerWebMapper() {
    }

    public static CreateCustomerCommand toCommand(CustomerRequest request) {
        return new CreateCustomerCommand(request.firstName(), request.lastName(), request.telephone(),
                request.email(), request.address());
    }

    public static CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(customer.getId(), customer.getFirstName(), customer.getLastName(),
                customer.getTelephone(), customer.getEmail().value(), customer.getAddress());
    }
}
