# API Contract

Planned first-release APIs only. Keep this contract simple for now; do not add OpenAPI setup yet.

Public read APIs return active records only.

Vehicle source/provenance fields `sourceUrl`, `sourceLabel`, and `sourceCheckedAt` remain internal data-quality fields and are not part of public response DTOs. Vehicle public response DTOs include `verificationStatus` for frontend source-confidence badges.

Charger source/provenance fields `sourceUrl`, `sourceLabel`, and `sourceCheckedAt` remain internal data-quality fields and are not part of public response DTOs. Charger public response DTOs include `verificationStatus` for frontend source-confidence badges.

For vehicle and charger public DTOs, `OFFICIAL` means the data is source-backed from an official, operator, or distributor-type source. It does not mean EVReady personally audited, field-verified, or guarantees the data. Public API responses must not imply live charger availability, verified prices, or guaranteed charger status.

## Response Contract

- Successful list endpoints return plain JSON arrays.
- Empty list responses are valid and should be handled as `[]`.
- Successful detail endpoints return plain JSON objects.
- Successful create endpoints return their documented response DTO.
- Success responses are not wrapped in a common `ApiResponse` envelope.
- Frontend clients should show empty states when list endpoints return `[]`.
- `404` means a missing or inactive record for public read APIs.
- Error responses use the shared error envelope documented below.
- Frontend clients should read `fieldErrors` for form validation messages.

## Brands

### `GET /api/v1/brands`

Returns active brands ordered by `displayOrder` ascending, then `name` ascending.

## Charger Types

### `GET /api/v1/charger-types`

Returns active charger types ordered by `displayOrder` ascending, then `name` ascending.

## Vehicles

### `GET /api/v1/vehicles`

Returns a list of vehicles for the catalog.

Public vehicle list responses include `verificationStatus`. Frontend clients use this value for source-confidence badges. `OFFICIAL` means the vehicle data came from an official OEM, operator, or distributor-type source; the UI must not describe it as "Verified" or imply EVReady personally audited vehicle specs or prices.

`sourceUrl` and `sourceLabel` remain internal and are not exposed in public vehicle responses. Missing or `null` `verificationStatus` values should be treated by frontend clients as `UNVERIFIED`.

Planned filters:

- `type`
- `brandId`
- `chargerTypeId`
- `priceMax`
- `rangeMin`
- `dcFastCharging`

Planned sort values:

- default: `displayOrder` ascending, then `id` ascending
- `priceAsc`
- `priceDesc`
- `rangeAsc`
- `rangeDesc`

### `GET /api/v1/vehicles/{id}`

Returns a single active vehicle by ID, including brand and charger type display info. Returns 404 when the vehicle does not exist or is not active.

Public vehicle detail responses include `verificationStatus` with the same frontend handling rules as vehicle list responses.

### `GET /api/v1/vehicles/reviews/experience-types`

Returns public vehicle review experience type options for frontend dropdowns as a plain JSON array. Values are derived from the backend `VehicleReviewExperienceType` enum.

Each option includes:

- `value`
- `label`

Current values:

- `OWNER`
- `FORMER_OWNER`
- `TEST_DRIVE`
- `BOOKED`
- `CONSIDERING`
- `RESEARCH_ONLY`
- `OTHER`

### `POST /api/v1/vehicles/{vehicleId}/reviews`

Stores a public vehicle review submission with default `PENDING` moderation status. This endpoint does not publish the review, expose it publicly, update vehicle DTOs, calculate average ratings, or imply EVReady verified the user-submitted claim.

Returns `404` when the vehicle does not exist or is not active.

Request fields:

- `rating`
- `reviewText`
- `displayName`
- `city`
- `experienceType`

Validation:

- `rating` is required and must be an integer from 1 to 5.
- `reviewText` must be 2000 characters or fewer.
- `displayName` must be 120 characters or fewer.
- `city` must be 100 characters or fewer.
- `experienceType` is required and must be a valid `VehicleReviewExperienceType`.

Returns `201 Created` with:

- `id`
- `reviewStatus`
- `message`

`reviewStatus` is `PENDING` for public submissions. The response message must make clear that the review was submitted for moderation and is not published.

## Chargers

### `GET /api/v1/chargers`

Returns a list of active chargers for the directory. Chargers whose charger type is inactive are not returned.

Public charger list responses include `verificationStatus`. Frontend clients use this value for source-confidence badges. `OFFICIAL` means the charger data came from an official operator, network, distributor, or similar source; the UI must not imply live charger availability, verified price, guaranteed operational status, or EVReady field verification.

`sourceUrl`, `sourceLabel`, and `sourceCheckedAt` remain internal and are not exposed in public charger responses. Missing or `null` `verificationStatus` values should be treated by frontend clients as `UNVERIFIED`.

Planned filters:

