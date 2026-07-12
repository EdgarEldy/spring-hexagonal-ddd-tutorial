package com.edgareldy.infrastructure.out.event;

import com.edgareldy.domain.event.DomainEvent;
import com.edgareldy.domain.port.out.DomainEventPublisherPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Implements {@link DomainEventPublisherPort} by delegating to Spring's
 * {@link ApplicationEventPublisher}. Whether listeners react immediately or only after the
 * surrounding transaction commits (e.g. via {@code @TransactionalEventListener(AFTER_COMMIT)})
 * is entirely up to whoever listens; this adapter only forwards the event.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@Component
public class SpringDomainEventPublisherAdapter implements DomainEventPublisherPort {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringDomainEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = Objects.requireNonNull(applicationEventPublisher,
                "applicationEventPublisher must not be null");
    }

    @Override
    public void publish(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
