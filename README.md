# Insurance Policy CQRS Service

A full-stack Spring Boot (Java 21, Spring Boot 3.5.7) backend with a React frontend (CRA) that demonstrates a CQRS + Event Sourcing approach for managing insurance policies. The backend uses PostgreSQL for the read model and MongoDB for the event store. The repository includes a Docker Compose setup to run services locally.

## Overview
- Backend: Spring Boot (Java 21), Spring Data JPA, MongoDB event store
- Backend Test Coverage: 94%
- Frontend: React (Create React App), Axios
- Databases: PostgreSQL (read model), MongoDB (event store)

## Prerequisites
- Docker & Docker Compose
- Java 21 (if running backend locally, outside Docker)
- Maven or use the included wrapper (for local development)
- npm (for local development)

## Quick start

From the project root, start all services:

```bash
docker compose up --build
```

This will build and run the `postgres`, `mongo`, `backend`, and `frontend` services defined in `docker-compose.yml`.

- Backend will be reachable at: http://localhost:8080
- Frontend (container) will be served at: http://localhost:3000 (note: if you run frontend locally instead of the container, see the next section)

### Frontend (run locally during development)

If you prefer to run the React app on your workstation (so you can use the hot-reload dev server):

1. Open a terminal and go to the frontend folder:

```bash
cd frontend
npm install
npm start
```

CRA will normally start on http://localhost:3000. The frontend is configured to proxy API requests in development to the backend at `http://localhost:8080`. If the backend is running inside Docker, `http://localhost:8080` should be reachable from your host.

### Backend (run locally without Docker)

You can also run the backend locally (outside Docker) if you have Java and a Postgres/Mongo instance available or use Docker for DB only. The project includes a Maven wrapper:

```bash
cd backend
./mvnw spring-boot:run
```

## API endpoints

- GET  /api/policies           — list all policies
- GET  /api/policies/{id}      — get a policy by id
- POST /api/policies           — create a policy
- PUT  /api/policies/{id}      — update a policy

Example request body (create):

```json
{
  "policyName": "Policy A",
  "policyStatus": "ACTIVE",
  "coverageStartDate": "2025-01-01",
  "coverageEndDate": "2025-12-31"
}
```

## Running Java tests

From the repository root or `backend` directory you can run the project's tests using the Maven wrapper (recommended):

```bash
docker compose up -d postgres
docker compose up -d mongo

cd backend
./mvnw test
```

To run a specific test class:

```bash
./mvnw -Dtest=InsurancePolicyCqrsApplicationTests test
```

### Notes about tests
- The project includes unit tests and some integration-style tests (Testcontainers may be used for DB-backed tests). Ensure Docker is running if tests require containers.
