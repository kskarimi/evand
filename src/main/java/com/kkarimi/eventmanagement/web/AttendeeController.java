package com.kkarimi.eventmanagement.web;

import com.kkarimi.eventmanagement.attendees.Attendee;
import com.kkarimi.eventmanagement.attendees.AttendeeDirectory;
import com.kkarimi.eventmanagement.attendees.NewAttendeeCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attendees")
class AttendeeController {

    private final AttendeeDirectory attendeeDirectory;

    AttendeeController(AttendeeDirectory attendeeDirectory) {
        this.attendeeDirectory = attendeeDirectory;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Attendee create(@RequestBody CreateAttendeeRequest request) {
        return attendeeDirectory.register(new NewAttendeeCommand(request.fullName(), request.email()));
    }

    @GetMapping
    List<Attendee> list() {
        return attendeeDirectory.findAll();
    }

    record CreateAttendeeRequest(@NotBlank String fullName, @NotBlank @Email String email) {
    }
}
