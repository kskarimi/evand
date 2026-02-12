package com.kkarimi.eventmanagement.events;

import java.time.LocalDateTime;

public record Event(Long id, String title, LocalDateTime startsAt, int capacity, int reservedSeats) {

    public boolean hasAvailableSeats() {
        return reservedSeats < capacity;
    }
}
