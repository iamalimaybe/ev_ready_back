# Data Model

High-level planned entities for the first release.

## Vehicle

Represents an EV model shown in the vehicle catalog.

Internal source/provenance fields support production seed data quality. `sourceUrl`, `sourceLabel`, and `sourceCheckedAt` remain internal. `verificationStatus` is exposed only on public vehicle response DTOs for frontend source-confidence badges:

- `verificationStatus`

`batteryCapacityKwh` supports up to 3 decimal places so smaller EV bike and scooter battery values can be stored without rounding.

## Charger

Represents a charging location shown in the charger directory.

Field summary:

- `id`
- `chargerType`
- `name`
- `city`
- `area`
- `address`
- `latitude`
- `longitude`
- `chargingType`
- `status`
- `powerKw`
- `priceNote`
- `description`
- `image`
- `sourceUrl`
- `sourceLabel`
- `sourceCheckedAt`
- `verificationStatus`
- `active`
- `displayOrder`
- audit fields

Charger uses a `ChargerType` reference for connector/filter information.

`chargingType`, `status`, and `verificationStatus` are Java enums. The database should not add CHECK constraints for these fields unless explicitly requested later.

## LeadSubmission

Represents a Get Help / lead capture submission from the frontend.

Field summary:

- `id`
- `name`
- `phone`
- `city`
- `interestType`
- `message`
- `sourcePage`
- `consentAccepted`
- `leadStatus`
- `active`
- audit fields

`interestType` and `leadStatus` are Java enums. The database should not add CHECK constraints for these fields unless explicitly requested later.

Leads are stored for Get Help / lead capture only and are not exposed publicly in the first release.

## ContactSubmission

Represents a Contact Us submission from users, businesses, collaborators, charger providers, dealers, or other general inquiries.

Field summary:

- `id`
- `name`
- `email`
- `phone`
- `organization`
- `inquiryType`
- `message`
- `sourcePage`
- `consentAccepted`
- audit fields

`inquiryType` is stored as a string for now.

Contact submissions are separate from Get Help leads and are not exposed through public or admin retrieval APIs in the first release.

## Data Conventions

- All entity IDs should be `BIGINT` in PostgreSQL and `Long` in Java.
- Do not use UUID primary keys.
- Every entity should include audit fields:
  - `createdAt`
  - `createdBy`
  - `updatedAt`
  - `updatedBy`
- Every JPA entity should include equals and hashCode.
- Ratings/reviews are deferred and should later be separate tables/entities.
- Vehicle `sourceUrl`, `sourceLabel`, and `sourceCheckedAt` fields remain internal. Vehicle public response DTOs expose `verificationStatus` only for frontend source-confidence badges.
- Charger source/provenance fields remain internal data-quality fields for now and should not be exposed in public API response DTOs until an internal/admin contract is designed.
- Vehicle `batteryCapacityKwh` should preserve up to 3 decimal places.
