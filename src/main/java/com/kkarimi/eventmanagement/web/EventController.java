package com.kkarimi.eventmanagement.web;

import com.kkarimi.eventmanagement.events.Event;
import com.kkarimi.eventmanagement.events.EventCatalog;
import com.kkarimi.eventmanagement.events.NewEventCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/events")
class EventController {

    private final EventCatalog eventCatalog;

    EventController(EventCatalog eventCatalog) {
        this.eventCatalog = eventCatalog;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Event create(@Valid @RequestBody CreateEventRequest request) {
        return eventCatalog.create(new NewEventCommand(request.title(), request.startsAt(), request.capacity()));
    }

    @GetMapping("/{eventId}")
    Event getById(@PathVariable Long eventId) {
        return eventCatalog.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("Event not found: " + eventId));
    }

    @GetMapping
    PageResponse<Event> list(@PageableDefault(size = 20, sort = "startsAt") Pageable pageable) {
        return PageResponse.from(eventCatalog.findAll(pageable));
    }

    record CreateEventRequest(
            @NotBlank String title,
            @NotNull @Future LocalDateTime startsAt,
            @Min(1) int capacity
    ) {
    }
}