- `city`
- `chargerTypeId`
- `chargingType`
- `status`

Planned sort values:

- default: `displayOrder` ascending, then `id` ascending
- `nameAsc`
- `cityAsc`
- `powerDesc`
- `powerAsc`

### `GET /api/v1/chargers/{id}`

Returns a single active charger by ID, including charger type display info. Returns 404 when the charger does not exist or is not active.

Public charger detail responses include `verificationStatus` with the same frontend handling rules as charger list responses.

### `GET /api/v1/chargers/cities`

Returns a plain JSON array of distinct city names from active charger directory records, sorted ascending.

If no active charger records exist, returns `[]`.

## Leads

### `POST /api/v1/leads`

Stores a Get Help / lead capture submission.

Request fields:

- `name`
- `phone`
- `city`
- `interestType`
- `message`
- `sourcePage`
- `consentAccepted`

Validation:

- `name` is required and must be 120 characters or fewer.
- `phone` is required and must be 40 characters or fewer.
- `city` must be 100 characters or fewer.
- `interestType` is required.
- `message` must be 2000 characters or fewer.
- `sourcePage` must be 120 characters or fewer.
- `consentAccepted` must be `true`.

Returns `201 Created` with:

- `id`
- `leadStatus`
- `message`

Public lead retrieval APIs are not part of the first release.

## Contact Submissions

### `POST /api/v1/contact-submissions`

Stores a Contact Us submission separately from Get Help / lead capture submissions.

Request fields:

- `name`
- `email`
- `phone`
- `organization`
- `inquiryType`
- `message`
- `sourcePage`
- `consentAccepted`

Validation:

- `name` is required and must be 120 characters or fewer.
- `email` must be 160 characters or fewer.
- `phone` must be 40 characters or fewer.
- `organization` must be 160 characters or fewer.
- `inquiryType` is required and must be 80 characters or fewer.
- `message` is required and must be 2000 characters or fewer.
- `sourcePage` must be 120 characters or fewer.
- `consentAccepted` must be `true`.

Returns `201 Created` with:

- `id`
- `message`

Public Contact Us retrieval APIs are not part of the first release. Protected admin retrieval and status management are available for authenticated admins only.

## Protected Admin Auth

Admin auth endpoints are reserved for future admin UI use. They do not expose lead/contact submissions or catalog management data.

### `POST /api/v1/admin/auth/login`

Authenticates the configured admin using environment-backed credentials. On success, creates an admin session cookie and returns a minimal response:

- `authenticated`
- `username`
- `roles`
- `message`

Failed login returns a safe error response and must not expose credential details.

### `POST /api/v1/admin/auth/logout`

Requires an active admin session. Invalidates the current admin session and returns `204 No Content`.

### `GET /api/v1/admin/auth/me`

Requires an active admin session. Returns the current admin session summary:

- `authenticated`
- `username`
- `roles`
- `message`

All other `/api/v1/admin/**` endpoints are protected and must not be public.

## Protected Admin Lead Reads

These endpoints require an active admin session. They expose personal submission data to trusted admins only and must not be public.

### `GET /api/v1/admin/leads`

Returns Get Help lead submissions newest-first, paginated with a stable response shape:

- `content`
- `page`
- `size`
- `totalElements`
- `totalPages`

Query parameters:

- `page`, optional, default `0`
- `size`, optional, default `20`, capped at `100`

Lead admin records include:

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
- `createdAt`
- `updatedAt`

### `GET /api/v1/admin/leads/statuses`

Returns lead status options for admin UI controls as a plain JSON array. Values are derived from the backend `LeadStatus` enum.

Each option includes:

- `value`
- `label`

### `GET /api/v1/admin/leads/{id}`

Returns one Get Help lead submission for admins. Returns `404` when the record does not exist.

### `PATCH /api/v1/admin/leads/{id}/status`

Updates only the `leadStatus` for one Get Help lead submission. Returns the updated admin lead record. Returns `404` when the record does not exist.

This protected admin endpoint is called by browser clients with session credentials, so backend CORS allowed methods must include `PATCH`.

Request fields:

- `leadStatus`

Allowed `leadStatus` values:

- `NEW`
- `CONTACTED`
- `CLOSED`
- `SPAM`

This endpoint does not imply a customer callback, SLA, internal notes workflow, or contact submission status support.

## Protected Admin Contact Submission APIs

These endpoints require an active admin session. They expose personal submission data to trusted admins only and must not be public.

### `GET /api/v1/admin/contact-submissions`

Returns Contact Us submissions newest-first, paginated with the same stable page response shape as admin lead reads.

Query parameters:

- `page`, optional, default `0`
- `size`, optional, default `20`, capped at `100`

Contact admin records include:

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
- `createdAt`
- `updatedAt`

