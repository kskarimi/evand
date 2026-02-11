package com.kkarimi.eventmanagement.registration;

import java.time.Instant;
import java.util.UUID;

public record Registration(UUID id, UUID eventId, UUID attendeeId, Instant registeredAt) {
}
