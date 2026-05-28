# Codex Tasks

## Completed

- Task Zero - Backend Docs and Liquibase SQL Setup Rules
- Add Brand, Charger Type, Vehicle Schema and Entities
- Add Vehicle Catalog Read APIs
- Refactor Catalog Read Services for Repository Ownership
- Add Global API Exception Handling
- Add basic validation and error responses
- Add Charger Schema and Entity
- Add Charger Directory Read APIs
- Add Lead Submission Schema and Entity
- Add Lead Submission API
- Refactor Lead String Helpers into Reusable Utility
- Document Public API Response Contract and Add CORS Config
- Add Charger City Options and Contact Us Submission API
- Add Internal Source Tracking and Verification Status to Vehicle and Charger
- Clean Up Placeholder Seed Migrations and Add Charger Type Seed Data
- Add Brand Seed Data
- Review Vehicle Seed Readiness; Actual Vehicle Rows Require Source-Backed Data Before Insertion
- Expand Brand Seed Data for Larger EV Catalog Readiness
- Add Portable AC Charger Type Seed Refinement
- Widen Vehicle Battery Capacity Precision to 3 Decimal Places
- Normalize Local Liquibase Migration History and V-Prefixed Changeset IDs
- Add Vehicle Seed Data Batch 001
- Add Vehicle Seed Data Batch 002
- Add Vehicle Seed Data Batch 003
- Add Vehicle Seed Data Batch 004
- Review Charger Seed Readiness; Actual Charger Rows Require Source-Backed Data Before Insertion
- Add Charger Seed Data Batch 001
- Document Vehicle Verification Status in Public API Responses
- Split Spring Configuration into Common, Dev, and Prod Profiles
- Prepare Git Ignore Rules for Private Backend Deployment Hygiene
- Move Server Port Configuration into Dev and Prod Profiles
- Configure Production File Log Rolling and Retention
- Correct Production Logback Rolling Policy Property Name
- Document Production Deployment Profile, Environment, and Logging Notes
- Add Production Deployment Planning Checklist
- Document Repository Branch Strategy
- Update Production Deployment Planning Checklist
- Update Deployment Plan with Purchased Domain and DNS Status
- Add Minimal Backend Docker Production Deployment Files
- Document Live Production Deployment State
- Document Completed VPS Hardening Steps
- Add Production Smoke Test Checklist
- Complete Frontend/Backend Integration and Live Production Alignment
- Update Production Logging Bind Mount, Rollback, Backup Restore-Test, and API Contract Docs
  - Changed files: `docker-compose.prod.yml`, `README.md`, `docs/API_CONTRACT.md`, `docs/DATA_MODEL.md`, `docs/DEPLOYMENT_PLAN.md`, `docs/CODEX_TASKS.md`
- Clarify Manual PostgreSQL Backup Approach
  - Changed files: `docs/DEPLOYMENT_PLAN.md`, `docs/CODEX_TASKS.md`
- Quiet Static Resource 404 Scanner Logs
  - Changed files: `src/main/java/com/ev/ready/exception/GlobalApiExceptionHandler.java`, `docs/CODEX_TASKS.md`

## Charger Seed Readiness Note

- Charger seed insertion should target actual `charger` columns only after source-backed data is available: `charger_type_id`, `name`, `city`, `area`, `address`, `latitude`, `longitude`, `charging_type`, `status`, `power_kw`, `price_note`, `description`, `image`, `source_url`, `source_label`, `source_checked_at`, `verification_status`, `display_order`, `created_by`.
- Resolve `charger_type_id` from `charger_type.code`.
- Use enum values exactly as stored by JPA string enums: `charging_type` = `AC`, `DC`, `AC_DC`; `status` = `OPERATIONAL`, `LIMITED`, `COMING_SOON`, `UNKNOWN`; `verification_status` = `OFFICIAL`, `DEALER_CONFIRMED`, `USER_REPORTED`, `UNVERIFIED`.
- Do not insert charger rows until charger names, locations, coordinates, prices, and sources are verified from source-backed data.
- For charger seed coordinates, keep latitude/longitude NULL unless exact coordinates are visible in the row's cited source data.

## Next Tasks

- Add admin-only workflows only after authentication and authorization are designed.
- Consider CAPTCHA/rate limiting for public write endpoints before broader marketing traffic.
- Consider off-server backup storage later.