### `GET /api/v1/admin/contact-submissions/statuses`

Returns contact submission status options for admin UI controls as a plain JSON array. Values are derived from the backend `ContactSubmissionStatus` enum.

Each option includes:

- `value`
- `label`

### `GET /api/v1/admin/contact-submissions/{id}`

Returns one Contact Us submission for admins. Returns `404` when the record does not exist.

### `PATCH /api/v1/admin/contact-submissions/{id}/status`

Updates only the `contactStatus` for one Contact Us submission. Returns the updated admin contact submission record. Returns `404` when the record does not exist.

This protected admin endpoint is called by browser clients with session credentials, so backend CORS allowed methods must include `PATCH`.

Request fields:

- `contactStatus`

Allowed `contactStatus` values:

- `NEW`
- `REVIEWED`
- `RESPONDED`
- `CLOSED`
- `SPAM`

This endpoint does not imply a customer callback, SLA, internal notes workflow, or lead status behavior changes.

## Protected Admin Vehicle Review APIs

These endpoints require an active admin session. They expose submitted review text and moderation metadata to trusted admins only and must not be public.

### `GET /api/v1/admin/vehicle-reviews`

Returns vehicle review submissions newest-first, paginated with the same stable page response shape as admin lead/contact reads.

Query parameters:

- `page`, optional, default `0`
- `size`, optional, default `20`, capped at `100`
- `reviewStatus`, optional
- `vehicleId`, optional

Filtering is applied by repository query, not in memory. When `reviewStatus` is provided, it must match a `VehicleReviewStatus` enum value.

Admin vehicle review records include:

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
- `createdAt`
- `createdBy`
- `updatedAt`
- `updatedBy`

### `GET /api/v1/admin/vehicle-reviews/statuses`

Returns vehicle review moderation status options for admin UI controls as a plain JSON array. Values are derived from the backend `VehicleReviewStatus` enum.

Each option includes:

- `value`
- `label`

### `GET /api/v1/admin/vehicle-reviews/{id}`

Returns one vehicle review submission for admins. Returns `404` when the review does not exist.

### `PATCH /api/v1/admin/vehicle-reviews/{id}/status`

Updates moderation status and moderation metadata for one vehicle review submission. Returns the updated admin vehicle review record. Returns `404` when the review does not exist.

This protected admin endpoint is called by browser clients with session credentials, so backend CORS allowed methods must include `PATCH`.

Request fields:

- `reviewStatus`
- `moderationReason`

Validation:

- `reviewStatus` is required and must be a valid `VehicleReviewStatus`.
- `moderationReason` must be 500 characters or fewer.

Allowed `reviewStatus` values:

- `PENDING`
- `APPROVED`
- `REJECTED`
- `SPAM`

Updating a review status does not publish reviews publicly, calculate rating aggregates, add stars to vehicle cards, or imply EVReady verified the user-submitted claim.

## Future Planned Reviews And Feedback APIs

Public vehicle review submission, vehicle review experience type options, and protected admin vehicle review moderation APIs are implemented above. Rating aggregates, public review retrieval, and charger feedback APIs are not implemented yet. This section is planning-only and must not be treated as an active API contract except where an endpoint is documented elsewhere as implemented.

Future public submit endpoints may include:

- `POST /api/v1/chargers/{id}/feedback`

Public submit endpoints should create pending submissions only. They should not publish the submitted content, expose internal moderation data, or imply EVReady has verified user claims.

Future public approved-only aggregate endpoints may support listing cards:

- `GET /api/v1/vehicles/{id}/rating-summary`
- `GET /api/v1/chargers/{id}/feedback-summary` or `GET /api/v1/chargers/{id}/rating-summary` if charger star ratings are allowed

Listing-card aggregates should include only average rating, stars out of 5, and rating count. Average ratings must use approved reviews only and should display with max 1 decimal place. Pending, rejected, spam, and unmoderated records must not affect public averages.

Future public approved-only review/comment endpoints may support dedicated detail pages:

- `GET /api/v1/vehicles/{id}/reviews`
- `GET /api/v1/chargers/{id}/feedback`

These endpoints must not return unmoderated content or internal-only contact fields. Detail-page comments must not imply EVReady verifies every user-submitted claim. Charger feedback/comments must not imply live charger availability.

Future protected admin moderation endpoints may include:

- `GET /api/v1/admin/charger-feedback`
- `PATCH /api/v1/admin/charger-feedback/{id}/status`

Protected moderation endpoints must require an active admin session. Browser-called admin moderation methods must also be reflected in Spring Security CORS allowed methods when implemented.

## Error Response Format

API errors use a shared JSON response shape:

- `timestamp`
- `status`
- `error`
- `message`
- `path`
- `fieldErrors`, optional

Validation errors may include `fieldErrors` with field names and user-friendly validation messages.
