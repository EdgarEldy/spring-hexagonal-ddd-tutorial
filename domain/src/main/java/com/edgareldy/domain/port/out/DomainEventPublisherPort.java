package com.edgareldy.domain.port.out;

import com.edgareldy.domain.event.DomainEvent;

/**
 * Outbound port: publish a domain event. The domain does not know or care who is listening;
 * infrastructure is responsible for the actual delivery mechanism.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface DomainEventPublisherPort {

    void publish(DomainEvent event);
}
