package com.kkarimi.eventmanagement.registration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RegistrationApplication {

    Registration register(RegistrationCommand command);

    Page<Registration> findAll(Pageable pageable);
}
