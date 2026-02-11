package com.kkarimi.eventmanagement.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record Event(UUID id, String title, LocalDateTime startsAt, int capacity, int reservedSeats) {

    public boolean hasAvailableSeats() {
        return reservedSeats < capacity;
    }
}
