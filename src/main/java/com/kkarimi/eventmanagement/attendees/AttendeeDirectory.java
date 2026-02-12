package com.kkarimi.eventmanagement.attendees;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AttendeeDirectory {

    Attendee register(NewAttendeeCommand command);

    Optional<Attendee> findById(UUID attendeeId);

    Page<Attendee> findAll(Pageable pageable);
}
