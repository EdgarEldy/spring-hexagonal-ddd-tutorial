package com.edgareldy.infrastructure.in.web.controller;

import com.edgareldy.domain.exception.EmptyOrderException;
import com.edgareldy.domain.model.order.Order;
import com.edgareldy.domain.model.order.OrderLine;
import com.edgareldy.domain.model.order.OrderStatus;
import com.edgareldy.domain.model.shared.Money;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.CreateOrderUseCase;
import com.edgareldy.domain.port.in.GetOrderUseCase;
import com.edgareldy.domain.port.in.ListOrdersUseCase;
import com.edgareldy.infrastructure.in.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@code @WebMvcTest} for {@link OrderController}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@WebMvcTest(OrderController.class)
@Import({OrderController.class, GlobalExceptionHandler.class})
class OrderControllerTest {

    private static final Currency EUR = Currency.getInstance("EUR");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateOrderUseCase createOrderUseCase;

    @MockitoBean
    private GetOrderUseCase getOrderUseCase;

    @MockitoBean
    private ListOrdersUseCase listOrdersUseCase;

    @Test
    void creates_an_order() throws Exception {
        Order order = Order.reconstitute(1L, 7L, List.of(lineOf(9.99, 2)), OrderStatus.PLACED, Instant.now());
        when(createOrderUseCase.createOrder(any())).thenReturn(order);

        mockMvc.perform(post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\": 7, \"lines\": [{\"productId\": 1, \"quantity\": 2}]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.status").value("PLACED"))
                .andExpect(jsonPath("$.data.lines[0].quantity").value(2));
    }

    @Test
    void translates_an_empty_order_into_422() throws Exception {
        when(createOrderUseCase.createOrder(any())).thenThrow(new EmptyOrderException());

        mockMvc.perform(post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\": 7, \"lines\": []}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void returns_the_order_when_found() throws Exception {
        Order order = Order.reconstitute(1L, 7L, List.of(lineOf(9.99, 2)), OrderStatus.PLACED, Instant.now());
        when(getOrderUseCase.getOrder(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/api/v1/orders/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.customerId").value(7));
    }

    @Test
    void returns_404_when_the_order_is_not_found() throws Exception {
        when(getOrderUseCase.getOrder(404L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/orders/404")).andExpect(status().isNotFound());
    }

    @Test
    void returns_400_for_a_non_numeric_id() throws Exception {
        mockMvc.perform(get("/api/v1/orders/not-a-number")).andExpect(status().isBadRequest());
    }

    @Test
    void returns_500_for_an_unexpected_failure() throws Exception {
        when(getOrderUseCase.getOrder(1L)).thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/api/v1/orders/1")).andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("an unexpected error occurred"));
    }

    @Test
    void lists_orders() throws Exception {
        Order order = Order.reconstitute(1L, 7L, List.of(lineOf(9.99, 2)), OrderStatus.PLACED, Instant.now());
        PageResult<Order> page = new PageResult<>(List.of(order), 0, 20, 1, 1);
        when(listOrdersUseCase.listOrders(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/orders")).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1));
    }

    private static OrderLine lineOf(double unitPrice, int quantity) {
        return OrderLine.of(1L, "Mechanical keyboard", quantity, new Money(BigDecimal.valueOf(unitPrice), EUR));
    }
}
