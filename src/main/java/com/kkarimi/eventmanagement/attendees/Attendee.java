package com.kkarimi.eventmanagement.attendees;

import java.util.UUID;

public record Attendee(UUID id, String fullName, String email) {
}
