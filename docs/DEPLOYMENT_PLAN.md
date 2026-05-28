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
- SSH password login is disabled.
- Server access uses the `deploy` user.

## Server Hardening Completed

- UFW is enabled.
- UFW allows only OpenSSH/`22`, `80`, and `443`.
- Hetzner Cloud Firewall is enabled with inbound `22`, `80`, and `443` only.
- Public ports allowed: `22`, `80`, `443`.
- Non-public ports: `3000`, `8080`, `5432`.
- PostgreSQL port `5432` is not publicly exposed.
- Backend port `8080` is bound to localhost only and is not public.
- Frontend port `3000` is bound to localhost only and is not public.
- SSH root login is disabled.
- SSH password login is disabled.
- Server access uses the `deploy` user.
- Docker is installed and enabled.
- Caddy is installed and enabled.
- `unattended-upgrades` is enabled.
- Docker daemon log rotation is configured with `json-file`, `max-size` `10m`, and `max-file` `5`.

## Domain Plan

- Frontend domain: `evready.pk`.
- API subdomain: `api.evready.pk`.
- Recommended shape:
  - Main domain for frontend.
  - `api.evready.pk` for backend.
- Production `CORS_ALLOWED_ORIGINS` should include only:
  - `https://evready.pk`
  - `https://www.evready.pk` only if it is actually needed despite the redirect.
- Do not use wildcard or localhost origins in production.

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
- Supply secrets and production values through `/opt/evready/env/backend.prod.env` with Docker Compose `--env-file`.
- The real production env file should not be world-readable. Expected permissions are restrictive, for example `600`.
- Use `.env.prod.example` only as a placeholder template.
- Real `.env` files and production secrets must not be committed.
- PostgreSQL must stay private to the Docker network and must not publish a public port.
- The backend binds to localhost on the VPS for now, with `127.0.0.1:8080:8080` by default.
- The backend is expected to be reached through a reverse proxy later at `api.evready.pk`.
- Reverse proxy/HTTPS setup is separate.
- Frontend deployment is separate.
- Production backend file logs are bind-mounted to `/opt/evready/logs/backend`.
- Before the first bind-mount deployment, create the host log directory and make it writable by the container's `evready` user.

## Production Operations

- Backend production env file is outside the repo at `/opt/evready/env/backend.prod.env`.
- Real env values and secrets are not committed.
- PostgreSQL backup script exists at `/opt/evready/scripts/backup-postgres.sh`.
- Manual backups are stored under `/opt/evready/backups/postgres`.
- Manual backup command:

```bash
/opt/evready/scripts/backup-postgres.sh
```

- PostgreSQL backups remain manual for now.
- Manual backup creation and restore-test practice are documented.
- Automated backups are intentionally not planned at this stage unless the decision changes later.
- Logs are app-rolled and are not backups.
- Old Docker named volume `backend_backend_logs` may still exist after the logging bind mount deployment. Leave it in place initially as historical log fallback.

## Production Logging

- Docker console logs are viewed with:

```bash
docker compose --env-file /opt/evready/env/backend.prod.env -f docker-compose.prod.yml logs --tail=200 backend
```

- Spring Boot rolling file logs are written to `LOG_PATH` / `LOG_FILE`.
- Production backend file logs are accessible on the host at `/opt/evready/logs/backend`.
- Before first use, prepare the host log directory:

```bash
sudo mkdir -p /opt/evready/logs/backend
docker compose --env-file /opt/evready/env/backend.prod.env -f docker-compose.prod.yml build backend
docker compose --env-file /opt/evready/env/backend.prod.env -f docker-compose.prod.yml run --rm --no-deps --user root --entrypoint chown backend -R evready:evready /app/logs
```

- Useful file-log commands:

```bash
tail -n 200 /opt/evready/logs/backend/evready-backend.log
ls -lah /opt/evready/logs/backend
du -sh /opt/evready/logs/backend
```

- Console logs and file logs are intentionally both present:
  - Console logs help Docker/container debugging.
  - File logs provide rolling application history.
- Rolling logs are not backups.

## Manual Backup Restore-Test

- Creating backup files is not enough; periodically prove a backup can be restored.
- Restore-test only into a temporary test database/container, never into the production `postgres` service.
- Use a disposable PostgreSQL container or temporary database name, restore the selected dump there, run basic row-count/sanity checks, then remove only the temporary test resources.
- Do not restore over production unless intentionally performing disaster recovery.
- Do not use production volume deletion commands during restore testing.

## Production Smoke Test Checklist

Public URL checks:

- `https://evready.pk` loads.
- `https://www.evready.pk` redirects to `https://evready.pk`.
- `https://api.evready.pk/api/v1/vehicles` returns `200`.
- `https://api.evready.pk/api/v1/chargers` returns `200`.

Browser checks:

- Home page loads.
- Vehicle Catalog shows backend data.
- Charger Directory shows backend data.
- Get Help page opens.
- Contact page opens.
- Browser console has no CORS errors.

Write-flow checks:

- Submit one obvious test Get Help request.
- Submit one obvious test Contact request.
- Backend logs show no `500` errors.

Server checks:

- `docker ps` shows backend, frontend, and postgres running.
- Caddy is active/running.
- UFW is active.
- PostgreSQL port `5432` is not public.
- Backend `8080` and frontend `3000` are not public.

Backup check:

- Run `/opt/evready/scripts/backup-postgres.sh` after successful deployment or smoke test.
- Confirm backup file exists.
- Backups are manual for now.
- Periodically restore-test a backup into a temporary test database/container.

Cloudflare check:

- SSL/TLS mode is Full (strict).
- Always Use HTTPS is enabled.
- Automatic HTTPS Rewrites is enabled.
- DNS records remain DNS-only unless proxy mode is intentionally tested later.

## Remaining Ops Items

- Optionally move backups off-server later.
- Keep Cloudflare DNS records reviewed if proxy mode is changed later.

## Redeploy Reminder

- Production VPS clones should stay on `main`.
- Run a manual DB backup before risky backend migrations or data changes.
- Pull latest `main` on the VPS.
- Rebuild/restart backend:

```bash
docker compose --env-file /opt/evready/env/backend.prod.env -f docker-compose.prod.yml up -d --build backend
```

- Rebuild/restart frontend only if frontend code changed.
- Check backend and postgres containers are running and healthy.
- Check Caddy status.
- Check API smoke endpoints:
  - `https://api.evready.pk/api/v1/vehicles`
  - `https://api.evready.pk/api/v1/chargers`
- For code/container rollback, check out or reset the VPS clone to the intended previous Git commit, then rebuild/restart the affected service.
- Database migrations may not be safely reversible by Git rollback alone. Treat migration rollback as a separate database recovery task and restore from a known-good backup only when intentionally doing disaster recovery.
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

## Security Checklist

- PostgreSQL must not be exposed publicly.
- Only ports `22`, `80`, and `443` should be public initially.
- SSH key login is preferred.
- Production secrets must not be committed.
- HTTPS is required before public launch.
- CORS must allow only `https://evready.pk` in production, plus `https://www.evready.pk` only if actually needed despite redirect.
- Lead/contact endpoints may need CAPTCHA or rate limiting before public marketing.

## Backup Checklist

- PostgreSQL volume is not a backup.
- Keep manual `pg_dump` backups before risky backend migrations, seed changes, or production data work.
- Backup creation alone is not enough; periodically restore-test backups.
- Restore-test only into a temporary test database/container.
- Do not restore over production unless intentionally performing disaster recovery.
- Automated backups are intentionally not planned at this stage unless the decision changes later.
- Later, move backups off-server if possible.
