# KULAKWETU API

Production-grade backend foundation for:

- **AGRISOL** marketplace
- **AGRICASH** wallet, payments, ledger, and idempotent financial flows

## Stack

- Spring Boot 3
- PostgreSQL
- Flyway migrations
- JWT resource server + RBAC
- OpenAPI docs
- Dockerized build

## Run locally

```bash
mvn spring-boot:run
```

OpenAPI: `http://localhost:8080/swagger-ui.html`
