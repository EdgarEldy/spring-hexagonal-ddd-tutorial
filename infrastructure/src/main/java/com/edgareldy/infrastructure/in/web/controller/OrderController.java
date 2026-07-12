package com.edgareldy.infrastructure.in.web.controller;

import com.edgareldy.domain.model.order.Order;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.CreateOrderUseCase;
import com.edgareldy.domain.port.in.GetOrderUseCase;
import com.edgareldy.domain.port.in.ListOrdersUseCase;
import com.edgareldy.domain.port.in.command.PageQuery;
import com.edgareldy.infrastructure.in.web.dto.common.ApiResponse;
import com.edgareldy.infrastructure.in.web.dto.common.PageResponse;
import com.edgareldy.infrastructure.in.web.dto.order.OrderRequest;
import com.edgareldy.infrastructure.in.web.dto.order.OrderResponse;
import com.edgareldy.infrastructure.in.web.exception.ResourceNotFoundException;
import com.edgareldy.infrastructure.in.web.mapper.OrderWebMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * REST controller for {@code /api/v1/orders}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final ListOrdersUseCase listOrdersUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase, GetOrderUseCase getOrderUseCase,
            ListOrdersUseCase listOrdersUseCase) {
        this.createOrderUseCase = Objects.requireNonNull(createOrderUseCase, "createOrderUseCase must not be null");
        this.getOrderUseCase = Objects.requireNonNull(getOrderUseCase, "getOrderUseCase must not be null");
        this.listOrdersUseCase = Objects.requireNonNull(listOrdersUseCase, "listOrdersUseCase must not be null");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(@RequestBody OrderRequest request) {
        Order order = createOrderUseCase.createOrder(OrderWebMapper.toCommand(request));
        OrderResponse response = OrderWebMapper.toResponse(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response, "Order placed successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> get(@PathVariable Long id) {
        Order order = getOrderUseCase.getOrder(id)
                .orElseThrow(() -> new ResourceNotFoundException("no order found with id " + id));
        return ResponseEntity.ok(ApiResponse.success(OrderWebMapper.toResponse(order), "Order retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> list(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResult<Order> result = listOrdersUseCase.listOrders(new PageQuery(page, size));
        PageResponse<OrderResponse> response = PageResponse.from(result, OrderWebMapper::toResponse);
        return ResponseEntity.ok(ApiResponse.success(response, "Orders retrieved successfully"));
    }
}
