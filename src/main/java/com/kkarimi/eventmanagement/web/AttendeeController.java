package com.kkarimi.eventmanagement.web;

import com.kkarimi.eventmanagement.attendees.Attendee;
import com.kkarimi.eventmanagement.attendees.AttendeeDirectory;
import com.kkarimi.eventmanagement.attendees.NewAttendeeCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@RequestMapping("/api/attendees")
class AttendeeController {

    private final AttendeeDirectory attendeeDirectory;

    AttendeeController(AttendeeDirectory attendeeDirectory) {
        this.attendeeDirectory = attendeeDirectory;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Attendee create(@Valid @RequestBody CreateAttendeeRequest request) {
        return attendeeDirectory.register(new NewAttendeeCommand(request.fullName(), request.email()));
    }

    @GetMapping
    PageResponse<Attendee> list(@PageableDefault(size = 20, sort = "fullName") Pageable pageable) {
        return PageResponse.from(attendeeDirectory.findAll(pageable));
    }

    record CreateAttendeeRequest(@NotBlank String fullName, @NotBlank @Email String email) {
    }
}
