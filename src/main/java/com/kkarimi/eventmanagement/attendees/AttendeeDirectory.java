package com.kkarimi.eventmanagement.attendees;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendeeDirectory {

    Attendee register(NewAttendeeCommand command);

    Optional<Attendee> findById(UUID attendeeId);

    List<Attendee> findAll();
}
