# FlowApprove BE

Backend for the FlowApprove project, built with Spring Boot and a Maven multi-module structure.

## Table of Contents

1. [Overview](#overview)
2. [Current Status](#current-status)
3. [Tech Stack](#tech-stack)
4. [Module Structure](#module-structure)
5. [Environment Requirements](#environment-requirements)
6. [Running Local Dependencies](#running-local-dependencies)
7. [Running Tests](#running-tests)
8. [Running the API](#running-the-api)
9. [Running the DB Migrator](#running-the-db-migrator)
10. [Configuration Notes](#configuration-notes)
11. [Current API Surface](#current-api-surface)
12. [Known Limitations](#known-limitations)
13. [Next Steps](#next-steps)

## Overview

This repository contains the backend application for FlowApprove.

The backend is still in the foundation stage. The current focus is on establishing the base architecture, module boundaries, security, migration flow, and basic API wiring before real business features and entities are introduced.

## Current Status

- The repository is already split into multiple modules by responsibility.
- Skeleton APIs already exist for several initial areas.
- JWT-based security is already enabled for `/api/v1/**`.
- A dedicated database migrator module already exists.
- Baseline PostgreSQL migration files are already in place.
- Integration tests already cover security and migration flow.
- Real business features have not been implemented yet.
- There is still no complete entity or domain model for the full system.

## Tech Stack

- Java 21
- Spring Boot 3
- Maven Wrapper
- Spring Security Resource Server
- Flyway
- PostgreSQL
- Redis
- Testcontainers

## Module Structure

```text
flowapprove-shared          Shared types, value objects, common contracts
flowapprove-domain          Domain layer
flowapprove-application     Use cases, command/query handlers, ports
flowapprove-infrastructure  Persistence, redis, tenant context, security adapters
flowapprove-api             REST API application
flowapprove-db-migrator     Migration runner for public and tenant schemas
```

## Environment Requirements

- JDK 21
- Docker Desktop or Docker Engine

Global Maven installation is not required because the repository already includes `mvnw` and `mvnw.cmd`.

Quick check:

```bash
java -version
docker version
```

## Running Local Dependencies

Start PostgreSQL and Redis:

```bash
docker compose up -d
```

Default services:
- PostgreSQL: `localhost:55432`
- Redis: `localhost:56379`

## Running Tests

Windows:

```bash
.\mvnw.cmd test
```

macOS/Linux:

```bash
./mvnw test
```

Notes:
- Some integration tests use Testcontainers, so Docker must be running.
- Tests are currently the main verification point for backend build health and base wiring.

## Running the API

Windows:

```bash
.\mvnw.cmd -pl flowapprove-api spring-boot:run
```

macOS/Linux:

```bash
./mvnw -pl flowapprove-api spring-boot:run
```

Default runtime behavior of `flowapprove-api`:
- Database: PostgreSQL
- Actuator exposure: `health`, `info`
- Redis: configured through environment variables

## Running the DB Migrator

Windows:

```bash
.\mvnw.cmd -pl flowapprove-db-migrator spring-boot:run
```

macOS/Linux:

```bash
./mvnw -pl flowapprove-db-migrator spring-boot:run
```

Default PostgreSQL connection:
- URL: `jdbc:postgresql://localhost:55432/flowapprove`
- Username: `postgres`
- Password: `postgres`

Supported environment overrides:

```text
FLOWAPPROVE_DB_URL
FLOWAPPROVE_DB_USERNAME
FLOWAPPROVE_DB_PASSWORD
FLOWAPPROVE_DB_ACTION
FLOWAPPROVE_DB_TENANT
```

## Configuration Notes

### API Application

`flowapprove-api` now defaults to PostgreSQL, aligned with the Docker Compose stack and the migrator.

### Security

- `/actuator/health` is public.
- `/api/v1/**` requires a valid JWT.
- The current secret is only suitable for local development and is not production-ready.

### Database Migration

`flowapprove-db-migrator` manages migration for:
- `public` schema
- tenant schemas

## Current API Surface

The backend is still in the skeleton stage, but these endpoint groups already exist:

- `POST /api/v1/organizations`
- `POST /api/v1/workflow-definitions`
- `POST /api/v1/workflow-definitions/{workflowDefinitionId}/publish`
- `POST /api/v1/workflow-requests`
- `GET /api/v1/workflow-requests/{workflowRequestId}`
- `GET /api/v1/approvals/pending`

These endpoints currently exist to support the technical foundation and validate wiring between layers. They do not represent a complete business feature set yet.

## Known Limitations

- Full entities and real business flows are still missing.
- API contracts are not finalized yet.
- Runtime configuration between the API application and PostgreSQL migration flow is not fully unified.
- The current setup is suitable for foundation work, not for production use.

## Next Steps

- Add entities and domain models based on actual use cases.
- Finalize API contracts for real frontend integration.
- Unify local and development runtime configuration.
- Expand tests beyond wiring and integration smoke coverage into real use-case coverage.
