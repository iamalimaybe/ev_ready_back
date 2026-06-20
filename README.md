# EVReady Pakistan Backend

Backend service for EVReady Pakistan.

This service provides public APIs for the EV vehicle catalog, charger directory, AI EV recommendation gateway, lead capture, contact submissions, vehicle reviews, and charger feedback. It also includes protected admin APIs for reviewing operational submissions, moderating user-submitted content, and managing catalog data.

The backend is designed to keep public data access separate from internal/admin workflows. Public APIs return only safe frontend-facing fields. Admin APIs are protected and expose sensitive operational data only to authenticated admins.

For AI recommendations, this backend acts as the public gateway. The browser calls the backend, and the backend calls the internal AI recommender service. The AI recommender and model runtime are not intended to be browser-facing public services.

## Tech Stack

* Java 17
* Spring Boot 4.0.3
* Gradle
* PostgreSQL 16
* Spring Data JPA
* Liquibase
* Bean Validation
* Spring Web MVC
* Spring Security
* Bucket4j
* Lombok
* Docker Compose

## Current Scope

Implemented backend scope includes:

* Vehicle catalog APIs
* Charger directory APIs
* Brand APIs
* Charger type APIs
* AI EV recommendation gateway APIs
* Backend-side rate limiting for expensive AI recommendation creation
* Get Help lead submission API
* Contact Us submission API
* SMTP-based lead/contact notification support
* Public vehicle review submission
* Approved-only public vehicle review retrieval
* Approved-only vehicle rating summaries
* Public charger feedback submission
* Approved-only public charger feedback retrieval
* Minimal admin authentication
* Protected admin lead/contact retrieval
* Protected admin lead/contact status updates
* Protected admin vehicle review moderation
* Protected admin charger feedback moderation
* Protected admin vehicle catalog management
* Protected admin charger directory management
* Liquibase-based schema and seed data management
* Dev and production profile separation
* Production Docker Compose support

## Non-Goals

This backend does not currently provide:

* Payments
* Booking
* Public user accounts
* Public user authentication
* Live charger availability
* Real-time charger network integration
* Route planning guarantees
* Field verification guarantees
* Admin frontend UI
* Complex CRM workflows
* SLA or callback guarantee workflows
* Direct public browser access to the AI recommender service
* Direct public browser access to model runtime services

## Important Data Trust Rules

EVReady stores and exposes catalog and directory data carefully, but public responses must not overstate certainty.

### Vehicle Data

Vehicle public responses may include `verificationStatus` as a source-confidence signal.

`OFFICIAL` means the data is source-backed from an official OEM, operator, distributor, or similar source. It does not mean EVReady personally audited, field-verified, or guarantees vehicle prices, specs, range, availability, warranty, or dealer claims.

Internal vehicle provenance fields such as `sourceUrl`, `sourceLabel`, and `sourceCheckedAt` are not exposed through public vehicle DTOs.

### Charger Data

Charger public responses may include `verificationStatus` as a source-confidence signal.

`OFFICIAL` means the charger data is source-backed from an official operator, network, distributor, or similar source. It does not mean live charger availability, guaranteed charger status, verified pricing, or EVReady field verification.

Charger `status` is not live availability. It should not be presented as proof that a charger is working, available, unoccupied, compatible, or priced as shown at the time of travel.

Internal charger provenance fields such as `sourceUrl`, `sourceLabel`, and `sourceCheckedAt` are not exposed through public charger DTOs.

### AI Recommendation Data

AI EV recommendations are generated from available EVReady catalog data and user-provided request details.

AI responses must be treated as decision support, not guaranteed purchase advice. The system keeps warnings visible where relevant, especially around vehicle prices, specs, availability, route feasibility, charger access, connector compatibility, and travel planning.

The backend does not expose the model runtime directly. AI recommendation requests go through the backend gateway, which forwards requests to an internal recommender service and applies request controls to protect expensive model calls.

### User Reviews And Feedback

Vehicle reviews and charger feedback are user-submitted content.

Public submissions default to a pending state and are not shown publicly until approved through protected admin moderation APIs.

Approved vehicle reviews may affect public vehicle rating summaries. Pending, rejected, and spam reviews must not affect public ratings.

Approved charger feedback may be shown publicly, but it must not automatically change public charger status or imply live charger availability.

## API Overview

Base API path:

```text
/api/v1
```

### Public APIs

