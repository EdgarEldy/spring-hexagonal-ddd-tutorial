package com.edgareldy.application.service;

import com.edgareldy.domain.model.customer.Customer;
import com.edgareldy.domain.model.shared.Email;
import com.edgareldy.domain.port.out.CustomerRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Mockito tests for {@link GetCustomerService}: delegates to
 * {@link CustomerRepositoryPort#findById}, propagating an empty {@link Optional} as-is.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@ExtendWith(MockitoExtension.class)
class GetCustomerServiceTest {

    @Mock
    private CustomerRepositoryPort customerRepositoryPort;

    @Test
    void returns_the_customer_when_found() {
        Customer customer = Customer.reconstitute(1L, "Jane", "Doe", "0102030405", new Email("jane@example.com"),
                "1 rue de Paris");
        when(customerRepositoryPort.findById(1L)).thenReturn(Optional.of(customer));
        GetCustomerService service = new GetCustomerService(customerRepositoryPort);

        Optional<Customer> result = service.getCustomer(1L);

        assertThat(result).contains(customer);
    }

    @Test
    void returns_empty_when_not_found() {
        when(customerRepositoryPort.findById(404L)).thenReturn(Optional.empty());
        GetCustomerService service = new GetCustomerService(customerRepositoryPort);

        Optional<Customer> result = service.getCustomer(404L);

        assertThat(result).isEmpty();
    }
}
