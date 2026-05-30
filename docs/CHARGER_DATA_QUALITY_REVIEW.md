# Charger Data Quality Review

This review covers the current charger directory seed data visible in the Liquibase charger seed files. It is an audit note only; it does not change seed data, schema, or public API behavior.

## Current Seed Snapshot

- Charger type seed file contains 8 active charger type rows:
  - `TYPE2_AC`
  - `CCS2_DC`
  - `GB_T`
  - `CHADEMO`
  - `THREE_PIN`
  - `PORTABLE_AC_CHARGER`
  - `PROPRIETARY`
  - `OTHER`
- Charger seed file contains 20 charger rows.
- All current charger rows reference `CCS2_DC`.
- Charging type values visible in seed data:
  - 18 `DC`
  - 2 `AC_DC`
- Status values visible in seed data:
  - 19 `OPERATIONAL`
  - 1 `UNKNOWN`
- Verification status values visible in seed data:
  - 8 `OFFICIAL`
  - 12 `UNVERIFIED`
- Coordinates are present for 6 rows and missing for 14 rows.
- Power values are present for 5 rows and missing for 15 rows.
- Every current charger seed row has `image` set to `NULL`.
- Current seed rows set `source_checked_at` to `2026-05-26 00:00:00+05`.

These counts are based on the current seed SQL files, not a live production database query.

## Public Directory Fields

The charger directory currently depends on these seeded fields for public display and filtering:

- `charger_type_code` through the resolved `charger_type_id`
- `name`
- `city`
- `area`
- `address`
- `latitude`
- `longitude`
- `charging_type`
- `status`
- `power_kw`
- `price_note`
- `description`
- `image`
- `verification_status`
- `display_order`

Internal provenance fields are also seeded:

- `source_url`
- `source_label`
- `source_checked_at`

Per the API contract, provenance fields stay internal. Public charger DTOs expose `verificationStatus` for frontend source-confidence badges.

## Trust And Verification

Charger data has higher user-safety risk than vehicle catalog data because users may rely on it before travel. The current seed data should be treated as a directory starting point, not a live charging network map.

`verification_status` affects frontend source-confidence badges. `OFFICIAL` should mean source-backed from an official operator, network, distributor, or similar source. It does not mean EVReady physically visited, tested, or guaranteed the charger.

`status` is not live availability. `OPERATIONAL` means the source suggested the charger was operational when reviewed; it does not mean the charger is available right now, working today, unoccupied, compatible with the user's vehicle, or priced as shown.

## Common Gaps And Weak Fields

- All current charger rows are mapped to `CCS2_DC`; seeded charger type coverage is broader than actual charger row coverage.
- Most rows do not have latitude/longitude, which limits map precision and navigation usefulness.
- Most rows do not have `power_kw`, so users cannot reliably compare charging speed.
- All image fields are empty.
- Price notes are mostly cautionary text rather than confirmed current tariffs.
- Several rows rely on third-party maps, news, social posts, or rollout announcements rather than a direct live operator listing.
- Some source labels describe rollout or public map evidence, which may not prove current operation.
- Connector details are coarse: `charger_type_id` plus `charging_type` do not capture stall count, connector count, exact plug mix, AC/DC split, payment method, hours, access restrictions, or downtime.
- There is no field for last operator confirmation, last user report, last successful charge, opening hours, phone number, stall count, or live status provider.

## Risky Fields Users May Treat As Exact

- `address`: may be enough for recognition but not precise enough for navigation.
- `latitude` / `longitude`: only 6 rows have coordinates, and public-map coordinates should still be treated as approximate unless operator-confirmed.
- `status`: not live availability and not a guarantee the charger works.
- `power_kw`: often missing; when present, it may be advertised peak power, not delivered speed.
- `charger_type`: all seeded rows are `CCS2_DC`, but connector availability should be verified before travel.
- `charging_type`: `DC` or `AC_DC` may not describe all plugs at a site.
- `price_note`: not a tariff guarantee.
- `verification_status`: source confidence only; not EVReady physical verification.

## Verify Before Adding More Chargers

Before adding additional charger rows, verify at minimum:

- Charger type and connector compatibility from a source suitable for public display.
- Charging type, especially whether AC, DC, or both are actually available at the location.
- Current operational status from an operator, official network page, recent direct confirmation, or another clearly labeled source.
- City, area, address, and coordinates independently enough for a traveler to find the site.
- Power rating and whether it applies to one connector, multiple connectors, or the whole site.
- Pricing or payment notes, with cautious wording if not current.
- Source URL, source label, and source checked date.
- Verification status based on source quality, not on whether the listing seems useful.
- Duplicate or nearby charger names to avoid misleading users into thinking one site is multiple sites.

## Admin Correction APIs

Protected admin charger management APIs now allow trusted admins to list, view, create, and update charger directory records without direct database access. These APIs are for directory data management only; they do not delete chargers, import/export CSV, apply user feedback automatically, or imply live charger availability.

The admin correction APIs make these fields editable:

- Active/inactive state
- Display order
- Charger type
- Name
- City
- Area
- Address
- Latitude
- Longitude
- Charging type
- Status
- Power kW
- Price note
- Description
- Image
- Source URL
- Source label
- Source checked date/time
- Verification status

Admin workflows should later support charger-specific review metadata, such as last operator-confirmed at, last user-reported at, last successful charge report, stall count, connector count, opening hours, payment method, access notes, and review notes. Those fields are data-management needs, not current public API commitments.

## Practical Recommendation

Keep public wording conservative until there is live charger integration or regular operator/user confirmation. The directory can help users discover likely charging locations, but it should keep warning users to verify operation, connector availability, pricing, and access before travel.
