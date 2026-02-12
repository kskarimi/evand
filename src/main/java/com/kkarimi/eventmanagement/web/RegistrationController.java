package com.kkarimi.eventmanagement.web;

import com.kkarimi.eventmanagement.registration.Registration;
import com.kkarimi.eventmanagement.registration.RegistrationApplication;
import com.kkarimi.eventmanagement.registration.RegistrationCommand;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/api/registrations")
class RegistrationController {

    private final RegistrationApplication registrationApplication;

    RegistrationController(RegistrationApplication registrationApplication) {
        this.registrationApplication = registrationApplication;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Registration register(@Valid @RequestBody CreateRegistrationRequest request) {
        return registrationApplication.register(new RegistrationCommand(request.eventId(), request.attendeeId()));
    }

    @GetMapping
    PageResponse<Registration> list(@PageableDefault(size = 20, sort = "registeredAt") Pageable pageable) {
        return PageResponse.from(registrationApplication.findAll(pageable));
    }

    record CreateRegistrationRequest(@NotNull Long eventId, @NotNull Long attendeeId) {
    }
}
