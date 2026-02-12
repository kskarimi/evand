package com.kkarimi.eventmanagement.registration.internal;

import org.springframework.data.jpa.repository.JpaRepository;

interface RegistrationJpaRepository extends JpaRepository<RegistrationJpaEntity, Long> {

    boolean existsByEventIdAndAttendeeId(Long eventId, Long attendeeId);
}
