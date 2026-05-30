# Data Model

High-level planned entities for the first release.

## Vehicle

Represents an EV model shown in the vehicle catalog.

Internal source/provenance fields support production seed data quality. `sourceUrl`, `sourceLabel`, and `sourceCheckedAt` remain internal. `verificationStatus` is exposed only on public vehicle response DTOs for frontend source-confidence badges:

- `verificationStatus`

`OFFICIAL` means the vehicle data is source-backed from an official OEM, operator, distributor, or similar source. It does not mean EVReady personally audited, field-verified, or guarantees specs or pricing.

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

`sourceUrl`, `sourceLabel`, and `sourceCheckedAt` remain internal provenance fields. `verificationStatus` is exposed on public charger response DTOs for frontend source-confidence badges. `OFFICIAL` means the charger data is source-backed from an official operator, network, distributor, or similar source; it must not imply live charger availability, verified price, guaranteed charger status, or EVReady field verification.

Protected admin charger correction APIs can update editable charger directory fields, including internal provenance fields. Public charger response DTOs must continue excluding `sourceUrl`, `sourceLabel`, and `sourceCheckedAt`. Admin updates to `status` remain reported/non-live directory corrections and do not imply current charger availability.

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
- `contactStatus`
- audit fields

`inquiryType` is stored as a string for now. `contactStatus` is a Java enum with first-version admin workflow values: `NEW`, `REVIEWED`, `RESPONDED`, `CLOSED`, and `SPAM`. The database should not add CHECK constraints for this field unless explicitly requested later.

Contact submissions are separate from Get Help leads and are not exposed publicly. Protected admin APIs can read submissions and update `contactStatus`.

## VehicleReview

Represents a public vehicle review submission stored for future moderation.

Field summary:

- `id`
- `vehicleId`
- `rating`
- `reviewText`
- `displayName`
- `city`
- `experienceType`
- `reviewStatus`
- `moderatedAt`
- `moderatedBy`
- `moderationReason`
- audit fields

`reviewStatus` is a Java enum with values: `PENDING`, `APPROVED`, `REJECTED`, and `SPAM`.

`experienceType` is a Java enum with controlled reviewer-context values: `OWNER`, `FORMER_OWNER`, `TEST_DRIVE`, `BOOKED`, `CONSIDERING`, `RESEARCH_ONLY`, and `OTHER`.

Public submissions default to `PENDING`. Protected admin APIs can read submissions and update `reviewStatus`, `moderatedAt`, `moderatedBy`, `moderationReason`, and update audit fields.

Vehicle list/detail DTOs expose an approved-only rating summary derived from `VehicleReview` records with `reviewStatus = APPROVED`. Public approved review retrieval returns only safe public fields. Pending, rejected, and spam reviews must not affect public averages and must not be returned publicly.

## ChargerFeedback

Represents a public charger feedback submission stored for moderation.

Field summary:

- `id`
- `chargerId`
- `rating`
- `feedbackType`
- `message`
- `displayName`
- `city`
- `reportedByContact`
- `feedbackStatus`
- `reviewedAt`
- `reviewedBy`
- audit fields

`feedbackStatus` is a Java enum with values: `PENDING`, `APPROVED`, `REJECTED`, `SPAM`, and `APPLIED`.

`feedbackType` is a Java enum with controlled values: `WORKING`, `NOT_WORKING`, `CONNECTOR_UNAVAILABLE`, `PRICE_CHANGED`, `ACCESS_ISSUE`, `LOCATION_WRONG`, `CLOSED_OR_REMOVED`, and `OTHER`.

Public submissions default to `PENDING`. Protected admin APIs can read submissions and update `feedbackStatus`, `reviewedAt`, `reviewedBy`, and update audit fields. Public approved feedback retrieval returns only safe public fields and excludes `reportedByContact`, moderation metadata, status, and audit user fields. Charger feedback does not affect public rating aggregates and does not update charger status. `reportedByContact` is internal-only and must be kept out of any public response DTOs.

## Data Conventions

- All entity IDs should be `BIGINT` in PostgreSQL and `Long` in Java.
- Do not use UUID primary keys.
- Every entity should include audit fields:
  - `createdAt`
  - `createdBy`
  - `updatedAt`
  - `updatedBy`
- Every JPA entity should include equals and hashCode.
- Admin moderation UI is deferred.
- Vehicle `sourceUrl`, `sourceLabel`, and `sourceCheckedAt` fields remain internal. Vehicle public response DTOs expose `verificationStatus` only for frontend source-confidence badges.
- Charger `sourceUrl`, `sourceLabel`, and `sourceCheckedAt` fields remain internal. Charger public response DTOs expose `verificationStatus` only for frontend source-confidence badges.
- Vehicle `batteryCapacityKwh` should preserve up to 3 decimal places.

## Future Planned Reviews And Feedback

Vehicle review submission persistence, protected admin moderation APIs, approved-only rating summaries, approved-only public review retrieval, and pending-only charger feedback submission persistence exist. Charger feedback uses a separate `ChargerFeedback` entity rather than storing user-submitted feedback directly on `Charger`.

Future review/feedback entities should follow the same ID and audit conventions: `BIGINT` / `Long` primary keys, `createdAt`, `createdBy`, `updatedAt`, and `updatedBy`. Moderation metadata such as status, moderator, timestamp, and reason should be included when the feature is implemented. Avoid entity relationships unless the implementation later needs them.

Public rating aggregates should be computed from approved records only. Pending, rejected, spam, and unmoderated records must not affect public averages or public detail-page comments.
