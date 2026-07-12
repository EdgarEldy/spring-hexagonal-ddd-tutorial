package com.edgareldy.infrastructure.out.persistence.adapter;

import com.edgareldy.domain.model.product.Product;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.command.ListProductsQuery;
import com.edgareldy.domain.port.out.ProductRepositoryPort;
import com.edgareldy.infrastructure.out.persistence.entity.ProductEntity;
import com.edgareldy.infrastructure.out.persistence.mapper.PageMapper;
import com.edgareldy.infrastructure.out.persistence.mapper.ProductPersistenceMapper;
import com.edgareldy.infrastructure.out.persistence.repository.ProductJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

/**
 * Implements {@link ProductRepositoryPort} via {@link ProductJpaRepository}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@Repository
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;

    public ProductRepositoryAdapter(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = Objects.requireNonNull(productJpaRepository,
                "productJpaRepository must not be null");
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = ProductPersistenceMapper.toEntity(product);
        ProductEntity saved = productJpaRepository.save(entity);
        return ProductPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id).map(ProductPersistenceMapper::toDomain);
    }

    @Override
    public PageResult<Product> findAll(ListProductsQuery query) {
        Pageable pageable = PageMapper.toPageable(query.page());
        Page<ProductEntity> page = query.categoryId() != null
                ? productJpaRepository.findByCategoryId(query.categoryId(), pageable)
                : productJpaRepository.findAll(pageable);
        return PageMapper.toPageResult(page, ProductPersistenceMapper::toDomain);
    }
}
