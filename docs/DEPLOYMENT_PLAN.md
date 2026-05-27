# Production Deployment Plan

This document is a planning checklist only. It does not deploy anything, add infrastructure automation, or assume that any external services have been purchased.

## Current Status

- Backend repo pushed to private GitHub repository.
- Frontend repo pushed to private GitHub repository.
- VPS not purchased yet.
- Domain not purchased yet.
- DNS not configured yet.
- Current domain preference is `.ai`; final domain is still TBD.

## Planned Low-Cost Architecture

- Start with one VPS.
- Run the backend Spring Boot app on the server.
- Serve the frontend as a Vite static build.
- Keep PostgreSQL private to the server or Docker network.
- Put a reverse proxy in front of the frontend and backend for HTTPS.
- Consider Cloudflare DNS later if it is compatible with the selected domain and registrar.

## Domain Plan

- Frontend domain: TBD, likely `.ai`.
- API domain/subdomain: TBD.
- Recommended shape:
  - Main domain serves the frontend.
  - `api.<domain>` serves the backend API.
- Production CORS must be updated after the final domain is selected.

## Production Environment Variables Checklist

- `SPRING_PROFILES_ACTIVE=prod`
- `SERVER_PORT`
- `DB_URL`
- `DB_USER`
- `DB_PASS`
- `CORS_ALLOWED_ORIGINS`
- `LOG_PATH` or `LOG_FILE`

## Logging Checklist

- Production file logs are enabled in the `prod` profile.
- `LOG_PATH` sets the log directory when `LOG_FILE` is not set.
- `LOG_FILE` sets the full file path.
- Logs roll at `10MB`.
- Archives are retained for 7 daily periods.
- Total archive size cap is configured when supported by the active logging setup.
- Log directory must exist and be writable by the app process.

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
- Buy final domain, likely `.ai`.
- Configure DNS.
- Configure reverse proxy and HTTPS.
- Deploy backend.
- Deploy frontend.
- Smoke test.
