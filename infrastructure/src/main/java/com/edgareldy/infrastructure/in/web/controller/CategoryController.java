package com.edgareldy.infrastructure.in.web.controller;

import com.edgareldy.domain.model.category.Category;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.CreateCategoryUseCase;
import com.edgareldy.domain.port.in.ListCategoriesUseCase;
import com.edgareldy.domain.port.in.command.PageQuery;
import com.edgareldy.infrastructure.in.web.dto.category.CategoryRequest;
import com.edgareldy.infrastructure.in.web.dto.category.CategoryResponse;
import com.edgareldy.infrastructure.in.web.dto.common.ApiResponse;
import com.edgareldy.infrastructure.in.web.dto.common.PageResponse;
import com.edgareldy.infrastructure.in.web.mapper.CategoryWebMapper;
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
 * REST controller for {@code /api/v1/categories}. Injects only inbound ports
 * ({@code domain.port.in}), never a concrete {@code application} service or a repository/
 * adapter directly.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase,
            ListCategoriesUseCase listCategoriesUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase,
                "createCategoryUseCase must not be null");
        this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase,
                "listCategoriesUseCase must not be null");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@RequestBody CategoryRequest request) {
        Category category = createCategoryUseCase.createCategory(CategoryWebMapper.toCommand(request));
        CategoryResponse response = CategoryWebMapper.toResponse(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response, "Category created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CategoryResponse>>> list(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        PageResult<Category> result = listCategoriesUseCase.listCategories(new PageQuery(page, size));
        PageResponse<CategoryResponse> response = PageResponse.from(result, CategoryWebMapper::toResponse);
        return ResponseEntity.ok(ApiResponse.success(response, "Categories retrieved successfully"));
    }
}