Public frontend-facing APIs include:

```text
GET  /api/v1/brands
GET  /api/v1/charger-types

GET  /api/v1/vehicles
GET  /api/v1/vehicles/{id}
GET  /api/v1/vehicles/reviews/experience-types
POST /api/v1/vehicles/{vehicleId}/reviews
GET  /api/v1/vehicles/{vehicleId}/reviews

GET  /api/v1/chargers
GET  /api/v1/chargers/{id}
GET  /api/v1/chargers/cities
GET  /api/v1/chargers/feedback-types
POST /api/v1/chargers/{chargerId}/feedback
GET  /api/v1/chargers/{chargerId}/feedback

POST /api/v1/ai/recommendations
GET  /api/v1/ai/recommendations/{id}
GET  /api/v1/ai/recommendations/health

POST /api/v1/leads
POST /api/v1/contact-submissions
```

Public list endpoints return active records only where applicable.

Public lead and contact retrieval APIs do not exist. Lead/contact data contains personal information and is available only through protected admin APIs.

See `docs/API_CONTRACT.md` for the detailed request and response contract.

### AI Recommendation Gateway APIs

The AI recommendation endpoints are public frontend-facing gateway endpoints:

```text
POST /api/v1/ai/recommendations
GET  /api/v1/ai/recommendations/{id}
GET  /api/v1/ai/recommendations/health
```

The intended flow is:

```text
Frontend /recommend
  -> EVReady backend API gateway
  -> Internal AI recommender service
  -> Model runtime
```

The frontend should call the backend gateway, not the internal recommender service directly.

The backend gateway:

* accepts public recommendation creation requests
* forwards accepted requests to the internal recommender service
* exposes polling for recommendation status/result retrieval
* exposes a lightweight recommender health endpoint
* applies Bucket4j rate limiting to expensive recommendation creation requests
* leaves result polling available so accepted requests can complete normally

Rate limiting applies to:

```text
POST /api/v1/ai/recommendations
```

Rate limiting does not apply to:

```text
GET /api/v1/ai/recommendations/{id}
GET /api/v1/ai/recommendations/health
```

When the rate limit is exceeded, the backend returns:

```text
429 Too Many Requests
```

### Protected Admin APIs

Protected admin APIs require an authenticated admin session.

Admin API groups include:

```text
/api/v1/admin/auth
/api/v1/admin/leads
/api/v1/admin/contact-submissions
/api/v1/admin/vehicles
/api/v1/admin/chargers
/api/v1/admin/vehicle-reviews
/api/v1/admin/charger-feedback
```

Admin APIs are intended for trusted operators only. They may expose personal submission data, moderation fields, internal provenance fields, active flags, display order, and catalog correction fields.

Admin UI is separate from this backend and is not part of this repository.

## Response Contract

Public and admin APIs generally use plain JSON responses.

* Successful list endpoints return JSON arrays or stable paginated responses where documented.
* Successful detail endpoints return plain JSON objects.
* Successful create endpoints return their documented response DTO.
* Success responses are not wrapped in a common `ApiResponse` envelope.
* Error responses use the shared error envelope.
* Public read APIs return `404` for missing or inactive records where applicable.
* Frontend clients should read `fieldErrors` for validation messages.
* AI recommendation gateway responses preserve the JSON response shape returned by the internal recommender service where applicable.
* AI recommendation rate-limit failures return `429 Too Many Requests`.

See `docs/API_CONTRACT.md` for endpoint-level details.

## Local Setup

Start PostgreSQL from the repo root:

```powershell
docker compose up -d
```

Expected local database:

```text
Database: ev_ready
Username: evready
Password: evreadypass
Port: 5432
```

Run the app locally:

```powershell
.\gradlew bootRun --args='--spring.profiles.active=dev'
```

Alternatively, set `SPRING_PROFILES_ACTIVE=dev` in your shell or IDE run configuration.

The dev profile listens on port `8080` by default.

For local AI recommendation gateway testing, the internal recommender service should be running separately and reachable through:

```text
AI_RECOMMENDER_BASE_URL
```

The dev default is:

```text
http://localhost:8081
```

## Environment Variables

Spring Boot does not automatically read `.env` files. A `.env` file only works if the runtime loads it, such as Docker Compose `env_file`, Docker Compose `environment`, systemd `EnvironmentFile`, shell export, IDE run configuration, or Gradle `bootRun` environment.

