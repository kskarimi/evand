package com.kkarimi.eventmanagement.registration;

import java.time.Instant;

public record Registration(Long id, Long eventId, Long attendeeId, Instant registeredAt) {
}
