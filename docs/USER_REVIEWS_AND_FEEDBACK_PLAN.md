# User Reviews And Feedback Plan

Planning document only. This does not implement reviews, ratings, charger feedback, moderation, authentication, schema changes, API endpoints, or frontend pages.

Vehicle and charger public DTOs currently expose `verificationStatus` as source-confidence only. Future user-submitted content must preserve that distinction: reviews and feedback are community signals, not EVReady field verification.

## Goals

- Plan vehicle ratings and optional reviews without adding public user authentication in the first version.
- Plan charger feedback carefully because charger data can affect travel decisions.
- Keep public listing cards simple: average rating, stars out of 5, and rating count only.
- Use dedicated detail pages for full details and approved review/comment display.
- Require admin moderation before user-submitted content affects public display or public summaries.

## Non-Goals

- No implementation in this task.
- No public user auth yet.
- No entities, repositories, services, controllers, migrations, or frontend code.
- No public display of pending, rejected, spam, or otherwise unmoderated content.
- No fake or static reviews.
- No modal-based detail/review display as the main user experience.
- No automatic public charger status changes from user feedback.
- No claim that EVReady verifies every user-submitted statement.

## Public User Authentication Decision

Do not implement public user authentication in the first review/feedback version.

Public users may later submit vehicle reviews or charger feedback without login. Submissions should default to `PENDING` and require moderation before public display or aggregate use.

Optional submitter fields may include:

- `displayName`
- `city`
- optional contact field for internal follow-up only

Contact fields must not be displayed publicly. Public auth can be reconsidered later only if abuse, ownership, edit/delete, contributor trust, or legal/reputation requirements justify it.

## Vehicle Ratings And Reviews

Vehicle reviews are lower operational risk than charger feedback, but still require moderation because public reviews can be fake, abusive, defamatory, or misleading.

Future behavior:

- Users may rate vehicles from 1 to 5 stars.
- Users may optionally submit text reviews.
- Submissions default to `PENDING`.
- Reviews must not be displayed publicly until moderation rules and admin workflows exist.
- Pending, rejected, and spam reviews must not affect public averages.
- Public average ratings must use approved reviews only.
- Public text reviews/comments must be approved before display.
- User comments must not imply EVReady verified every ownership, range, price, or charging claim.

Basic fields likely needed later:

- `vehicleId`
- `rating`
- `reviewText`
- `displayName`, optional
- `city`, optional
- `ownershipStatus` or `experienceType`, optional
- `reviewStatus`, such as `PENDING`, `APPROVED`, `REJECTED`, `SPAM`
- `createdAt`
- `createdBy`
- `updatedAt`
- `updatedBy`
- `moderatedAt`
- `moderatedBy`
- `moderationReason`

## Charger Feedback And Reviews

Charger feedback is higher risk than vehicle reviews because users may rely on it before travel. Charger feedback should be framed as community reporting or feedback, not live reliability or guaranteed availability.

Future behavior:

- Users may later report charger working/not working observations.
- Users may report connector unavailable or wrong.
- Users may report price changes.
- Users may report access issues.
- Users may report location or coordinates wrong.
- Users may report charger removed or closed.
- Users may submit another note when predefined feedback types do not fit.
- Feedback defaults to `PENDING`.
- Feedback should first go to admin review.
- Admin review is required before user feedback affects visible content.
- User feedback must not automatically change public charger status.
- User comments must not imply live charger availability.
- If star ratings are used for chargers, label them as community feedback, not live reliability.

Basic fields likely needed later:

- `chargerId`
- `rating`, optional if charger star ratings are allowed
- `feedbackType`
- `message`
- `displayName`, optional
- `city`, optional
- `reportedByContact`, optional/internal only
- `feedbackStatus`, such as `PENDING`, `APPROVED`, `REJECTED`, `SPAM`, `APPLIED`
- `createdAt`
- `createdBy`
- `updatedAt`
- `updatedBy`
- `reviewedAt`
- `reviewedBy`

## Public Listing-Card Aggregate Display

Future listing cards for vehicles and chargers should show only:

- average rating
- stars out of 5
- rating count

Aggregate rules:

- Average rating must use approved reviews only.
- Average rating should display with max 1 decimal place.
- Pending, rejected, spam, and unmoderated reviews must not affect public averages.
- A zero-review state should not fabricate a rating.
- Listing cards should not show full review text or comments.
- Listing cards should not imply EVReady verified user-submitted claims.

