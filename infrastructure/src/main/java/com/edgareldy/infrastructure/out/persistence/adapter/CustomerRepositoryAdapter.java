package com.edgareldy.infrastructure.out.persistence.adapter;

import com.edgareldy.domain.model.customer.Customer;
import com.edgareldy.domain.port.out.CustomerRepositoryPort;
import com.edgareldy.infrastructure.out.persistence.entity.CustomerEntity;
import com.edgareldy.infrastructure.out.persistence.mapper.CustomerPersistenceMapper;
import com.edgareldy.infrastructure.out.persistence.repository.CustomerJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

/**
 * Implements {@link CustomerRepositoryPort} via {@link CustomerJpaRepository}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@Repository
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final CustomerJpaRepository customerJpaRepository;

    public CustomerRepositoryAdapter(CustomerJpaRepository customerJpaRepository) {
        this.customerJpaRepository = Objects.requireNonNull(customerJpaRepository,
                "customerJpaRepository must not be null");
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = CustomerPersistenceMapper.toEntity(customer);
        CustomerEntity saved = customerJpaRepository.save(entity);
        return CustomerPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerJpaRepository.findById(id).map(CustomerPersistenceMapper::toDomain);
    }
}
