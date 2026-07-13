package com.edgareldy.domain.port.out;

import com.edgareldy.domain.model.customer.Customer;

import java.util.Optional;

/**
 * Outbound port: persistence for {@code Customer}. Operates only on {@code domain} types, never
 * on DTOs or JPA entities. No {@code findAll}: the README's endpoint table exposes no paginated
 * customer listing, only creation and lookup by id.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface CustomerRepositoryPort {

    Customer save(Customer customer);

    Optional<Customer> findById(Long id);
}
