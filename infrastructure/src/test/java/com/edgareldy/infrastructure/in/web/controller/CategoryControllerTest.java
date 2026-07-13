package com.edgareldy.infrastructure.in.web.controller;

import com.edgareldy.domain.model.category.Category;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.CreateCategoryUseCase;
import com.edgareldy.domain.port.in.ListCategoriesUseCase;
import com.edgareldy.infrastructure.in.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@code @WebMvcTest} for {@link CategoryController}: the inbound ports are mocked, no Spring
 * Data/database context is loaded.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@WebMvcTest(CategoryController.class)
@Import({CategoryController.class, GlobalExceptionHandler.class})
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockitoBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Test
    void creates_a_category() throws Exception {
        when(createCategoryUseCase.createCategory(any())).thenReturn(Category.reconstitute(1L, "Peripherals"));

        mockMvc.perform(post("/api/v1/categories").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Peripherals\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Peripherals"));
    }

    @Test
    void lists_categories() throws Exception {
        PageResult<Category> page = new PageResult<>(List.of(Category.reconstitute(1L, "Peripherals")), 0, 20, 1, 1);
        when(listCategoriesUseCase.listCategories(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].name").value("Peripherals"))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    void rejects_a_malformed_request_body() throws Exception {
        mockMvc.perform(post("/api/v1/categories").contentType(MediaType.APPLICATION_JSON).content("not json"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(createCategoryUseCase);
    }

    @Test
    void rejects_an_unsupported_http_method() throws Exception {
        mockMvc.perform(delete("/api/v1/categories")).andExpect(status().isMethodNotAllowed());
    }
}
