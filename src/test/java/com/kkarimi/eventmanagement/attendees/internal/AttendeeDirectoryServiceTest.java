package com.kkarimi.eventmanagement.attendees.internal;

import com.kkarimi.eventmanagement.attendees.Attendee;
import com.kkarimi.eventmanagement.attendees.DuplicateAttendeeException;
import com.kkarimi.eventmanagement.attendees.NewAttendeeCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendeeDirectoryServiceTest {

    @Mock
    private AttendeeJpaRepository repository;

    @Mock
    private AttendeeMapper mapper;

    @InjectMocks
    private AttendeeDirectoryService service;

    @Test
    void registerShouldPersistAndReturnMappedModel() {
        NewAttendeeCommand command = new NewAttendeeCommand("Karim", "karim@example.com");
        Long id = 1L;
        AttendeeJpaEntity entity = new AttendeeJpaEntity(id, command.fullName(), command.email());
        Attendee model = new Attendee(id, command.fullName(), command.email());

        when(repository.existsByEmailIgnoreCase(command.email())).thenReturn(false);
        when(mapper.toEntity(any(NewAttendeeCommand.class))).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toModel(entity)).thenReturn(model);

        Attendee result = service.register(command);

        assertEquals(model, result);
    }

    @Test
    void registerShouldFailWhenEmailAlreadyExists() {
        NewAttendeeCommand command = new NewAttendeeCommand("Karim", "karim@example.com");
        when(repository.existsByEmailIgnoreCase(command.email())).thenReturn(true);

        assertThrows(DuplicateAttendeeException.class, () -> service.register(command));
    }

    @Test
    void registerShouldTranslateDataIntegrityViolationToDuplicateAttendeeException() {
        NewAttendeeCommand command = new NewAttendeeCommand("Karim", "karim@example.com");
        Long id = 1L;
        AttendeeJpaEntity entity = new AttendeeJpaEntity(id, command.fullName(), command.email());
        when(repository.existsByEmailIgnoreCase(command.email())).thenReturn(false);
        when(mapper.toEntity(any(NewAttendeeCommand.class))).thenReturn(entity);
        when(repository.save(entity)).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThrows(DuplicateAttendeeException.class, () -> service.register(command));
    }

    @Test
    void findByIdShouldReturnMappedValue() {
        Long id = 1L;
        AttendeeJpaEntity entity = new AttendeeJpaEntity(id, "Karim", "karim@example.com");
        Attendee model = new Attendee(id, "Karim", "karim@example.com");

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        Optional<Attendee> result = service.findById(id);

        assertEquals(Optional.of(model), result);
    }

    @Test
    void findAllShouldMapAllAttendees() {
        AttendeeJpaEntity e1 = new AttendeeJpaEntity(1L, "A", "a@example.com");
        AttendeeJpaEntity e2 = new AttendeeJpaEntity(2L, "B", "b@example.com");
        Attendee m1 = new Attendee(e1.getId(), "A", "a@example.com");
        Attendee m2 = new Attendee(e2.getId(), "B", "b@example.com");
        Pageable pageable = PageRequest.of(0, 20);
        Page<AttendeeJpaEntity> entityPage = new PageImpl<>(List.of(e1, e2), pageable, 2);

        when(repository.findAll(pageable)).thenReturn(entityPage);
        when(mapper.toModel(e1)).thenReturn(m1);
        when(mapper.toModel(e2)).thenReturn(m2);

        Page<Attendee> result = service.findAll(pageable);

        assertEquals(List.of(m1, m2), result.getContent());
    }
}
