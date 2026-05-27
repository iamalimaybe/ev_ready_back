# Production Deployment Plan

This document is a planning checklist only. It does not deploy anything, add infrastructure automation, or assume that any external services have been purchased.

## Current Status

- Backend repo pushed.
- Frontend repo pushed.
- Branch strategy documented.
- VPS not purchased yet.
- Domain purchased: `evready.pk`.
- Registrar: PKNIC.
- DNS provider: Cloudflare Free.
- Cloudflare nameservers have been added at PKNIC.
- Cloudflare activation/propagation is pending.
- DNS A/CNAME records are not configured yet because the VPS IP is not available.

## Planned Low-Cost Architecture

- Start with one VPS initially.
- Run the backend Spring Boot app.
- Serve the frontend Vite static build.
- Keep PostgreSQL private to the server or Docker network.
- Use a reverse proxy for HTTPS.
- Manage DNS through Cloudflare Free after activation/propagation completes.

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

- Choose VPS provider.
- Buy VPS.
- Configure DNS A/CNAME records after VPS IP is available.
- Configure reverse proxy and HTTPS.
- Deploy backend.
- Deploy frontend.
- Smoke test.
