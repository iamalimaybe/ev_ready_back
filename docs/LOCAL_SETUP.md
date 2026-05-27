# Local Setup

## Start PostgreSQL

Use docker-compose from the repo root:

```powershell
docker compose up -d
```

Expected database:

- Database: `ev_ready`
- Username: `evready`
- Password: `evreadypass`
- Port: `5432`

## Run The App

Run locally with Gradle:

```powershell
.\gradlew bootRun --args='--spring.profiles.active=dev'
```

Alternatively, set `SPRING_PROFILES_ACTIVE=dev` in your shell or IDE run configuration.

The dev profile listens on port `8080` by default. Override it with `SERVER_PORT` when needed.

## Environment Variables

- `DB_URL`: JDBC URL for PostgreSQL. Dev profile default: `jdbc:postgresql://localhost:5432/ev_ready`
- `DB_USER`: database username. Dev profile default: `evready`
- `DB_PASS`: database password. Dev profile default: `evreadypass`
- `CORS_ALLOWED_ORIGINS`: comma-separated frontend origins allowed to call `/api/**`. Dev profile default: `http://localhost:5173`
- `SERVER_PORT`: application HTTP port. Dev profile default: `8080`

Spring Boot does not automatically read `.env` files unless they are wired through the shell, IDE run configuration, Gradle configuration, or Docker Compose `env_file`.

For production deployments, activate the `prod` profile with `SPRING_PROFILES_ACTIVE=prod` and provide `SERVER_PORT` when the deployment platform requires a specific port. Provide `DB_URL`, `DB_USER`, `DB_PASS`, and `CORS_ALLOWED_ORIGINS` through environment variables or the deployment platform's secret store. Do not commit production secrets.

Local frontend default origin:

```text
http://localhost:5173
```
