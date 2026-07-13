package com.edgareldy.bootstrap.event;

import com.edgareldy.domain.event.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Reacts to {@link OrderPlacedEvent} only after the surrounding transaction commits, per the
 * README's domain event contract: {@code @TransactionalEventListener(AFTER_COMMIT)} rather than
 * a plain {@code @EventListener}, which would fire synchronously inside the still-open
 * transaction and could react to an order that later rolls back. No real side effect is wired
 * yet (no use case in this project needs one), this exists to demonstrate and empirically prove
 * the timing guarantee itself.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@Component
public class OrderPlacedEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderPlacedEventListener.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderPlaced(OrderPlacedEvent event) {
        log.info("Order {} placed for customer {}, total {}", event.orderId(), event.customerId(), event.total());
    }
}
