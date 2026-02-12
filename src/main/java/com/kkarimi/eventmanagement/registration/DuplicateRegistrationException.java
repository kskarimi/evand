package com.kkarimi.eventmanagement.registration;

import java.util.UUID;

public class DuplicateRegistrationException extends RuntimeException {

    public DuplicateRegistrationException(UUID eventId, UUID attendeeId) {
        super("Attendee is already registered for this event. eventId=" + eventId + ", attendeeId=" + attendeeId);
    }
}
