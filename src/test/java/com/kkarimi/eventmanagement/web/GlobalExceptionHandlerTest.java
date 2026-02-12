package com.kkarimi.eventmanagement.web;

import com.kkarimi.eventmanagement.registration.DuplicateRegistrationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldMapDuplicateRegistrationToConflict() {
        DuplicateRegistrationException exception = new DuplicateRegistrationException(1L, 2L);

        ResponseEntity<ApiErrorResponse> response = handler.handleDuplicateRegistration(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("DUPLICATE_REGISTRATION", response.getBody().error());
    }
}
