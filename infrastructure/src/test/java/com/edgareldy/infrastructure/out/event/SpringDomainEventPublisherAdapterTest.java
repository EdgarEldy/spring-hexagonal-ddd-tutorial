package com.edgareldy.infrastructure.out.event;

import com.edgareldy.domain.event.DomainEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link SpringDomainEventPublisherAdapter}: verifies the event is forwarded to
 * {@link ApplicationEventPublisher#publishEvent(Object)} unchanged.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@ExtendWith(MockitoExtension.class)
class SpringDomainEventPublisherAdapterTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private DomainEvent event;

    @Test
    void delegates_the_event_to_the_application_event_publisher() {
        SpringDomainEventPublisherAdapter adapter = new SpringDomainEventPublisherAdapter(applicationEventPublisher);

        adapter.publish(event);

        verify(applicationEventPublisher).publishEvent(event);
    }
}
