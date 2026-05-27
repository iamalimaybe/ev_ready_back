# Production Deployment Plan

EVReady Pakistan is live in production.

## Current Production State

- Domain: `evready.pk`
- Frontend URL: `https://evready.pk`
- API URL: `https://api.evready.pk`
- VPS provider: Hetzner
- VPS public IPv4: `178.105.183.132`
- VPS OS: Ubuntu 26.04 LTS
- Frontend repo deployed from `main`.
- Backend repo deployed from `main`.
- Backend runs through Docker Compose.
- PostgreSQL runs in Docker and is not publicly exposed.
- Backend is bound to `127.0.0.1:8080`.
- Frontend is bound to `127.0.0.1:3000`.
- Caddy handles public HTTPS and reverse proxy routing.
- Cloudflare manages DNS.
- Public DNS:
  - `evready.pk` -> `178.105.183.132`
  - `api.evready.pk` -> `178.105.183.132`
  - `www.evready.pk` -> `evready.pk`
- Public ports allowed: `22`, `80`, `443`.
- Public ports blocked/not exposed: `3000`, `8080`, `5432`.
- SSH root login is disabled.
- Server access uses the `deploy` user.

## Domain Plan

- Frontend domain: `evready.pk`.
- API subdomain: `api.evready.pk`.
- Recommended shape:
  - Main domain for frontend.
  - `api.evready.pk` for backend.
- Production `CORS_ALLOWED_ORIGINS` should later include:
  - `https://evready.pk`
  - `https://www.evready.pk` only if `www` will be used.

## Production Environment Variables Checklist

- `SPRING_PROFILES_ACTIVE=prod`
- `SERVER_PORT`
- `DB_URL`
- `DB_USER`
- `DB_PASS`
- `CORS_ALLOWED_ORIGINS`
- `LOG_PATH` or `LOG_FILE`

## Backend Docker Deployment Notes

- `docker-compose.prod.yml` is for backend and PostgreSQL only.
- Supply secrets and production values through the shell environment or an untracked env file passed to Docker Compose, for example with `--env-file`.
- Use `.env.prod.example` only as a placeholder template.
- Real `.env` files and production secrets must not be committed.
- PostgreSQL must stay private to the Docker network and must not publish a public port.
- The backend binds to localhost on the VPS for now, with `127.0.0.1:8080:8080` by default.
- The backend is expected to be reached through a reverse proxy later at `api.evready.pk`.
- Reverse proxy/HTTPS setup is separate.
- Frontend deployment is separate.

## Production Operations

- Backend production env file is outside the repo at `/opt/evready/env/backend.prod.env`.
- Real env values and secrets are not committed.
- PostgreSQL backup script exists at `/opt/evready/scripts/backup-postgres.sh`.
- Manual backups are stored under `/opt/evready/backups/postgres`.
- Manual backup command:

```bash
/opt/evready/scripts/backup-postgres.sh
```

- Daily automated backups are not enabled yet.
- Logs are app-rolled and are not backups.

## Redeploy Reminder

- Production VPS clones should stay on `main`.
- Run a manual DB backup before risky backend migrations or data changes.
- Avoid dangerous Docker volume commands in production, including:
  - `docker compose down -v`
  - `docker volume rm`
  - `docker system prune --volumes`

## `.env` Behavior

- Spring Boot does not automatically read `.env`.
- `.env` only works if the runtime loads it, such as:
  - Docker Compose `env_file`
  - Docker Compose `environment`
  - systemd `EnvironmentFile`
  - shell export
  - IDE run configuration
  - Gradle `bootRun` environment

## Logging Checklist

- Production file logs are enabled in the `prod` profile.
- Default log file should be `logs/evready-backend.log`.
- `LOG_PATH` sets the log directory when `LOG_FILE` is not set.
- `LOG_FILE` sets the full file path.
- Logs roll/archive at `10MB`.
- Archives are retained for 7 daily periods.
- Total archive size cap should be documented if configured.
- Log directory must exist and be writable by the app process.
- Console logging remains available for Docker/container logs.
- Rolling logs are not backups.

## Security Checklist

- PostgreSQL must not be exposed publicly.
- Only ports `22`, `80`, and `443` should be public initially.
- SSH key login is preferred.
- Production secrets must not be committed.
- HTTPS is required before public launch.
- CORS must allow only the real frontend domain in production.
- Lead/contact endpoints may need CAPTCHA or rate limiting before public marketing.

## Backup Checklist

- PostgreSQL volume is not a backup.
- Plan a daily `pg_dump`.
- Keep at least 7 days of backups.
- Later, move backups off-server if possible.

## Manual External Steps To Do Later

- Enable daily automated PostgreSQL backups.
