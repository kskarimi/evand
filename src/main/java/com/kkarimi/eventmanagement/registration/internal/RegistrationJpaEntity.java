package com.kkarimi.eventmanagement.registration.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "registrations")
class RegistrationJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID eventId;

    @Column(nullable = false)
    private UUID attendeeId;

    @Column(nullable = false)
    private Instant registeredAt;

    protected RegistrationJpaEntity() {
    }

    RegistrationJpaEntity(UUID id, UUID eventId, UUID attendeeId, Instant registeredAt) {
        this.id = id;
        this.eventId = eventId;
        this.attendeeId = attendeeId;
        this.registeredAt = registeredAt;
    }

    UUID getId() {
        return id;
    }

    UUID getEventId() {
        return eventId;
    }

    UUID getAttendeeId() {
        return attendeeId;
    }

    Instant getRegisteredAt() {
        return registeredAt;
    }
}
