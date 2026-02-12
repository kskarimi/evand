package com.kkarimi.eventmanagement.web;

import com.kkarimi.eventmanagement.registration.Registration;
import com.kkarimi.eventmanagement.registration.RegistrationApplication;
import com.kkarimi.eventmanagement.registration.RegistrationCommand;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/registrations")
class RegistrationController {

    private final RegistrationApplication registrationApplication;

    RegistrationController(RegistrationApplication registrationApplication) {
        this.registrationApplication = registrationApplication;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Registration register(@RequestBody CreateRegistrationRequest request) {
        try {
            return registrationApplication.register(new RegistrationCommand(request.eventId(), request.attendeeId()));
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage(), ex);
        }
    }

    @GetMapping
    PageResponse<Registration> list(@PageableDefault(size = 20, sort = "registeredAt") Pageable pageable) {
        return PageResponse.from(registrationApplication.findAll(pageable));
    }

    record CreateRegistrationRequest(@NotNull UUID eventId, @NotNull UUID attendeeId) {
    }
}
