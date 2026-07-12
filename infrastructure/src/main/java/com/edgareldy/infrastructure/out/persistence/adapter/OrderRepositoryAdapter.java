package com.edgareldy.infrastructure.out.persistence.adapter;

import com.edgareldy.domain.model.order.Order;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.command.PageQuery;
import com.edgareldy.domain.port.out.OrderRepositoryPort;
import com.edgareldy.infrastructure.out.persistence.entity.OrderEntity;
import com.edgareldy.infrastructure.out.persistence.mapper.OrderPersistenceMapper;
import com.edgareldy.infrastructure.out.persistence.mapper.PageMapper;
import com.edgareldy.infrastructure.out.persistence.repository.OrderJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

/**
 * Implements {@link OrderRepositoryPort} via {@link OrderJpaRepository}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@Repository
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryAdapter(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = Objects.requireNonNull(orderJpaRepository, "orderJpaRepository must not be null");
    }

    /**
     * A transient order (no id yet) is mapped whole, lines included, and inserted. An
     * already-persisted order is instead loaded back first and only its status/total/placedAt
     * are copied onto the managed entity: this project has no use case that mutates an order's
     * lines after creation, and mapping a fresh, id-less lines collection here would make
     * Hibernate delete and re-insert every line on every save for no reason.
     */
    @Override
    public Order save(Order order) {
        OrderEntity saved;
        if (order.getId() == null) {
            saved = orderJpaRepository.save(OrderPersistenceMapper.toNewEntity(order));
        } else {
            OrderEntity entity = orderJpaRepository.findById(order.getId())
                    .orElseThrow(() -> new IllegalStateException("order " + order.getId() + " not found for update"));
            OrderPersistenceMapper.applyStatusChange(entity, order);
            saved = orderJpaRepository.save(entity);
        }
        return OrderPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id).map(OrderPersistenceMapper::toDomain);
    }

    @Override
    public PageResult<Order> findAll(PageQuery query) {
        return PageMapper.toPageResult(orderJpaRepository.findAll(PageMapper.toPageable(query)),
                OrderPersistenceMapper::toDomain);
    }
}
