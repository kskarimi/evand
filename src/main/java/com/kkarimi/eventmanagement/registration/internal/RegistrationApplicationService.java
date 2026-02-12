package com.kkarimi.eventmanagement.registration.internal;

import com.kkarimi.eventmanagement.attendees.AttendeeDirectory;
import com.kkarimi.eventmanagement.eventhistory.TrackEventHistory;
import com.kkarimi.eventmanagement.events.EventCatalog;
import com.kkarimi.eventmanagement.metrics.MeasuredOperation;
import com.kkarimi.eventmanagement.notifications.NotificationGateway;
import com.kkarimi.eventmanagement.registration.DuplicateRegistrationException;
import com.kkarimi.eventmanagement.registration.Registration;
import com.kkarimi.eventmanagement.registration.RegistrationApplication;
import com.kkarimi.eventmanagement.registration.RegistrationCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
class RegistrationApplicationService implements RegistrationApplication {

    private final EventCatalog eventCatalog;
    private final AttendeeDirectory attendeeDirectory;
    private final NotificationGateway notificationGateway;
    private final RegistrationJpaRepository repository;
    private final RegistrationMapper mapper;

    @Override
    @Transactional
    @MeasuredOperation(
            timer = "registration.process.duration",
            successCounter = "registration.created.total",
            failureCounter = "registration.failed.total"
    )
    @TrackEventHistory(module = "registration", action = "create", entity = "registration")
    public Registration register(RegistrationCommand command) {
        eventCatalog.findById(command.eventId())
                .orElseThrow(() -> new NoSuchElementException("Event not found: " + command.eventId()));

        attendeeDirectory.findById(command.attendeeId())
                .orElseThrow(() -> new NoSuchElementException("Attendee not found: " + command.attendeeId()));

        if (repository.existsByEventIdAndAttendeeId(command.eventId(), command.attendeeId())) {
            throw new DuplicateRegistrationException(command.eventId(), command.attendeeId());
        }

        eventCatalog.reserveSeat(command.eventId());

        Registration registration = new Registration(
                null,
                command.eventId(),
                command.attendeeId(),
                Instant.now()
        );
        Registration persistedRegistration = mapper.toModel(repository.save(mapper.toEntity(registration)));
        notificationGateway.sendRegistrationConfirmation(persistedRegistration);
        return persistedRegistration;
    }

    @Override
    public Page<Registration> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toModel);
    }
}