### Common Variables

```text
SERVER_PORT
DB_URL
DB_USER
DB_PASS
CORS_ALLOWED_ORIGINS
```

### AI Recommender Gateway Variables

```text
AI_RECOMMENDER_BASE_URL
AI_RECOMMENDER_CONNECT_TIMEOUT
AI_RECOMMENDER_READ_TIMEOUT
AI_RECOMMENDATION_RATE_LIMIT_MAX_REQUESTS
AI_RECOMMENDATION_RATE_LIMIT_WINDOW
AI_RECOMMENDATION_RATE_LIMIT_ENTRY_TTL
```

Example development values:

```text
AI_RECOMMENDER_BASE_URL=http://localhost:8081
AI_RECOMMENDER_CONNECT_TIMEOUT=5s
AI_RECOMMENDER_READ_TIMEOUT=95s
AI_RECOMMENDATION_RATE_LIMIT_MAX_REQUESTS=2
AI_RECOMMENDATION_RATE_LIMIT_WINDOW=10m
AI_RECOMMENDATION_RATE_LIMIT_ENTRY_TTL=30m
```

Example production shape:

```text
AI_RECOMMENDER_BASE_URL=http://internal-recommender:8081
AI_RECOMMENDER_CONNECT_TIMEOUT=5s
AI_RECOMMENDER_READ_TIMEOUT=95s
AI_RECOMMENDATION_RATE_LIMIT_MAX_REQUESTS=2
AI_RECOMMENDATION_RATE_LIMIT_WINDOW=10m
AI_RECOMMENDATION_RATE_LIMIT_ENTRY_TTL=30m
```

Do not expose the internal recommender service directly to browsers.

### Production Variables

```text
SPRING_PROFILES_ACTIVE
SERVER_PORT
DB_URL
DB_USER
DB_PASS
CORS_ALLOWED_ORIGINS
LOG_PATH
LOG_FILE
EMAIL_NOTIFICATIONS_ENABLED
SMTP_HOST
SMTP_PORT
SMTP_USERNAME
SMTP_PASSWORD
SMTP_FROM
LEAD_NOTIFICATION_TO
CONTACT_NOTIFICATION_TO
ADMIN_USERNAME
ADMIN_PASSWORD
AI_RECOMMENDER_BASE_URL
AI_RECOMMENDER_CONNECT_TIMEOUT
AI_RECOMMENDER_READ_TIMEOUT
AI_RECOMMENDATION_RATE_LIMIT_MAX_REQUESTS
AI_RECOMMENDATION_RATE_LIMIT_WINDOW
AI_RECOMMENDATION_RATE_LIMIT_ENTRY_TTL
```

Do not commit production secrets, `.env` files, SMTP credentials, database passwords, or admin credentials.

Production values should be supplied through environment variables, an untracked server env file, or a deployment secret store.

## Email Notifications

The backend supports best-effort SMTP notification emails for lead and contact submissions.

Cloudflare Email Routing is inbound/forwarding only. Backend outbound notification emails use SMTP configuration.

Lead/contact submissions are saved to PostgreSQL as the source of truth. Notification email failures are logged safely and must not fail a successfully saved submission.

Required SMTP-related variables:

```text
EMAIL_NOTIFICATIONS_ENABLED
SMTP_HOST
SMTP_PORT
SMTP_USERNAME
SMTP_PASSWORD
SMTP_FROM
LEAD_NOTIFICATION_TO
CONTACT_NOTIFICATION_TO
```

`SMTP_PASSWORD` must come from the environment only and must never be committed.

## Admin Authentication

The first admin authentication version uses environment-backed credentials:

```text
ADMIN_USERNAME
ADMIN_PASSWORD
```

Admin authentication is used only for protected `/api/v1/admin/**` endpoints.

The first version is intentionally small and uses a single admin role. Granular roles, public user auth, and complex permission models are deferred until there is a real operating need.

## AI Gateway Security

The AI recommendation gateway protects the public backend boundary around expensive AI work.

Security shape:

```text
Browser
  -> EVReady backend
  -> Internal AI recommender
  -> Model runtime
```

The backend should be the only browser-facing AI entry point.

Current gateway controls include:

* no direct browser access to the internal recommender service
* backend-side request forwarding
* configurable connect and read timeouts
* Bucket4j rate limiting for recommendation creation
* safe `429 Too Many Requests` response when creation quota is exceeded
* continued support for polling already-created recommendation runs

