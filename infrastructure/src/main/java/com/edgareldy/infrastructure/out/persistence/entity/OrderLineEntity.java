package com.edgareldy.infrastructure.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * JPA entity for the {@code order_lines} table. Unlike {@code ProductEntity.categoryId}, the
 * link back to {@link OrderEntity} is a real {@code @ManyToOne}, since it is the owning side of
 * a true composition relation ({@code Order.lines}, cascaded and orphan-removed from
 * {@link OrderEntity}), not a mere reference. {@code productId} stays a plain column: this line
 * carries its own snapshot of the product's name and price, it never navigates back to
 * {@code ProductEntity}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@Entity
@Table(name = "order_lines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPriceAmount;

    @Column(name = "unit_price_currency", nullable = false, length = 3)
    private String unitPriceCurrency;
}
