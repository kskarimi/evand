package com.kkarimi.eventmanagement.attendees.internal;

import org.springframework.data.jpa.repository.JpaRepository;

interface AttendeeJpaRepository extends JpaRepository<AttendeeJpaEntity, Long> {

    boolean existsByEmailIgnoreCase(String email);
}
