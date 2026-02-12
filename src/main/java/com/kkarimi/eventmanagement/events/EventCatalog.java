package com.kkarimi.eventmanagement.events;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventCatalog {

    Event create(NewEventCommand command);

    Optional<Event> findById(Long eventId);

    Event reserveSeat(Long eventId);

    Page<Event> findAll(Pageable pageable);
}
