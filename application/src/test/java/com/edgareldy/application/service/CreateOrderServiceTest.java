package com.edgareldy.application.service;

import com.edgareldy.domain.event.OrderPlacedEvent;
import com.edgareldy.domain.exception.CustomerNotFoundException;
import com.edgareldy.domain.exception.ProductNotFoundException;
import com.edgareldy.domain.model.customer.Customer;
import com.edgareldy.domain.model.order.Order;
import com.edgareldy.domain.model.order.OrderStatus;
import com.edgareldy.domain.model.product.Product;
import com.edgareldy.domain.model.shared.Email;
import com.edgareldy.domain.model.shared.Money;
import com.edgareldy.domain.port.in.command.CreateOrderCommand;
import com.edgareldy.domain.port.in.command.CreateOrderLineCommand;
import com.edgareldy.domain.port.out.CustomerRepositoryPort;
import com.edgareldy.domain.port.out.DomainEventPublisherPort;
import com.edgareldy.domain.port.out.OrderRepositoryPort;
import com.edgareldy.domain.port.out.ProductRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Mockito tests for {@link CreateOrderService}: the customer/product existence checks, the
 * two-save flow around {@code Order.place()}, and event publication, with every outbound port
 * mocked.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@ExtendWith(MockitoExtension.class)
class CreateOrderServiceTest {

    private static final Money UNIT_PRICE = new Money(BigDecimal.valueOf(9.99), Currency.getInstance("EUR"));

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private CustomerRepositoryPort customerRepositoryPort;

    @Mock
    private DomainEventPublisherPort domainEventPublisherPort;

    @Test
    void creates_places_and_persists_an_order_then_publishes_its_event() {
        Customer customer = Customer.reconstitute(7L, "Jane", "Doe", "0102030405", new Email("jane@example.com"),
                "1 rue de Paris");
        when(customerRepositoryPort.findById(7L)).thenReturn(Optional.of(customer));
        Product product = Product.reconstitute(1L, 1L, "Mechanical keyboard", UNIT_PRICE);
        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product));
        // Every save() reconstructs a brand new Order instance, exactly like a real adapter
        // round-tripping through a JPA entity would: this only passes if createOrder() captures
        // the domain event before the second save(), not by reading it off whatever the second
        // save() happens to return.
        when(orderRepositoryPort.save(any(Order.class))).thenAnswer(invocation -> {
            Order input = invocation.getArgument(0);
            Long id = input.getId() != null ? input.getId() : 99L;
            return Order.reconstitute(id, input.getCustomerId(), input.getLines(), input.getStatus(),
                    input.getPlacedAt());
        });
        CreateOrderService service = new CreateOrderService(orderRepositoryPort, productRepositoryPort,
                customerRepositoryPort, domainEventPublisherPort);
        CreateOrderCommand command = new CreateOrderCommand(7L, List.of(new CreateOrderLineCommand(1L, 2)));

        Order result = service.createOrder(command);

        assertThat(result.getId()).isEqualTo(99L);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PLACED);
        verify(orderRepositoryPort, times(2)).save(any(Order.class));
        ArgumentCaptor<OrderPlacedEvent> eventCaptor = ArgumentCaptor.forClass(OrderPlacedEvent.class);
        verify(domainEventPublisherPort).publish(eventCaptor.capture());
        OrderPlacedEvent event = eventCaptor.getValue();
        assertThat(event.orderId()).isEqualTo(99L);
        assertThat(event.customerId()).isEqualTo(7L);
        assertThat(event.total().amount()).isEqualByComparingTo(BigDecimal.valueOf(19.98));
    }

    @Test
    void rejects_an_order_for_a_nonexistent_customer() {
        when(customerRepositoryPort.findById(7L)).thenReturn(Optional.empty());
        CreateOrderService service = new CreateOrderService(orderRepositoryPort, productRepositoryPort,
                customerRepositoryPort, domainEventPublisherPort);
        CreateOrderCommand command = new CreateOrderCommand(7L, List.of(new CreateOrderLineCommand(1L, 1)));

        assertThatThrownBy(() -> service.createOrder(command)).isInstanceOf(CustomerNotFoundException.class);

        verifyNoInteractions(orderRepositoryPort, productRepositoryPort, domainEventPublisherPort);
    }

    @Test
    void rejects_an_order_for_a_nonexistent_product() {
        Customer customer = Customer.reconstitute(7L, "Jane", "Doe", "0102030405", new Email("jane@example.com"),
                "1 rue de Paris");
        when(customerRepositoryPort.findById(7L)).thenReturn(Optional.of(customer));
        when(productRepositoryPort.findById(1L)).thenReturn(Optional.empty());
        CreateOrderService service = new CreateOrderService(orderRepositoryPort, productRepositoryPort,
                customerRepositoryPort, domainEventPublisherPort);
        CreateOrderCommand command = new CreateOrderCommand(7L, List.of(new CreateOrderLineCommand(1L, 1)));

        assertThatThrownBy(() -> service.createOrder(command)).isInstanceOf(ProductNotFoundException.class);

        verifyNoInteractions(orderRepositoryPort, domainEventPublisherPort);
    }
}
