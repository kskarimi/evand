# API Documentation

Base URL: `http://localhost:8080`

## Quick Business Flow
1. Create an event
2. Create an attendee
3. Register attendee in event

## cURL Examples

### 1. Create Event
```bash
curl -X POST 'http://localhost:8080/api/events' \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "Spring Modulith Workshop",
    "startsAt": "2026-03-01T10:00:00",
    "capacity": 100
  }'
```

### 2. List Events
```bash
curl 'http://localhost:8080/api/events'
```

### 3. Get Event by ID
```bash
curl 'http://localhost:8080/api/events/{eventId}'
```

### 4. Create Attendee
```bash
curl -X POST 'http://localhost:8080/api/attendees' \
  -H 'Content-Type: application/json' \
  -d '{
    "fullName": "Karim Karimi",
    "email": "karim@example.com"
  }'
```

### 5. List Attendees
```bash
curl 'http://localhost:8080/api/attendees'
```

### 6. Register Attendee in Event
```bash
curl -X POST 'http://localhost:8080/api/registrations' \
  -H 'Content-Type: application/json' \
  -d '{
    "eventId": "{eventId}",
    "attendeeId": "{attendeeId}"
  }'
```

### 7. List Registrations
```bash
curl 'http://localhost:8080/api/registrations'
```

## HTTP Statuses
- `201 Created`: successful create/register operations.
- `200 OK`: successful read/list operations.
- `404 Not Found`: event or attendee not found.
- `409 Conflict`: event has no remaining seat.
- `429 Too Many Requests`: customer API rate limit exceeded.

## Customer API Rate Limit
Applied to:
- `/api/attendees/**`
- `/api/registrations/**`

Default policy:
- `60 requests per minute` per client IP.

Response when exceeded:
```json
{"error":"rate_limit_exceeded"}
```

## Actuator and Monitoring
- `GET /actuator/health`
- `GET /actuator/metrics`
- `GET /actuator/prometheus`
- `GET /actuator/circuitbreakers`

Business metrics:
- `event.created.total`
- `event.create.duration`
- `event.lookup.duration`
- `registration.created.total`
- `registration.failed.total`
- `registration.process.duration`

## Circuit Breaker
Notification boundary uses Resilience4j circuit breaker:
- name: `notificationService`
- fallback: graceful log fallback when call path is unavailable

## Schema Migrations
Liquibase changelog:
- `classpath:db/changelog/db.changelog-master.yaml`
