package com.kkarimi.eventmanagement.registration.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface RegistrationJpaRepository extends JpaRepository<RegistrationJpaEntity, UUID> {
}
