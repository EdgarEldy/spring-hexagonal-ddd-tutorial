package com.edgareldy.infrastructure.in.web.mapper;

import com.edgareldy.domain.model.product.Product;
import com.edgareldy.domain.model.shared.Money;
import com.edgareldy.domain.port.in.command.CreateProductCommand;
import com.edgareldy.infrastructure.in.web.dto.common.MoneyDto;
import com.edgareldy.infrastructure.in.web.dto.product.ProductRequest;
import com.edgareldy.infrastructure.in.web.dto.product.ProductResponse;

import java.util.Currency;

/**
 * Manual mapper between the product web DTOs and the domain.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class ProductWebMapper {

    private ProductWebMapper() {
    }

    public static CreateProductCommand toCommand(ProductRequest request) {
        Money unitPrice = new Money(request.unitPrice().amount(), Currency.getInstance(request.unitPrice().currency()));
        return new CreateProductCommand(request.categoryId(), request.name(), unitPrice);
    }

    public static ProductResponse toResponse(Product product) {
        MoneyDto unitPrice = new MoneyDto(product.getUnitPrice().amount(),
                product.getUnitPrice().currency().getCurrencyCode());
        return new ProductResponse(product.getId(), product.getCategoryId(), product.getName(), unitPrice);
    }
}
