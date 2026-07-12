package com.edgareldy.infrastructure.out.persistence.repository;

import com.edgareldy.infrastructure.out.persistence.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository on {@link OrderEntity}. {@code findAll}/{@code findById} are
 * overridden with an {@code @EntityGraph} on {@code lines}: without it, {@code lines} being
 * {@code LAZY} would throw {@code LazyInitializationException} the moment the persistence
 * mapper reads it outside the (already-closed) transaction each repository call runs in.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    @Override
    @EntityGraph(attributePaths = "lines")
    Page<OrderEntity> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "lines")
    Optional<OrderEntity> findById(Long id);
}
