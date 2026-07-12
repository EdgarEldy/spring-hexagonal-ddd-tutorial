package com.edgareldy.infrastructure.out.persistence.repository;

import com.edgareldy.infrastructure.out.persistence.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository on {@link ProductEntity}. {@code findByCategoryId} is a derived
 * query backing the products endpoint's {@code categoryId} filter.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    Page<ProductEntity> findByCategoryId(Long categoryId, Pageable pageable);
}
