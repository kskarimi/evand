package com.kkarimi.eventmanagement.attendees;

public class DuplicateAttendeeException extends RuntimeException {

    public DuplicateAttendeeException(String email) {
        super("Attendee with email already exists: " + email);
    }
}
