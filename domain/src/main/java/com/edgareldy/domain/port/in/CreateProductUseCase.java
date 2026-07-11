package com.edgareldy.domain.port.in;

import com.edgareldy.domain.model.product.Product;
import com.edgareldy.domain.port.in.command.CreateProductCommand;

/**
 * Inbound port: create a product within a category.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface CreateProductUseCase {

    Product createProduct(CreateProductCommand command);
}
