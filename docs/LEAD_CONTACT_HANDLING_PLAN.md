# Lead And Contact Handling Plan

This plan documents the current production handling for Get Help leads and Contact Us submissions. It is planning documentation only; it does not add email delivery, admin APIs, auth, schema changes, or public abuse protection.

## Current State

- Get Help submissions are accepted through the public `POST /api/v1/leads` endpoint and stored in PostgreSQL.
- Contact Us submissions are accepted through the public `POST /api/v1/contact-submissions` endpoint and stored separately in PostgreSQL.
- Public retrieval APIs are not part of the current release.
- Admin retrieval APIs and admin UI are not part of the current release.
- Cloudflare Email Routing is configured and tested for receiving and forwarding:
  - `info@evready.pk`
  - `contact@evready.pk`
  - `leads@evready.pk`
- Cloudflare Email Routing is inbound/forwarding only. It does not provide outbound SMTP for backend notifications.
- The backend does not send outbound lead/contact notification emails yet.

## Immediate Manual Handling

- Treat PostgreSQL as the source of truth for website form submissions.
- Query or export lead/contact rows from the database when follow-up is needed.
- Keep exports limited to trusted operators because submissions contain personal contact data.
- Use forwarded email addresses for direct inbound human emails only.
- Do not assume every DB submission also generated an email notification; that is not implemented.

## Future Options

### Lightweight DB Export Or Report Task

Pros:
- Lowest implementation risk.
- Keeps PostgreSQL as the source of truth.
- Can support manual operations before a full admin panel exists.
- Avoids outbound email provider work.

Cons:
- Still requires someone with safe server/database access.
- Manual exports can become messy as volume grows.
- Needs care to avoid leaking phone numbers, emails, or messages.

### Backend Email Notifications

Pros:
- Faster awareness of new leads/contact submissions.
- Can notify different addresses for Get Help vs Contact Us.
- Useful before a full admin panel is ready.

Cons:
- Requires choosing and configuring an outbound email provider or SMTP service.
- Cloudflare Email Routing alone is not enough for outbound mail.
- Needs retry/failure handling decisions.
- Email content must avoid unnecessary sensitive data exposure.

### Protected Admin Panel

Pros:
- Best long-term workflow for reviewing, filtering, assigning, and closing submissions.
- Can reduce direct database access.
- Can provide audit-friendly status and notes.

Cons:
- Requires authentication, authorization, and role design first.
- Increases security and privacy responsibility.
- Should not expose personal contact data through public or weakly protected endpoints.

## Risks And Boundaries

- Public forms can receive spam later.
- CAPTCHA, rate limiting, and other abuse protection are not implemented yet by current decision.
- The website should not imply a guaranteed SLA, callback time, or response promise unless operations can support it.
- Submission data includes sensitive user contact details and free-text messages.
- Lead and contact data must not be exposed publicly.
- Operational exports should be handled carefully and shared only with people who need access.

## Likely Admin Fields And Actions Later

Future admin workflows should likely support:

- List/filter Get Help leads.
- List/filter Contact Us submissions.
- Search by name, phone, email, city, interest type, inquiry type, status, and date.
- View full submission details.
- Update lead status.
- Mark contacted, resolved, closed, or spam.
- Add internal notes.
- Export CSV for trusted operators.
- Track created/updated audit fields.

Future status handling should stay simple at first. Avoid building assignment, SLA, notification, and CRM-style workflows until the real operating process is clearer.

## Practical Recommendation

Keep the next step manual unless submission volume proves otherwise. A small controlled export/report path is lower risk than jumping straight to outbound email or admin CRUD. Choose an outbound mail provider only when notification requirements are clear, and design admin access before exposing any retrieval endpoints.
