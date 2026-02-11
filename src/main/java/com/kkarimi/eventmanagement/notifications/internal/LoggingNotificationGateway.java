package com.kkarimi.eventmanagement.notifications.internal;

import com.kkarimi.eventmanagement.notifications.NotificationGateway;
import com.kkarimi.eventmanagement.registration.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class LoggingNotificationGateway implements NotificationGateway {

    private static final Logger log = LoggerFactory.getLogger(LoggingNotificationGateway.class);

    @Override
    public void sendRegistrationConfirmation(Registration registration) {
        log.info("Confirmation sent for registration {}", registration.id());
    }
}
