package com.kkarimi.eventmanagement.attendees;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AttendeeDirectory {

    Attendee register(NewAttendeeCommand command);

    Optional<Attendee> findById(Long attendeeId);

    Page<Attendee> findAll(Pageable pageable);
}
