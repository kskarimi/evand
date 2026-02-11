package com.kkarimi.eventmanagement.events.internal;

import com.kkarimi.eventmanagement.events.Event;
import com.kkarimi.eventmanagement.events.EventCatalog;
import com.kkarimi.eventmanagement.events.NewEventCommand;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
class EventCatalogService implements EventCatalog {

    private final EventJpaRepository repository;

    EventCatalogService(EventJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"eventById", "eventList"}, allEntries = true)
    public Event create(NewEventCommand command) {
        if (command.capacity() <= 0) {
            throw new IllegalArgumentException("Event capacity must be greater than zero");
        }
        UUID id = UUID.randomUUID();
        EventJpaEntity entity = new EventJpaEntity(
                id,
                command.title(),
                command.startsAt(),
                command.capacity(),
                0
        );
        return toModel(repository.save(entity));
    }

    @Override
    @Cacheable(cacheNames = "eventById", key = "#eventId")
    public Optional<Event> findById(UUID eventId) {
        return repository.findById(eventId).map(this::toModel);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"eventById", "eventList"}, allEntries = true)
    public Event reserveSeat(UUID eventId) {
        EventJpaEntity entity = repository.findByIdForUpdate(eventId)
                .orElseThrow(() -> new NoSuchElementException("Event not found: " + eventId));

        if (entity.getReservedSeats() >= entity.getCapacity()) {
            throw new IllegalStateException("No seat available for event: " + eventId);
        }

        entity.setReservedSeats(entity.getReservedSeats() + 1);
        return toModel(entity);
    }

    @Override
    @Cacheable(cacheNames = "eventList")
    public List<Event> findAll() {
        return repository.findAll().stream()
                .map(this::toModel)
                .sorted(Comparator.comparing(Event::startsAt))
                .toList();
    }

    private Event toModel(EventJpaEntity entity) {
        return new Event(
                entity.getId(),
                entity.getTitle(),
                entity.getStartsAt(),
                entity.getCapacity(),
                entity.getReservedSeats()
        );
    }
}
