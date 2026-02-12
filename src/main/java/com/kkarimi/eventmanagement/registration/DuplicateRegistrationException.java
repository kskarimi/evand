package com.kkarimi.eventmanagement.registration;

public class DuplicateRegistrationException extends RuntimeException {

    public DuplicateRegistrationException(Long eventId, Long attendeeId) {
        super("Attendee is already registered for this event. eventId=" + eventId + ", attendeeId=" + attendeeId);
    }
}
