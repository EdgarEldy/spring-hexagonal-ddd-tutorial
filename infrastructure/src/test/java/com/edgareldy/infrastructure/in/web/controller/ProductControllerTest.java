package com.edgareldy.infrastructure.in.web.controller;

import com.edgareldy.domain.exception.CategoryNotFoundException;
import com.edgareldy.domain.model.product.Product;
import com.edgareldy.domain.model.shared.Money;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.CreateProductUseCase;
import com.edgareldy.domain.port.in.ListProductsUseCase;
import com.edgareldy.infrastructure.in.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@code @WebMvcTest} for {@link ProductController}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@WebMvcTest(ProductController.class)
@Import({ProductController.class, GlobalExceptionHandler.class})
class ProductControllerTest {

    private static final Money UNIT_PRICE = new Money(BigDecimal.valueOf(9.99), Currency.getInstance("EUR"));

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateProductUseCase createProductUseCase;

    @MockitoBean
    private ListProductsUseCase listProductsUseCase;

    @Test
    void creates_a_product() throws Exception {
        when(createProductUseCase.createProduct(any())).thenReturn(
                Product.reconstitute(1L, 2L, "Mechanical keyboard", UNIT_PRICE));

        mockMvc.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryId\": 2, \"name\": \"Mechanical keyboard\", "
                                + "\"unitPrice\": {\"amount\": 9.99, \"currency\": \"EUR\"}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Mechanical keyboard"))
                .andExpect(jsonPath("$.data.unitPrice.currency").value("EUR"));
    }

    @Test
    void translates_a_missing_category_into_422() throws Exception {
        when(createProductUseCase.createProduct(any())).thenThrow(new CategoryNotFoundException(2L));

        mockMvc.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryId\": 2, \"name\": \"Mechanical keyboard\", "
                                + "\"unitPrice\": {\"amount\": 9.99, \"currency\": \"EUR\"}}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void lists_products_filtered_by_category() throws Exception {
        PageResult<Product> page = new PageResult<>(
                List.of(Product.reconstitute(1L, 2L, "Mechanical keyboard", UNIT_PRICE)), 0, 20, 1, 1);
        when(listProductsUseCase.listProducts(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/products").param("categoryId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].categoryId").value(2));
    }
}
