# PayGate Transaction Processing Service

A runnable starter implementation of a transaction-processing REST service using Java 17, Spring Boot 3, Spring Data JPA, PostgreSQL, validation, OpenAPI, and JUnit/Mockito.

## Included
- Create, list, fetch, status-update, and delete transaction endpoints.
- Database uniqueness constraint and lookup for idempotency keys.
- Indexed transaction status/creation fields and Hibernate JDBC batching configuration.
- HTTP Basic demo security so the sample runs without a token issuer.
- OpenAPI UI and controller tests.

> Important: this is a portfolio starter, not a production payment processor. It does not move real money, implement bank settlement, or provide JWT issuance/verification. Do not claim the supplied resume metrics unless you measured them yourself. For production, replace demo auth with JWT resource-server validation, add migrations, audit trails, authorization policies, rate limiting, and reconciliation workflows.

## Requirements
- JDK 17+
- Maven 3.9+
- PostgreSQL 14+

## Run
Create a database and user (example):
```sql
CREATE USER paygate WITH PASSWORD 'paygate';
CREATE DATABASE paygate OWNER paygate;
```
Then from the project folder:
```bash
mvn spring-boot:run
```
Environment overrides: `DATABASE_URL`, `DATABASE_USER`, `DATABASE_PASSWORD`.

Swagger UI: http://localhost:8080/swagger-ui.html

Demo credentials: `demo` / `change-me` (change before sharing or deployment).

## Example request
```bash
curl -u demo:change-me -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{"amount":12.50,"currency":"USD","idempotencyKey":"order-123"}'
```

## Tests
```bash
mvn test
```
