# Email Notification Plan

This plan documents the outbound email notification path for backend lead/contact submission alerts. It does not include credentials, schema changes, admin APIs, auth, or public abuse protection.

## Current State

- Cloudflare Email Routing is configured and tested for inbound receiving/forwarding:
  - `info@evready.pk`
  - `contact@evready.pk`
  - `leads@evready.pk`
- Cloudflare Email Routing handles inbound forwarding only for the current setup.
- The backend can send outbound notification emails when `EMAIL_NOTIFICATIONS_ENABLED=true` and SMTP environment variables are configured.
- Get Help submissions are stored in PostgreSQL through `POST /api/v1/leads`.
- Contact Us submissions are stored separately in PostgreSQL through `POST /api/v1/contact-submissions`.
- PostgreSQL remains the source of truth for lead/contact submissions.
- Email delivery is best-effort only. A failed notification must not fail a successfully saved submission.

## Provider Options

### SMTP2GO

Recommended first option for low-volume backend SMTP notifications.

Pros:
- Fits a simple SMTP-based backend implementation.
- Keeps the first implementation provider-agnostic at the application boundary.
- Suitable for transactional notification-style emails if configured correctly.

Cons:
- Requires account setup, SMTP credentials, and DNS/deliverability configuration.
- Delivery still depends on provider limits, domain reputation, and correct DNS records.

### Resend

Developer-friendly email API option.

Pros:
- Clean API-first developer experience.
- Good fit if the backend later prefers provider APIs over raw SMTP.

Cons:
- Would likely shape implementation around a provider API instead of plain SMTP.
- May require provider-specific client code or dependency decisions later.

### Brevo

Broader marketing/CRM-style option.

Pros:
- Useful if EVReady later wants contact lists, campaigns, or CRM-like email workflows.
- Can support more than simple submission notifications.

Cons:
- Heavier than needed for first backend alerts.
- Marketing/CRM features can add operational complexity before they are needed.

### Cloudflare Email Service Outbound

Possible later option, but Cloudflare Email Routing itself is not outbound SMTP.

Pros:
- Could fit future Cloudflare-centered infrastructure decisions.

Cons:
- Not the current free inbound Email Routing feature.
- Not free on Workers Free for outbound use, so it should not be treated as the immediate low-cost backend SMTP path.

## Recommendation

Resend SMTP is the current production provider target. The backend implementation remains SMTP-based and provider-light so the project can move to another SMTP provider later without changing lead/contact persistence behavior.

The implementation is notification-only, not a full email workflow system.

## Future Backend Implementation Shape

- Configure SMTP and notification settings through environment variables only.
- Do not commit SMTP credentials.
- Do not add real credentials or production secrets to examples or docs.
- Send Get Help notifications to `leads@evready.pk`.
- Send Contact Us notifications to `contact@evready.pk`.
- Keep PostgreSQL save as the source-of-truth operation.
- Do not make email delivery part of the transaction success path.
- If email sending fails, the DB save should still succeed.
- Log email failures without exposing user contact details, message bodies, credentials, or provider secrets.
- Include only necessary submission details in the notification email.
- Keep the feature configurable with an environment flag.
- Avoid retries, queues, and complex delivery workflows until volume or reliability needs justify them.

## Future Environment Variables

Use environment variables like these in production. Values shown here are names only, not credentials:

- `EMAIL_NOTIFICATIONS_ENABLED`
- `SMTP_HOST`
- `SMTP_PORT`
- `SMTP_USERNAME`
- `SMTP_PASSWORD`
- `SMTP_FROM`
- `LEAD_NOTIFICATION_TO`
- `CONTACT_NOTIFICATION_TO`

Production values should live outside the repo, for example in the real production env file or deployment secret store.

Current production SMTP target:

- `SMTP_HOST=smtp.resend.com`
- `SMTP_PORT=587`
- `SMTP_USERNAME=resend`
- `SMTP_PASSWORD` must come from the Resend API key in the environment only.
- `SMTP_FROM=no-reply@evready.pk`
- `LEAD_NOTIFICATION_TO=leads@evready.pk`
- `CONTACT_NOTIFICATION_TO=contact@evready.pk`
- `EMAIL_NOTIFICATIONS_ENABLED=true`

## Risks And Boundaries

- Email deliverability depends on the provider, DNS records, sender reputation, and message content.
- Public forms may attract spam later.
- CAPTCHA, rate limiting, and other public abuse protection are not implemented yet by current decision.
- Frontend wording should not imply a guaranteed callback, response time, or SLA unless operations can support it.
- Notification emails may contain personal contact data; keep content minimal and route only to trusted mailboxes.
- Email notification failure should not hide or roll back a successfully stored lead/contact submission.

## Practical First Step Later

The backend notification service should stay small and configurable behind `EMAIL_NOTIFICATIONS_ENABLED`. Keep admin UI, auth, delivery dashboards, and anti-abuse controls as separate tasks.
