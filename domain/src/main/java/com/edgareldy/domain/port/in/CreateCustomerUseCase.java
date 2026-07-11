package com.edgareldy.domain.port.in;

import com.edgareldy.domain.model.customer.Customer;
import com.edgareldy.domain.port.in.command.CreateCustomerCommand;

/**
 * Inbound port: create a customer.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface CreateCustomerUseCase {

    Customer createCustomer(CreateCustomerCommand command);
}
