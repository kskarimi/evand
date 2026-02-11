package com.kkarimi.eventmanagement.events;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventCatalog {

    Event create(NewEventCommand command);

    Optional<Event> findById(UUID eventId);

    Event reserveSeat(UUID eventId);

    List<Event> findAll();
}
