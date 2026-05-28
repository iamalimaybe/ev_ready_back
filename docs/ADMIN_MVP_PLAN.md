# Backend Admin MVP Plan

This document plans the backend Admin MVP for EVReady Pakistan. The minimal admin authentication foundation now exists, but admin data APIs, schema changes, and frontend UI are still deferred.

## Goal

The Admin MVP should give trusted EVReady operators a protected way to review and manage production operational data that is already collected or displayed by the backend:

- Get Help lead submissions.
- Contact Us submissions.
- Later, basic vehicle catalog visibility/order/content.
- Later, basic charger directory visibility/order/content.

The first version should reduce direct database access without exposing personal user data or internal catalog management fields to the public frontend.

## Non-Goals

Do not build these first:

- Public admin routes.
- Open retrieval APIs for leads or contact submissions.
- Ratings/reviews.
- Payments.
- Booking.
- SLA or callback guarantee workflows.
- Complex CRM workflows such as assignment queues, reminders, pipelines, or multi-step automations.
- Live charger availability management.
- Field verification claims for vehicles or chargers.

## Why Auth Comes First

Admin retrieval endpoints would expose sensitive data that is not part of the public API contract. Get Help and Contact Us records include names, phone numbers, emails, organizations, free-text messages, and source pages.

No admin read or write endpoint should exist without authentication and access control. CORS is not authentication, frontend route hiding is not authorization, and private URL paths are not protection.

Lead/contact retrieval must not be public.

## Suggested First Admin Scope

First scope:

- View Get Help leads.
- View Contact Us submissions.
- Filter/search by status, date, name, phone/email, city, interest type, inquiry type, and source page where appropriate.
- Update lead/contact status.
- Add internal notes only if schema planning supports it later.

Later scope:

- Manage vehicle active state.
- Manage vehicle display order.
- Manage basic vehicle catalog fields after data-quality workflows are clearer.
- Manage charger active state.
- Manage charger display order.
- Manage basic charger directory fields after data-quality workflows are clearer.

Vehicle and charger management should remain conservative. Vehicle source confidence does not mean field verification, and charger status is not live availability.

## Security And Privacy Risks

- Lead/contact records contain personal contact data and free-text user messages.
- Admin APIs must be protected before they exist.
- Logs should not include full message bodies, phone numbers, emails, credentials, or exported personal data.
- Public frontend responses must not include admin-only fields.
- Source/provenance fields should stay internal unless an intentional public or admin contract is designed.
- CSV exports can leak personal data and should be limited to trusted operators.
- Admin write actions need auditability before they are used for production data.

## Auth Planning Options

### Minimal Username/Password Login

Pros:
- Smallest mental model for first admin access.
- Easy for a very small operator group to understand.
- Can be paired with a simple `ADMIN` role.

Cons:
- Requires careful password storage, login throttling decisions, session/token expiry, and credential rotation.
- Still needs secure transport, protected routes, and safe logout behavior.

### JWT

Pros:
- Works well for API-first clients.
- Can be stateless on the backend.
- Familiar for frontend/API integration.

Cons:
- Token storage and revocation need careful handling.
- Long-lived tokens can become risky.
- More moving parts if refresh tokens are introduced.

### Session Cookie

Pros:
- Good fit for a browser-only admin UI.
- Server can invalidate sessions.
- HttpOnly/Secure/SameSite cookies reduce frontend token exposure.

Cons:
- Requires session storage/management decisions.
- CSRF posture must be considered for state-changing admin actions.

## Auth Recommendation

The first version uses a protected browser-oriented username/password login with a server-controlled session cookie. Credentials come from `ADMIN_USERNAME` and `ADMIN_PASSWORD` environment variables only. Start with one `ADMIN` role and keep the flow small.

Do not introduce multiple auth modes in the MVP.

## Role Model

Start with:

- `ADMIN`

Defer granular permissions until there is a real operating need. Separate roles such as support, catalog editor, charger editor, or read-only reviewer can be added later if the team grows or workflows require them.

## Future Protected Admin API Groups

Implemented auth foundation endpoints:

- `/api/v1/admin/auth`
  - Login/logout/current admin session.

Future protected admin API groups, not implemented yet:

- `/api/v1/admin/leads`
  - List, filter, view detail, update status, later add internal notes.
- `/api/v1/admin/contact-submissions`
  - List, filter, view detail, update status, later add internal notes.
- `/api/v1/admin/vehicles`
  - Later: list/edit basic catalog fields, active state, display order.
- `/api/v1/admin/chargers`
  - Later: list/edit basic directory fields, active state, display order.

All admin API groups must be protected. Public frontend clients must not call or receive admin data.

## Data Model Planning

Existing fields are enough for initial read-only review of:

- Lead submissions.
- Contact submissions.
- Vehicle catalog records.
- Charger directory records.

Possible future fields for lead/contact operations:

- Status for contact submissions.
- Internal notes.
- Reviewed/contacted/resolved timestamps.
- Reviewed by / updated by admin identity.
- Closed reason or spam marker.

Possible future fields for vehicle and charger operations:

- Last reviewed at.
- Last reviewed by.
- Internal source notes.
- Reason for verification status change.
- Charger operator-confirmed at.
- Charger user-reported at.

These are future schema planning candidates only. Do not create migrations until the admin workflows and exact fields are chosen.

## Frontend Coordination

- Admin UI should be separate and protected.
- Public frontend must not receive admin-only data.
- Public pages should continue using existing public APIs.
- Public vehicle wording should remain conservative because source confidence is not EVReady field verification.
- Public charger wording should remain conservative because charger status is not live availability.

## Rollout Strategy

1. Plan admin scope and auth/access-control rules.
2. Implement auth foundation.
3. Implement read-only admin views for leads and contact submissions.
4. Implement status updates and internal notes after schema decisions.
5. Add vehicle catalog management later.
6. Add charger directory management later.

Each step should be small, reviewed, and deployed separately.
