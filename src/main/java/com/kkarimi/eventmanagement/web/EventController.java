package com.kkarimi.eventmanagement.web;

import com.kkarimi.eventmanagement.events.Event;
import com.kkarimi.eventmanagement.events.EventCatalog;
import com.kkarimi.eventmanagement.events.NewEventCommand;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
class EventController {

    private final EventCatalog eventCatalog;

    EventController(EventCatalog eventCatalog) {
        this.eventCatalog = eventCatalog;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Event create(@RequestBody CreateEventRequest request) {
        return eventCatalog.create(new NewEventCommand(request.title(), request.startsAt(), request.capacity()));
    }

    @GetMapping("/{eventId}")
    Event getById(@PathVariable UUID eventId) {
        return eventCatalog.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    @GetMapping
    List<Event> list() {
        return eventCatalog.findAll();
    }

    record CreateEventRequest(
            @NotBlank String title,
            @NotNull @Future LocalDateTime startsAt,
            @Min(1) int capacity
    ) {
    }
}
