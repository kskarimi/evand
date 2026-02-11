package com.kkarimi.eventmanagement.events.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "events")
class EventJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int reservedSeats;

    @Version
    private Long version;

    protected EventJpaEntity() {
    }

    EventJpaEntity(UUID id, String title, LocalDateTime startsAt, int capacity, int reservedSeats) {
        this.id = id;
        this.title = title;
        this.startsAt = startsAt;
        this.capacity = capacity;
        this.reservedSeats = reservedSeats;
    }

    UUID getId() {
        return id;
    }

    String getTitle() {
        return title;
    }

    LocalDateTime getStartsAt() {
        return startsAt;
    }

    int getCapacity() {
        return capacity;
    }

    int getReservedSeats() {
        return reservedSeats;
    }

    void setReservedSeats(int reservedSeats) {
        this.reservedSeats = reservedSeats;
    }
}
