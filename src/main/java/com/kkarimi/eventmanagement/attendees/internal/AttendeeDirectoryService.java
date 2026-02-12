package com.kkarimi.eventmanagement.attendees.internal;

import com.kkarimi.eventmanagement.eventhistory.TrackEventHistory;
import com.kkarimi.eventmanagement.attendees.Attendee;
import com.kkarimi.eventmanagement.attendees.AttendeeDirectory;
import com.kkarimi.eventmanagement.attendees.DuplicateAttendeeException;
import com.kkarimi.eventmanagement.attendees.NewAttendeeCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class AttendeeDirectoryService implements AttendeeDirectory {

    private final AttendeeJpaRepository repository;
    private final AttendeeMapper mapper;

    @Override
    @Transactional
    @TrackEventHistory(module = "attendees", action = "register", entity = "attendee")
    public Attendee register(NewAttendeeCommand command) {
        if (repository.existsByEmailIgnoreCase(command.email())) {
            throw new DuplicateAttendeeException(command.email());
        }
        UUID id = UUID.randomUUID();
        AttendeeJpaEntity entity = mapper.toEntity(id, command);
        try {
            return mapper.toModel(repository.save(entity));
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateAttendeeException(command.email());
        }
    }

    @Override
    public Optional<Attendee> findById(UUID attendeeId) {
        return repository.findById(attendeeId).map(mapper::toModel);
    }

    @Override
    public Page<Attendee> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toModel);
    }
}
