package com.kkarimi.eventmanagement.attendees.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface AttendeeJpaRepository extends JpaRepository<AttendeeJpaEntity, UUID> {
}
