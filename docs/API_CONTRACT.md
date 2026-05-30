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

When neither `page` nor `size` is provided, this endpoint preserves the legacy response and returns a plain JSON array.

When `page` or `size` is provided, this endpoint returns a stable page response:

- `content`
- `page`
- `size`
- `totalElements`
- `totalPages`

Pagination parameters:

- `page`, optional, default `0` when pagination is requested
- `size`, optional, default `6` when pagination is requested, capped at `50`

Public vehicle list responses include `verificationStatus`. Frontend clients use this value for source-confidence badges. `OFFICIAL` means the vehicle data came from an official OEM, operator, or distributor-type source; the UI must not describe it as "Verified" or imply EVReady personally audited vehicle specs or prices.

Public vehicle list responses include `ratingSummary` based only on approved vehicle reviews:

- `averageRating`
- `ratingCount`

`ratingCount` counts approved reviews only. `averageRating` is rounded to max 1 decimal place and is `null` when there are no approved reviews. Pending, rejected, and spam reviews must not affect public rating summaries.

`sourceUrl` and `sourceLabel` remain internal and are not exposed in public vehicle responses. Missing or `null` `verificationStatus` values should be treated by frontend clients as `UNVERIFIED`.

Supported filters:

- `type`
- `brandId`
- `chargerTypeId`
- `priceMax`
- `rangeMin`
- `dcFastCharging`
- `page`
- `size`

Supported sort values:

- default: `displayOrder` ascending, then `id` ascending
- `priceAsc`
- `priceDesc`
- `rangeAsc`
- `rangeDesc`

### `GET /api/v1/vehicles/{id}`

Returns a single active vehicle by ID, including brand and charger type display info. Returns 404 when the vehicle does not exist or is not active.

Public vehicle detail responses include `verificationStatus` and `ratingSummary` with the same frontend handling rules as vehicle list responses.

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

### `GET /api/v1/vehicles/{vehicleId}/reviews`

Returns approved vehicle reviews for a public vehicle detail page. Returns `404` when the vehicle does not exist or is not active.

Only reviews with `reviewStatus = APPROVED` are returned. Pending, rejected, and spam reviews are never returned by this endpoint. This endpoint does not imply EVReady verified user-submitted claims.

Query parameters:

- `page`, optional, default `0`
- `size`, optional, default `10`, capped at `50`

Returns a stable page response:

- `content`
- `page`
- `size`
- `totalElements`
- `totalPages`

Public approved review records include only safe public fields:

- `id`
- `rating`
- `reviewText`
- `displayName`
- `city`
- `experienceType`
- `createdAt`

Public approved review records do not include moderation metadata, `reviewStatus`, audit user fields, or internal/admin fields.

## Chargers

### `GET /api/v1/chargers`

Returns a list of active chargers for the directory. Chargers whose charger type is inactive are not returned.

When neither `page` nor `size` is provided, this endpoint preserves the legacy response and returns a plain JSON array.

When `page` or `size` is provided, this endpoint returns a stable page response:

- `content`
- `page`
- `size`
- `totalElements`
- `totalPages`

Pagination parameters:

- `page`, optional, default `0` when pagination is requested
- `size`, optional, default `6` when pagination is requested, capped at `50`

Public charger list responses include `verificationStatus`. Frontend clients use this value for source-confidence badges. `OFFICIAL` means the charger data came from an official operator, network, distributor, or similar source; the UI must not imply live charger availability, verified price, guaranteed operational status, or EVReady field verification.

`sourceUrl`, `sourceLabel`, and `sourceCheckedAt` remain internal and are not exposed in public charger responses. Missing or `null` `verificationStatus` values should be treated by frontend clients as `UNVERIFIED`.

Supported filters:

- `city`
- `chargerTypeId`
- `chargingType`
- `status`
- `page`
- `size`

Supported sort values:

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

### `GET /api/v1/chargers/feedback-types`

Returns public charger feedback type options for frontend controls as a plain JSON array. Values are derived from the backend `ChargerFeedbackType` enum.

Each option includes:

- `value`
- `label`

Current values:

- `WORKING`
- `NOT_WORKING`
- `CONNECTOR_UNAVAILABLE`
- `PRICE_CHANGED`
- `ACCESS_ISSUE`
- `LOCATION_WRONG`
- `CLOSED_OR_REMOVED`
- `OTHER`

This endpoint exposes feedback type choices only. It does not expose moderation status options, public feedback content, or live charger availability.

### `GET /api/v1/chargers/{chargerId}/feedback`

Returns approved charger feedback for a public charger detail page. Returns `404` when the charger does not exist or is not active.

