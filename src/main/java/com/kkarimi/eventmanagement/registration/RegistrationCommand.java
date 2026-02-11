package com.kkarimi.eventmanagement.registration;

import java.util.UUID;

public record RegistrationCommand(UUID eventId, UUID attendeeId) {
}
