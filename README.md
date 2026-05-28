# EV Ready Pakistan Backend

Backend service for EVReady Pakistan. The first release provides data APIs for the vehicle catalog and charger directory, and stores Get Help / lead capture submissions.

## Tech Stack

- Java 17
- Spring Boot 4.0.3
- Gradle
- PostgreSQL 16 through docker-compose
- Spring Data JPA
- Liquibase
- Validation
- Web MVC
- Lombok

## First Release Scope

- Vehicles
- Chargers
- Leads

## Deferred Scope

- Ratings/reviews
- Admin UI
- Authentication
- Payments
- Maps/live charger integration

## Local Setup

Start PostgreSQL:

```powershell
docker compose up -d
```

Expected local database:

- Database: `ev_ready`
- Username: `evready`
- Password: `evreadypass`
- Port: `5432`

Run the app locally:

```powershell
.\gradlew bootRun --args='--spring.profiles.active=dev'
```

Alternatively, set `SPRING_PROFILES_ACTIVE=dev` in your shell or IDE run configuration.

Environment variables:

- `SERVER_PORT`: application HTTP port. Dev profile default: `8080`
- `DB_URL`: JDBC URL for PostgreSQL. Dev profile default: `jdbc:postgresql://localhost:5432/ev_ready`
- `DB_USER`: database username. Dev profile default: `evready`
- `DB_PASS`: database password. Dev profile default: `evreadypass`
- `CORS_ALLOWED_ORIGINS`: comma-separated frontend origins. Dev profile default: `http://localhost:5173`

Do not hardcode the active Spring profile in `application.yml`. Local runs should use `SPRING_PROFILES_ACTIVE=dev`. Production deployments should use `SPRING_PROFILES_ACTIVE=prod`.

Production environment variables:

- `SERVER_PORT`: application HTTP port. Prod profile default: `8080`
- `DB_URL`: JDBC URL for the production PostgreSQL database
- `DB_USER`: production database username
- `DB_PASS`: production database password
- `CORS_ALLOWED_ORIGINS`: comma-separated production frontend origins
- `LOG_PATH`: log directory when `LOG_FILE` is not set. Prod profile default: `logs`
- `LOG_FILE`: full log file path. Prod profile default: `logs/evready-backend.log`
- `EMAIL_NOTIFICATIONS_ENABLED`: enables best-effort lead/contact notification emails. Default: `false`
- `SMTP_HOST`: SMTP host. Current production provider target: `smtp.resend.com`
- `SMTP_PORT`: SMTP port. Current production provider target: `587`
- `SMTP_USERNAME`: SMTP username. Current production provider target: `resend`
- `SMTP_PASSWORD`: SMTP password/API key from environment only; never commit it
- `SMTP_FROM`: notification sender address, for example `no-reply@evready.pk`
- `LEAD_NOTIFICATION_TO`: Get Help notification recipient, for example `leads@evready.pk`
- `CONTACT_NOTIFICATION_TO`: Contact Us notification recipient, for example `contact@evready.pk`

Provide production values through environment variables or the deployment platform's secret store. Do not commit production secrets.

Do not commit `.env` files or real production credentials. Deploy the backend as a built JAR or container image; do not expose the full repository as a public web directory.

The production backend env file lives at `/opt/evready/env/backend.prod.env`. Docker Compose `--env-file` makes values available for compose interpolation, but the backend container only receives variables that are explicitly mapped in `docker-compose.prod.yml` or intentionally loaded with `env_file`. When adding runtime env vars, update both the real server env file and the compose backend `environment:` mapping.

Cloudflare Email Routing is inbound/forwarding only. Backend outbound notification emails use SMTP configuration. Lead/contact submissions are saved to PostgreSQL as the source of truth; notification email failures are logged safely and must not fail successful form submissions.

Safe runtime verification without printing the SMTP secret:

```bash
docker compose --env-file /opt/evready/env/backend.prod.env -f docker-compose.prod.yml exec backend sh -lc 'echo EMAIL_NOTIFICATIONS_ENABLED=$EMAIL_NOTIFICATIONS_ENABLED; test -n "$SMTP_PASSWORD" && echo SMTP_PASSWORD=SET || echo SMTP_PASSWORD=MISSING'
```

In the `prod` profile, the backend writes file logs to `logs/evready-backend.log` by default. `LOG_PATH` changes the log directory when `LOG_FILE` is not set. `LOG_FILE` changes the full file path. In production Docker Compose, backend file logs are bind-mounted to the VPS at `/opt/evready/logs/backend` and should be writable by the container user. Log archives are compressed, roll when they reach 10 MB, are retained for 7 daily periods, and have a configured total archive size cap of 500 MB. Console logging remains available for Docker/container logs. Console logs and file logs are intentionally both present: console logs help Docker/container debugging, and rolling file logs provide application history. Server-level backups, monitoring, and log shipping are separate operational concerns.

Production Docker console logs:

```bash
docker compose --env-file /opt/evready/env/backend.prod.env -f docker-compose.prod.yml logs --tail=200 backend
```

Production backend file log examples:

```bash
tail -n 200 /opt/evready/logs/backend/evready-backend.log
ls -lah /opt/evready/logs/backend
du -sh /opt/evready/logs/backend
```

Older deployments may still have the Docker named volume `backend_backend_logs` with historical logs. Leave that volume in place initially as a fallback.

Spring Boot does not automatically read `.env` files. A `.env` file only works if the runtime loads it, such as Docker Compose `env_file`, Docker Compose `environment`, systemd `EnvironmentFile`, shell export, IDE run configuration, or Gradle `bootRun` environment.

## Production Deployment Checklist

- Set `SPRING_PROFILES_ACTIVE=prod`.
- Set `DB_URL`, `DB_USER`, and `DB_PASS`.
- Set `CORS_ALLOWED_ORIGINS` to the production frontend domain.
- Confirm the log directory exists and is writable by the app process.
- Confirm PostgreSQL is not publicly exposed.
- Confirm HTTPS and reverse proxy handling are configured outside the app.

## Production Docker Notes

- Use `docker-compose.prod.yml` for backend and PostgreSQL deployment on the VPS.
- Copy `.env.prod.example` to an untracked real env file on the server and replace placeholders there.
- Real `.env` files and production secrets must not be committed.
- PostgreSQL is only attached to the Docker network and must not publish a public port.
- The backend binds to `127.0.0.1:8080` by default; a reverse proxy should expose it later through `api.evready.pk`.
- Production backend file logs are available on the VPS at `/opt/evready/logs/backend`.
- Reverse proxy/HTTPS setup is separate from this backend Compose file.
- Frontend deployment is separate.

## Branch Strategy

- `main` is production-ready.
- `develop` is the integration/testing branch.
- All future work should use short-lived `feature/*` branches created from `develop`.
- Pull request flow:
  - `feature/*` -> `develop`
  - `develop` -> `main` for production deployment
- Avoid committing directly to `main` after initial setup.
- Keep deployment-related work in focused feature branches, for example:
  - `feature/deployment-plan`
  - `feature/docker-deployment`
  - `feature/frontend-prod-config`

See [docs/DEPLOYMENT_PLAN.md](docs/DEPLOYMENT_PLAN.md) for the production planning checklist before buying a VPS or domain.

See [docs/LOCAL_SETUP.md](docs/LOCAL_SETUP.md) and [docs/LIQUIBASE_GUIDE.md](docs/LIQUIBASE_GUIDE.md) for setup and migration rules.
