package com.edgareldy.infrastructure.in.web.controller;

import com.edgareldy.domain.model.customer.Customer;
import com.edgareldy.domain.model.shared.Email;
import com.edgareldy.domain.port.in.CreateCustomerUseCase;
import com.edgareldy.domain.port.in.GetCustomerUseCase;
import com.edgareldy.infrastructure.in.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@code @WebMvcTest} for {@link CustomerController}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@WebMvcTest(CustomerController.class)
@Import({CustomerController.class, GlobalExceptionHandler.class})
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateCustomerUseCase createCustomerUseCase;

    @MockitoBean
    private GetCustomerUseCase getCustomerUseCase;

    @Test
    void creates_a_customer() throws Exception {
        when(createCustomerUseCase.createCustomer(any())).thenReturn(
                Customer.reconstitute(1L, "Jane", "Doe", "0102030405", new Email("jane@example.com"),
                        "1 rue de Paris"));

        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Jane\", \"lastName\": \"Doe\", \"telephone\": \"0102030405\", "
                                + "\"email\": \"jane@example.com\", \"address\": \"1 rue de Paris\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value("jane@example.com"));
    }

    @Test
    void returns_the_customer_when_found() throws Exception {
        when(getCustomerUseCase.getCustomer(1L)).thenReturn(Optional.of(
                Customer.reconstitute(1L, "Jane", "Doe", "0102030405", new Email("jane@example.com"),
                        "1 rue de Paris")));

        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value("Jane"));
    }

    @Test
    void returns_404_when_the_customer_is_not_found() throws Exception {
        when(getCustomerUseCase.getCustomer(404L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/customers/404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void translates_a_malformed_email_into_400() throws Exception {
        when(createCustomerUseCase.createCustomer(any())).thenThrow(
                new IllegalArgumentException("invalid email format: not-an-email"));

        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Jane\", \"lastName\": \"Doe\", \"telephone\": \"0102030405\", "
                                + "\"email\": \"not-an-email\", \"address\": \"1 rue de Paris\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
