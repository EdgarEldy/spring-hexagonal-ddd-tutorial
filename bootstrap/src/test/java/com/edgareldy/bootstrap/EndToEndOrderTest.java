package com.edgareldy.bootstrap;

import com.edgareldy.bootstrap.event.OrderPlacedEventListener;
import com.edgareldy.domain.port.in.CreateOrderUseCase;
import com.edgareldy.domain.port.in.command.CreateOrderCommand;
import com.edgareldy.domain.port.in.command.CreateOrderLineCommand;
import com.edgareldy.domain.port.out.OrderRepositoryPort;
import com.edgareldy.infrastructure.out.persistence.repository.OrderJpaRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end test exercising the full chain controller -> use case -> domain -> adapter ->
 * database against a real Postgres container, and empirically proving the two guarantees this
 * project's own review process flagged as needing proof rather than a comment: an
 * {@code OrderPlacedEvent} is only delivered after the surrounding transaction commits, and a
 * failure partway through {@code CreateOrderService.createOrder()} rolls back everything, not
 * just the failing step.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
class EndToEndOrderTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private CreateOrderUseCase createOrderUseCase;

    @MockitoSpyBean
    private OrderPlacedEventListener orderPlacedEventListener;

    @MockitoSpyBean
    private OrderRepositoryPort orderRepositoryPort;

    @Test
    void places_an_order_through_the_full_chain_and_publishes_the_event_after_commit() throws Exception {
        Long categoryId = createCategory("Peripherals");
        Long productId = createProduct(categoryId, "Mechanical keyboard");
        Long customerId = createCustomer("jane.success@example.com");

        String orderRequest = ("{\"customerId\": %d, \"lines\": [{\"productId\": %d, \"quantity\": 2}]}")
                .formatted(customerId, productId);

        String createResponse = mockMvc.perform(
                        post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON).content(orderRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.status").value("PLACED"))
                .andReturn().getResponse().getContentAsString();
        Long orderId = ((Number) JsonPath.read(createResponse, "$.data.id")).longValue();

        mockMvc.perform(get("/api/v1/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.customerId").value(customerId))
                .andExpect(jsonPath("$.data.lines[0].quantity").value(2));

        verify(orderPlacedEventListener).onOrderPlaced(any());
    }

    @Test
    void a_failure_on_the_second_save_rolls_back_everything_and_never_publishes_the_event() throws Exception {
        Long categoryId = createCategory("Furniture");
        Long productId = createProduct(categoryId, "Standing desk");
        Long customerId = createCustomer("jane.rollback@example.com");

        AtomicInteger saveCallCount = new AtomicInteger();
        doAnswer(invocation -> {
            if (saveCallCount.incrementAndGet() == 2) {
                throw new RuntimeException("simulated failure on the second save");
            }
            return invocation.callRealMethod();
        }).when(orderRepositoryPort).save(any());

        CreateOrderCommand command = new CreateOrderCommand(customerId,
                List.of(new CreateOrderLineCommand(productId, 1)));

        assertThatThrownBy(() -> createOrderUseCase.createOrder(command)).isInstanceOf(RuntimeException.class);

        assertThat(orderJpaRepository.count()).isZero();
        verify(orderPlacedEventListener, never()).onOrderPlaced(any());
    }

    private Long createCategory(String name) throws Exception {
        String response = mockMvc.perform(post("/api/v1/categories").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"" + name + "\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(response, "$.data.id")).longValue();
    }

    private Long createProduct(Long categoryId, String name) throws Exception {
        String request = ("{\"categoryId\": %d, \"name\": \"%s\", \"unitPrice\": {\"amount\": 9.99, "
                + "\"currency\": \"EUR\"}}").formatted(categoryId, name);
        String response = mockMvc.perform(
                        post("/api/v1/products").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(response, "$.data.id")).longValue();
    }

    private Long createCustomer(String email) throws Exception {
        String request = ("{\"firstName\": \"Jane\", \"lastName\": \"Doe\", \"telephone\": \"0102030405\", "
                + "\"email\": \"%s\", \"address\": \"1 rue de Paris\"}").formatted(email);
        String response = mockMvc.perform(
                        post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(response, "$.data.id")).longValue();
    }
}
