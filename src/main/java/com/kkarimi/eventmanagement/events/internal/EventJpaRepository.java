package com.kkarimi.eventmanagement.events.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;

interface EventJpaRepository extends JpaRepository<EventJpaEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from EventJpaEntity e where e.id = :id")
    Optional<EventJpaEntity> findByIdForUpdate(@Param("id") UUID id);
}
