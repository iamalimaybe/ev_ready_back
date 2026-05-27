# Codex Rules

- Keep tasks small.
- Read only the files required for the current task.
- Do not inspect unrelated files.
- Do not run build/tests unless asked.
- Do not add broad abstractions early.
- Use DTOs for API responses/requests.
- Keep controllers thin.
- Keep business logic in services.
- Use Liquibase for schema changes only.
- Keep the existing XML master changelog if it only acts as an includeAll wrapper.
- Use SQL formatted Liquibase files for actual migrations, not YAML/XML migration files.
- Mention changed files after every task.

## File Inspection Rules

Before editing, inspect only the smallest set of files needed for the current task.

For docs/setup tasks:
- Start with build.gradle, settings.gradle, application.yml, docker-compose.yml, README.md, AGENTS.md, and docs files.
- Inspect db/changelog files only if the task involves Liquibase.
- Do not scan controllers, services, repositories, entities, DTOs, or tests unless the task explicitly needs them.

For API/entity tasks:
- Inspect only the target feature files and directly related docs.
- Do not inspect unrelated modules.
- Do not run broad repository searches unless the task requires cross-cutting changes.

For every task:
- Prefer the smallest safe file set.
- Do not expand beyond the listed task scope unless necessary.
- Avoid exploratory scanning because it increases usage.

## Backend Entity Rules

For every new JPA entity created later:
- Primary key must be `Long` / PostgreSQL `BIGINT`, not UUID.
- Use database identity/sequence strategy suitable for PostgreSQL.
- Include audit fields by default:
  - createdAt
  - createdBy
  - updatedAt
  - updatedBy
- Add equals and hashCode for every entity.
- Prefer stable equality based on the entity identifier once assigned.
- Do not add entity relationships unless the task explicitly needs them.
- Keep entities simple and avoid premature inheritance unless an audit base class is explicitly introduced in a later task.

## Service and Repository Ownership Rules

- A repository should be used only by its owning service.
- Do not inject one feature/entity repository directly into another feature/entity service.
- If another service needs data or behavior from that entity, expose a method from the owning service and inject that service instead.
- Example: use BrandService from VehicleService if brand-specific lookup/validation is needed; do not inject BrandRepository directly into VehicleService.
- Custom DB queries should be defined in the relevant repository interface.
- Built-in Spring Data repository methods can be used as-is from the owning service.
- Keep query logic in repositories and business/use-case logic in services.
- Avoid circular service dependencies. If a use case starts needing multiple services heavily, create a small orchestration/service layer for that use case instead of mixing repository access.

## Utility Reuse Rules

- Before creating private helper methods, check whether a matching utility already exists.
- Put reusable helper logic in categorized utility classes under a common utility package.
- Examples:
  - string/text helpers in a string utility class
  - file helpers in a file utility class
  - date/time helpers in a date/time utility class
- Do not create broad catch-all utility classes.
- Keep utility methods small, static, and side-effect free when possible.
- Do not add new libraries for simple utility behavior unless clearly justified.
- Prefer reusing existing project utilities before adding new methods.
