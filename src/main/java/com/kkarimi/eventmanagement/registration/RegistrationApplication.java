package com.kkarimi.eventmanagement.registration;

import java.util.List;

public interface RegistrationApplication {

    Registration register(RegistrationCommand command);

    List<Registration> findAll();
}