## Dedicated Detail Pages

The frontend direction is dedicated detail pages rather than modal-based detail/review display.

Future planned routes:

- `/vehicles/:id`
- `/chargers/:id`

Detail pages should show existing entity details plus approved user-submitted content. They should not use fake/static reviews and should not show unmoderated content.

### Vehicle Detail Page

Future route: `/vehicles/:id`

Should show:

- existing vehicle details
- source-confidence warning
- average rating
- rating count
- approved user comments/reviews

The page should not imply EVReady verified every user claim about ownership, price, availability, range, battery, or charging behavior.

### Charger Detail Page

Future route: `/chargers/:id`

Should show:

- existing charger details
- source-confidence warning
- non-live charger status warning
- average rating or feedback summary
- approved comments/feedback

The page must preserve clear warning language that charger status is not live availability. User comments must not imply the charger is working right now, available, unoccupied, compatible, or priced as shown. Admin review is required before feedback affects visible content.

## Admin Moderation

Admin moderation is required before reviews, comments, ratings, or charger feedback affect public display or aggregate summaries.

Future admin workflows should support:

- listing pending vehicle reviews
- approving, rejecting, or marking vehicle reviews as spam
- listing pending charger feedback
- approving, rejecting, marking as spam, or applying charger feedback
- storing moderation/review metadata
- preventing internal contact fields from public display
- keeping public averages based only on approved records

Moderation should be protected behind admin authentication. Browser-based admin moderation methods must also be reflected in Spring Security CORS allowed methods when implemented.

## Moderation And Abuse Risks

- Fake reviews
- Competitor manipulation
- Spam
- Offensive or defamatory content
- Personal information in free text
- Repeated submissions
- Public trust damage
- Legal/reputation risk

Free-text fields should be treated as untrusted user input. Future public display should avoid exposing phone numbers, emails, addresses, or other personal data from review text when moderation catches it.

## Suggested Phased Rollout

- Phase 1: Planning only.
- Phase 2: Backend persistence for vehicle reviews with default `PENDING` status.
- Phase 3: Admin moderation view for pending reviews.
- Phase 4: Public approved-only aggregate ratings on listing cards.
- Phase 5: Vehicle detail page with approved reviews.
- Phase 6: Charger feedback persistence and admin moderation.
- Phase 7: Charger detail page with approved feedback/comments.

Public user auth remains deferred unless abuse, ownership, edit/delete, or trust needs justify it.

## Future API Planning

All endpoints in this section are future/planned only.

Public submit endpoints:

- `POST /api/v1/vehicles/{id}/reviews`
- `POST /api/v1/chargers/{id}/feedback`

Public approved-only aggregate endpoints:

- `GET /api/v1/vehicles/{id}/rating-summary`
- `GET /api/v1/chargers/{id}/feedback-summary` or `GET /api/v1/chargers/{id}/rating-summary` if charger star ratings are allowed

Public approved-only detail-page content endpoints:

- `GET /api/v1/vehicles/{id}/reviews`
- `GET /api/v1/chargers/{id}/feedback`

Protected admin moderation endpoints:

- `GET /api/v1/admin/vehicle-reviews`
- `PATCH /api/v1/admin/vehicle-reviews/{id}/status`
- `GET /api/v1/admin/charger-feedback`
- `PATCH /api/v1/admin/charger-feedback/{id}/status`

API rules:

- Future public submit endpoints should return a safe submission acknowledgement, not published content.
- No public endpoint should retrieve unmoderated content.
- Public approved aggregate endpoints should include only approved records.
- Public approved review/comment endpoints should support detail pages and exclude internal-only contact fields.
- Protected admin moderation endpoints must require an active admin session.

## Future Data Model Planning

Likely future entities:

- `VehicleReview`
- `ChargerFeedback`

Do not prescribe exact migrations yet. When implemented, follow the existing backend conventions:

- IDs should be `BIGINT` in PostgreSQL and `Long` in Java.
- Include audit fields: `createdAt`, `createdBy`, `updatedAt`, `updatedBy`.
- Include equals and hashCode for each entity.
- Avoid entity relationships unless implementation later requires them.
- Keep moderation metadata on the submitted record unless a later implementation needs a separate audit/history model.
- Keep internal contact fields out of public DTOs.

