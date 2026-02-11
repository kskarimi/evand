package com.kkarimi.eventmanagement.notifications;

import com.kkarimi.eventmanagement.registration.Registration;

public interface NotificationGateway {

    void sendRegistrationConfirmation(Registration registration);
}
