package com.edgareldy.infrastructure.out.persistence.adapter;

import com.edgareldy.domain.model.customer.Customer;
import com.edgareldy.domain.model.shared.Email;
import com.edgareldy.infrastructure.PostgresTestcontainersConfiguration;
import com.edgareldy.infrastructure.out.persistence.repository.CustomerJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@code @DataJpaTest} for {@link CustomerRepositoryAdapter}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@DataJpaTest
@Import(PostgresTestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryAdapterTest {

    @Autowired
    private CustomerJpaRepository customerJpaRepository;

    private CustomerRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new CustomerRepositoryAdapter(customerJpaRepository);
    }

    @Test
    void saves_and_retrieves_a_customer_by_id() {
        Customer saved = adapter.save(
                Customer.create("Jane", "Doe", "0102030405", new Email("jane@example.com"), "1 rue de Paris"));

        Optional<Customer> found = adapter.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(new Email("jane@example.com"));
    }

    @Test
    void returns_empty_when_not_found() {
        assertThat(adapter.findById(404L)).isEmpty();
    }
}
