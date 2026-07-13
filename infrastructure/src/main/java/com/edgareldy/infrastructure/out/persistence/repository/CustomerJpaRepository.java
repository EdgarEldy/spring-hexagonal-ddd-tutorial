package com.edgareldy.infrastructure.out.persistence.repository;

import com.edgareldy.infrastructure.out.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository on {@link CustomerEntity}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
}
