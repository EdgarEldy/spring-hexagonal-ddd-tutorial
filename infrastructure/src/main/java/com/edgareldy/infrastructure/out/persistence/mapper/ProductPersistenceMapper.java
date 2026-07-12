package com.edgareldy.infrastructure.out.persistence.mapper;

import com.edgareldy.domain.model.product.Product;
import com.edgareldy.domain.model.shared.Money;
import com.edgareldy.infrastructure.out.persistence.entity.ProductEntity;

import java.util.Currency;

/**
 * Manual mapper between {@link Product} and {@link ProductEntity}, splitting/joining
 * {@link Money} into its two flat columns.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class ProductPersistenceMapper {

    private ProductPersistenceMapper() {
    }

    public static Product toDomain(ProductEntity entity) {
        Money unitPrice = new Money(entity.getUnitPriceAmount(), Currency.getInstance(entity.getUnitPriceCurrency()));
        return Product.reconstitute(entity.getId(), entity.getCategoryId(), entity.getProductName(), unitPrice);
    }

    public static ProductEntity toEntity(Product product) {
        return ProductEntity.builder()
                .id(product.getId())
                .categoryId(product.getCategoryId())
                .productName(product.getName())
                .unitPriceAmount(product.getUnitPrice().amount())
                .unitPriceCurrency(product.getUnitPrice().currency().getCurrencyCode())
                .build();
    }
}
