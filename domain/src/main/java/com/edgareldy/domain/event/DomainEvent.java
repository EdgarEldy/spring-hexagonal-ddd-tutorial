package com.edgareldy.domain.event;

import java.time.Instant;

/**
 * Marker interface for a statement that something meaningful has already happened in the
 * domain. Implementations are named in the past tense (e.g. {@code OrderPlacedEvent}), raised
 * by the aggregate that owns the invariant being satisfied, and published by infrastructure,
 * typically after the surrounding transaction commits.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface DomainEvent {

    Instant occurredOn();
}