The rate limiter is in-memory. This is suitable for the current single backend instance deployment. If the backend is scaled to multiple instances, rate-limit state should move to a shared store or edge-level limiter.

## Database And Liquibase

Liquibase manages schema and seed data.

Master changelog:

```text
src/main/resources/db/db.changelog-master.xml
```

SQL changelog files:

```text
src/main/resources/db/changelog/
```

Rules:

* Keep the XML master changelog as an include wrapper.
* Write actual schema and seed changes as SQL formatted Liquibase files.
* Use `BIGINT` primary keys, not UUIDs by default.
* Include audit fields on entities by default.
* Use preconditions for create table scripts.
* Keep migration changes focused and separate by responsibility.

See `docs/LIQUIBASE_GUIDE.md` for migration conventions.

## Production Deployment

The backend is deployed separately from the frontend and the AI recommender service.

Current production shape:

```text
Frontend: https://evready.pk
API: https://api.evready.pk
AI recommender: internal service behind the backend gateway
```

Production notes:

* Backend runs through Docker Compose.
* PostgreSQL runs privately and is not publicly exposed.
* Backend is served behind a reverse proxy.
* HTTPS is handled outside the Spring Boot app.
* Frontend deployment is separate.
* AI recommender deployment is separate and should remain internal to the backend gateway.
* Real production environment values live outside the repository.
* Logs, backups, monitoring, and reverse proxy configuration are operational concerns outside the application code.

Production Docker Compose file:

```text
docker-compose.prod.yml
```

The production env file is not committed. When adding runtime environment variables, update both the real server env file and the backend service environment mapping in `docker-compose.prod.yml`, unless an intentional `env_file` setup is being used.

## Logging

In the `prod` profile, backend file logging is enabled.

Relevant variables:

```text
LOG_PATH
LOG_FILE
```

Console logs remain available for container debugging. Rolling file logs provide application history. Logs are not backups.

Example Docker log command:

```bash
docker compose --env-file /path/to/backend.prod.env -f docker-compose.prod.yml logs --tail=200 backend
```

Example file log commands on the server:

```bash
tail -n 200 /opt/evready/logs/backend/evready-backend.log
ls -lah /opt/evready/logs/backend
du -sh /opt/evready/logs/backend
```

## Branch Strategy

* `main` is production-ready.
* `develop` is the integration/testing branch.
* Future work should use short-lived `feature/*` branches created from `develop`.
* Pull request flow:

    * `feature/*` to `develop`
    * `develop` to `main` for production deployment
* Avoid committing directly to `main` after initial setup.
* Keep deployment-related work in focused feature branches.
* Deploy production from `main`.

## Documentation

Additional backend documentation:

```text
docs/API_CONTRACT.md
docs/DATA_MODEL.md
docs/DEPLOYMENT_PLAN.md
docs/EMAIL_NOTIFICATION_PLAN.md
docs/LEAD_CONTACT_HANDLING_PLAN.md
docs/LOCAL_SETUP.md
docs/LIQUIBASE_GUIDE.md
docs/USER_REVIEWS_AND_FEEDBACK_PLAN.md
docs/VEHICLE_DATA_QUALITY_REVIEW.md
docs/CHARGER_DATA_QUALITY_REVIEW.md
docs/ADMIN_MVP_PLAN.md
docs/DECISIONS.md
```

Recommended reading order:

1. `docs/LOCAL_SETUP.md`
2. `docs/API_CONTRACT.md`
3. `docs/DATA_MODEL.md`
4. `docs/LIQUIBASE_GUIDE.md`
5. `docs/DEPLOYMENT_PLAN.md`

## Security And Privacy Notes

* Do not commit real secrets.
* Do not expose PostgreSQL publicly.
* Do not expose admin endpoints without authentication.
* Do not expose lead/contact retrieval publicly.
* Do not expose the internal AI recommender directly to browsers.
* Do not expose model runtime services directly to browsers.
* Do not log full message bodies, phone numbers, emails, credentials, or exported personal data.
* Do not claim live charger availability.
* Do not claim guaranteed route feasibility.
* Do not claim field verification where only source-backed catalog data exists.
* Keep production CORS restricted to intended frontend origins.
* Treat lead/contact exports as sensitive operational data.
* Treat AI recommendations as decision support, not guaranteed market or travel advice.
