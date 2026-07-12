package com.edgareldy.infrastructure.in.web.controller;

import com.edgareldy.domain.model.product.Product;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.CreateProductUseCase;
import com.edgareldy.domain.port.in.ListProductsUseCase;
import com.edgareldy.domain.port.in.command.ListProductsQuery;
import com.edgareldy.domain.port.in.command.PageQuery;
import com.edgareldy.infrastructure.in.web.dto.common.ApiResponse;
import com.edgareldy.infrastructure.in.web.dto.common.PageResponse;
import com.edgareldy.infrastructure.in.web.dto.product.ProductRequest;
import com.edgareldy.infrastructure.in.web.dto.product.ProductResponse;
import com.edgareldy.infrastructure.in.web.mapper.ProductWebMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * REST controller for {@code /api/v1/products}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final ListProductsUseCase listProductsUseCase;

    public ProductController(CreateProductUseCase createProductUseCase, ListProductsUseCase listProductsUseCase) {
        this.createProductUseCase = Objects.requireNonNull(createProductUseCase,
                "createProductUseCase must not be null");
        this.listProductsUseCase = Objects.requireNonNull(listProductsUseCase, "listProductsUseCase must not be null");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(@RequestBody ProductRequest request) {
        Product product = createProductUseCase.createProduct(ProductWebMapper.toCommand(request));
        ProductResponse response = ProductWebMapper.toResponse(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response, "Product created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> list(
            @RequestParam(required = false) Long categoryId, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResult<Product> result = listProductsUseCase.listProducts(
                new ListProductsQuery(categoryId, new PageQuery(page, size)));
        PageResponse<ProductResponse> response = PageResponse.from(result, ProductWebMapper::toResponse);
        return ResponseEntity.ok(ApiResponse.success(response, "Products retrieved successfully"));
    }
}