Only feedback with `feedbackStatus = APPROVED` is returned. Pending, rejected, spam, applied, and other non-public feedback records are never returned by this endpoint. This endpoint does not imply EVReady verified user-submitted claims or live charger availability.

Query parameters:

- `page`, optional, default `0`
- `size`, optional, default `10`, capped at `50`

Returns a stable page response:

- `content`
- `page`
- `size`
- `totalElements`
- `totalPages`

Public approved charger feedback records include only safe public fields:

- `id`
- `rating`
- `feedbackType`
- `message`
- `displayName`
- `city`
- `createdAt`

Public approved charger feedback records do not include `reportedByContact`, `feedbackStatus`, moderation metadata, audit user fields, or internal/admin fields.

### `POST /api/v1/chargers/{chargerId}/feedback`

Stores a public charger feedback submission with default `PENDING` moderation status. This endpoint does not publish feedback, expose approved feedback publicly, calculate public charger rating aggregates, update charger status, or imply live charger availability.

Returns `404` when the charger does not exist or is not active.

Request fields:

- `rating`
- `feedbackType`
- `message`
- `displayName`
- `city`
- `reportedByContact`

Validation:

- `rating` is optional, but when provided must be an integer from 1 to 5.
- `feedbackType` is required and must be a valid `ChargerFeedbackType`.
- `message` must be 2000 characters or fewer.
- `displayName` must be 120 characters or fewer.
- `city` must be 100 characters or fewer.
- `reportedByContact` must be 160 characters or fewer and is internal-only.

Allowed `feedbackType` values:

- `WORKING`
- `NOT_WORKING`
- `CONNECTOR_UNAVAILABLE`
- `PRICE_CHANGED`
- `ACCESS_ISSUE`
- `LOCATION_WRONG`
- `CLOSED_OR_REMOVED`
- `OTHER`

Returns `201 Created` with:

- `id`
- `feedbackStatus`
- `message`

`feedbackStatus` is `PENDING` for public submissions. The response message must make clear that feedback was submitted for moderation and is not published.

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

## Protected Admin Charger Directory APIs

These endpoints require an active admin session. They expose charger correction fields to trusted admins only and must not be public.

### `GET /api/v1/admin/chargers`

Returns charger directory records, including active and inactive records, paginated with the same stable page response shape as other admin reads.

Query parameters:

- `page`, optional, default `0`
- `size`, optional, default `20`, capped at `100`
- `active`, optional
- `city`, optional
- `status`, optional
- `verificationStatus`, optional

Results are ordered by `displayOrder` ascending, then `id` ascending.

Admin charger records include:

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
- `createdAt`
- `createdBy`
- `updatedAt`
- `updatedBy`

### `GET /api/v1/admin/chargers/{id}`

Returns one charger record for admin editing. Returns `404` when the charger record does not exist.

### `GET /api/v1/admin/chargers/form-options`

Returns admin charger form options as a plain JSON object so admin clients do not hardcode charger type or enum choices.

Response fields:

- `chargerTypes`
- `chargingTypes`
- `chargerStatuses`
- `verificationStatuses`

`chargerTypes` includes active charger type records only. Enum option arrays include:

- `value`
- `label`

### `POST /api/v1/admin/chargers`

Creates one charger directory record. Returns `201 Created` with the created admin charger record.

This protected admin endpoint is called by browser clients with session credentials, so backend CORS allowed methods must include `POST`.

Request fields:

- `chargerTypeId`
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

Validation is the same as `PATCH /api/v1/admin/chargers/{id}`, except `active` may be omitted and defaults to `true` when the rest of the request is valid.

Creating a charger record does not imply the charger is currently working, available, unoccupied, compatible, or priced as shown. `status` is reported/non-live status. `verificationStatus` is source confidence only, not EVReady field verification.

### `PATCH /api/v1/admin/chargers/{id}`

Updates editable charger directory fields for one existing charger record. Returns the updated admin charger record. Returns `404` when the charger record does not exist.

This protected admin endpoint is called by browser clients with session credentials, so backend CORS allowed methods must include `PATCH`.

Request fields:

- `chargerTypeId`
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

Validation:

- `chargerTypeId` is required and must refer to an active charger type.
- `name` is required and must be 150 characters or fewer.
- `city` is required and must be 100 characters or fewer.
- `area` must be 150 characters or fewer.
- `latitude` may be `null`; when provided, it must be between -90 and 90.
- `longitude` may be `null`; when provided, it must be between -180 and 180.
- `chargingType` is required and must be a valid `ChargingType`.
- `status` is required and must be a valid `ChargerStatus`.
- `powerKw` may be `null`; when provided, it must be greater than 0.
- `priceNote` must be 255 characters or fewer.
- `description` is required.
- `image` must be 255 characters or fewer.
- `sourceUrl` must be 500 characters or fewer.
- `sourceLabel` must be 150 characters or fewer.
- `verificationStatus` is required and must be a valid `VerificationStatus`.
- `active` is required.
- `displayOrder` is required and must be 0 or greater.

