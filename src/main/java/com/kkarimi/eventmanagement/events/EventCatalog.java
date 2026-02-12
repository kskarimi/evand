package com.kkarimi.eventmanagement.events;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventCatalog {

    Event create(NewEventCommand command);

    Optional<Event> findById(UUID eventId);

    Event reserveSeat(UUID eventId);

    Page<Event> findAll(Pageable pageable);
}
