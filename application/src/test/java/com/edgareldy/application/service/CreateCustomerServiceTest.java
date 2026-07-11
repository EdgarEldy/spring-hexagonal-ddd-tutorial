package com.edgareldy.application.service;

import com.edgareldy.domain.model.customer.Customer;
import com.edgareldy.domain.model.shared.Email;
import com.edgareldy.domain.port.in.command.CreateCustomerCommand;
import com.edgareldy.domain.port.out.CustomerRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Mockito tests for {@link CreateCustomerService}: Email format validation surfaces before any
 * repository call, and the happy path delegates to {@link CustomerRepositoryPort}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@ExtendWith(MockitoExtension.class)
class CreateCustomerServiceTest {

    @Mock
    private CustomerRepositoryPort customerRepositoryPort;

    @Test
    void creates_and_saves_a_customer_with_a_valid_email() {
        Customer saved = Customer.reconstitute(1L, "Jane", "Doe", "0102030405", new Email("jane@example.com"),
                "1 rue de Paris");
        when(customerRepositoryPort.save(any(Customer.class))).thenReturn(saved);
        CreateCustomerService service = new CreateCustomerService(customerRepositoryPort);
        CreateCustomerCommand command = new CreateCustomerCommand("Jane", "Doe", "0102030405", "jane@example.com",
                "1 rue de Paris");

        Customer result = service.createCustomer(command);

        assertThat(result).isEqualTo(saved);
    }

    @Test
    void rejects_a_malformed_email_before_touching_the_repository() {
        CreateCustomerService service = new CreateCustomerService(customerRepositoryPort);
        CreateCustomerCommand command = new CreateCustomerCommand("Jane", "Doe", "0102030405", "not-an-email",
                "1 rue de Paris");

        assertThatThrownBy(() -> service.createCustomer(command)).isInstanceOf(IllegalArgumentException.class);

        verify(customerRepositoryPort, never()).save(any());
    }
}
