package com.edgareldy.infrastructure.out.persistence.mapper;

import com.edgareldy.domain.model.customer.Customer;
import com.edgareldy.domain.model.shared.Email;
import com.edgareldy.infrastructure.out.persistence.entity.CustomerEntity;

/**
 * Manual mapper between {@link Customer} and {@link CustomerEntity}, wrapping/unwrapping
 * {@link Email}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class CustomerPersistenceMapper {

    private CustomerPersistenceMapper() {
    }

    public static Customer toDomain(CustomerEntity entity) {
        return Customer.reconstitute(entity.getId(), entity.getFirstName(), entity.getLastName(),
                entity.getTelephone(), new Email(entity.getEmail()), entity.getAddress());
    }

    public static CustomerEntity toEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .telephone(customer.getTelephone())
                .email(customer.getEmail().value())
                .address(customer.getAddress())
                .build();
    }
}
