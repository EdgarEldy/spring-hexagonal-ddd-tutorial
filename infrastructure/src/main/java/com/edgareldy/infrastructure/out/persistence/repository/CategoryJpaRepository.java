package com.edgareldy.infrastructure.out.persistence.repository;

import com.edgareldy.infrastructure.out.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository on {@link CategoryEntity}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
}
