package com.principal.backend.domain.port;

import com.principal.backend.domain.event.Event;

public interface EventPublisher {
    void publish(Event event);
}
