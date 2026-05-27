# API Contract

Planned first-release APIs only. Keep this contract simple for now; do not add OpenAPI setup yet.

Public read APIs return active records only.

Vehicle source/provenance fields `sourceUrl`, `sourceLabel`, and `sourceCheckedAt` remain internal data-quality fields and are not part of public response DTOs. Vehicle public response DTOs include `verificationStatus` for frontend source-confidence badges.

Charger source/provenance fields (`sourceUrl`, `sourceLabel`, `sourceCheckedAt`, `verificationStatus`) are internal data-quality fields and are not part of public response DTOs yet.

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

Public vehicle list responses include `verificationStatus`. Frontend clients use this value for source-confidence badges. `OFFICIAL` means the vehicle data came from an official OEM, operator, or distributor source; the UI must not describe it as "Verified" or imply EVReady personally audited vehicle specs or prices.

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

## Chargers

### `GET /api/v1/chargers`

Returns a list of active chargers for the directory. Chargers whose charger type is inactive are not returned.

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

Public or admin Contact Us retrieval APIs are not part of the first release.

## Error Response Format

API errors use a shared JSON response shape:

- `timestamp`
- `status`
- `error`
- `message`
- `path`
- `fieldErrors`, optional

Validation errors may include `fieldErrors` with field names and user-friendly validation messages.
