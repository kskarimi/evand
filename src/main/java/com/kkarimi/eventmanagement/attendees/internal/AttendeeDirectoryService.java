package com.kkarimi.eventmanagement.attendees.internal;

import com.kkarimi.eventmanagement.attendees.Attendee;
import com.kkarimi.eventmanagement.attendees.AttendeeDirectory;
import com.kkarimi.eventmanagement.attendees.NewAttendeeCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
class AttendeeDirectoryService implements AttendeeDirectory {

    private final AttendeeJpaRepository repository;

    AttendeeDirectoryService(AttendeeJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Attendee register(NewAttendeeCommand command) {
        UUID id = UUID.randomUUID();
        AttendeeJpaEntity entity = new AttendeeJpaEntity(id, command.fullName(), command.email());
        return toModel(repository.save(entity));
    }

    @Override
    public Optional<Attendee> findById(UUID attendeeId) {
        return repository.findById(attendeeId).map(this::toModel);
    }

    @Override
    public List<Attendee> findAll() {
        return repository.findAll().stream().map(this::toModel).toList();
    }

    private Attendee toModel(AttendeeJpaEntity entity) {
        return new Attendee(entity.getId(), entity.getFullName(), entity.getEmail());
    }
}