Updating a charger record does not imply the charger is currently working, available, unoccupied, compatible, or priced as shown. `status` remains reported/non-live status. `verificationStatus` remains source confidence only, not EVReady field verification. Charger feedback is never applied automatically to charger records.

## Protected Admin Charger Feedback APIs

These endpoints require an active admin session. They expose submitted charger feedback and internal contact data to trusted admins only and must not be public.

### `GET /api/v1/admin/charger-feedback`

Returns charger feedback submissions newest-first, paginated with the same stable page response shape as admin lead/contact/review reads.

Query parameters:

- `page`, optional, default `0`
- `size`, optional, default `20`, capped at `100`
- `feedbackStatus`, optional
- `chargerId`, optional

Filtering is applied by repository query, not in memory. When `feedbackStatus` is provided, it must match a `ChargerFeedbackStatus` enum value.

Admin charger feedback records include:

- `id`
- `chargerId`
- `chargerName`
- `rating`
- `feedbackType`
- `message`
- `displayName`
- `city`
- `reportedByContact`
- `feedbackStatus`
- `reviewedAt`
- `reviewedBy`
- `createdAt`
- `createdBy`
- `updatedAt`
- `updatedBy`

### `GET /api/v1/admin/charger-feedback/statuses`

Returns charger feedback moderation status options for admin UI controls as a plain JSON array. Values are derived from the backend `ChargerFeedbackStatus` enum.

Each option includes:

- `value`
- `label`

### `GET /api/v1/admin/charger-feedback/{id}`

Returns one charger feedback submission for admins. Returns `404` when the feedback record does not exist.

### `PATCH /api/v1/admin/charger-feedback/{id}/status`

Updates moderation status and audit metadata for one charger feedback submission. Returns the updated admin charger feedback record. Returns `404` when the feedback record does not exist.

This protected admin endpoint is called by browser clients with session credentials, so backend CORS allowed methods must include `PATCH`.

Request fields:

- `feedbackStatus`

Validation:

- `feedbackStatus` is required and must be a valid `ChargerFeedbackStatus`.

Allowed `feedbackStatus` values:

- `PENDING`
- `APPROVED`
- `REJECTED`
- `SPAM`
- `APPLIED`

When feedback moves out of `PENDING`, the backend sets `reviewedAt` and `reviewedBy` from the authenticated admin principal. Updating feedback status does not update charger public status, publish feedback publicly, calculate charger rating aggregates, or imply live charger availability.

## Future Planned Reviews And Feedback APIs

Public vehicle review submission, vehicle review experience type options, protected admin vehicle review moderation APIs, approved-only vehicle rating summaries, approved-only public vehicle review retrieval, pending-only public charger feedback submission, public approved-only charger feedback retrieval, and protected admin charger feedback moderation APIs are implemented above. This section is planning-only and must not be treated as an active API contract except where an endpoint is documented elsewhere as implemented.

Implemented public submit endpoints include:

- `POST /api/v1/chargers/{id}/feedback` - stores `PENDING` submissions only and does not publish feedback

Public submit endpoints should create pending submissions only. They should not publish the submitted content, expose internal moderation data, or imply EVReady has verified user claims.

Future public approved-only aggregate endpoints may support listing cards:

- `GET /api/v1/chargers/{id}/feedback-summary` or `GET /api/v1/chargers/{id}/rating-summary` if charger star ratings are allowed

Listing-card aggregates should include only average rating, stars out of 5, and rating count. Average ratings must use approved reviews only and should display with max 1 decimal place. Pending, rejected, spam, and unmoderated records must not affect public averages.

Implemented public approved-only review/comment endpoints include:

- `GET /api/v1/chargers/{id}/feedback` - approved feedback only

These endpoints must not return unmoderated content or internal-only contact fields. Detail-page comments must not imply EVReady verifies every user-submitted claim. Charger feedback/comments must not imply live charger availability.

Implemented protected admin moderation endpoints include:

- `GET /api/v1/admin/charger-feedback` - protected admin paginated list
- `GET /api/v1/admin/charger-feedback/statuses` - protected admin status options
- `GET /api/v1/admin/charger-feedback/{id}` - protected admin detail
- `PATCH /api/v1/admin/charger-feedback/{id}/status` - protected admin moderation update

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
