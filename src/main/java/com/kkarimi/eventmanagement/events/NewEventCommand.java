package com.kkarimi.eventmanagement.events;

import java.time.LocalDateTime;

public record NewEventCommand(String title, LocalDateTime startsAt, int capacity) {
}
