# Vehicle Data Quality Review

This review covers the current vehicle catalog seed data visible in the Liquibase vehicle seed batches. It is an audit note only; it does not change seed data, schema, or public API behavior.

## Current Seed Snapshot

- Brand seed file contains 29 active brand rows.
- Vehicle seed files contain 113 vehicle rows:
  - 41 cars
  - 72 bikes
- Verification status values visible in seed data:
  - 43 `OFFICIAL`
  - 70 `UNVERIFIED`
- Every current vehicle seed row has `image` set to `NULL`.
- Current seed batches set `source_checked_at` to `2026-05-26 00:00:00+05`.

These counts are based on the current seed SQL files, not a live production database query.

## Public Catalog Fields

The vehicle catalog currently depends on these seeded fields for public display and filtering:

- `vehicle_type`
- `brand_name` through the resolved `brand_id`
- `charger_type_code` through the resolved `charger_type_id`
- `model`
- `variant`
- `price_pkr`
- `range_km`
- `battery_capacity_kwh`
- `dc_fast_charging`
- `image`
- generated `description`
- `verification_status`
- `display_order`

Internal provenance fields are also seeded:

- `source_url`
- `source_label`
- `source_checked_at`

Per the API contract, provenance fields stay internal. Public vehicle DTOs expose `verificationStatus` for source-confidence badges.

## Trust And Verification

`verification_status` is the main field affecting frontend trust badges. `OFFICIAL` should mean source-backed from an official OEM, operator, distributor, or similar source. It should not be presented as EVReady personally audited or field-verified.

The seed set mixes official pages, third-party catalog pages, price references, comparison pages, and source labels that mention combined references. This is useful for launch coverage but uneven for trust. Rows marked `UNVERIFIED` should be treated as catalog leads, not final trusted facts.

Risky fields users may treat as exact:

- `price_pkr`: prices can change quickly and may vary by city, dealer, booking status, tax treatment, import route, or promotion.
- `range_km`: range may be based on claimed, local, NEDC/CLTC/WLTP-style, or marketplace values; the test cycle is not stored.
- `battery_capacity_kwh`: pack capacity may be gross/net, rounded, or inferred from source pages.
- `dc_fast_charging`: boolean support is too coarse to communicate connector limits, peak kW, charge curve, or market-specific availability.
- `charger_type_code`: useful for filtering, but should be checked against Pakistan-market vehicle specs before presenting as definitive.
- `verification_status`: `OFFICIAL` means source-backed, not EVReady verified; `UNVERIFIED` should remain visually cautious.

## Common Gaps And Weak Fields

- No seeded vehicle has an image yet.
- Descriptions are generated boilerplate, not model-specific copy.
- Most rows are `UNVERIFIED`, especially many third-party marketplace references.
- Price, range, and battery values lack explicit notes for measurement basis, effective date beyond `source_checked_at`, or whether the value is estimated.
- `source_label` sometimes describes multiple sources, but only one `source_url` is stored.
- There is no separate field for model year, launch status, warranty, availability, booking status, trim-specific charger speed, or source confidence notes.
- Bikes and cars share the same core fields, but the meaning of range, charging, and battery values may differ substantially by segment.

## Verify Before Adding More Vehicles

Before adding additional rows, verify at minimum:

- Brand exists and is active.
- Vehicle type is correct.
- Model and variant names match the source exactly enough for users to recognize them.
- Price is current enough for public display, or clearly treated as indicative.
- Range value has an understood source basis and is not mixed with real-world range unless labeled accordingly.
- Battery capacity is not confused between voltage/amp-hour marketing specs and kWh.
- Charger type and DC fast-charging support are market-appropriate.
- `verification_status` matches the quality of the source, not the desirability of the listing.
- `source_url`, `source_label`, and `source_checked_at` are present and explain where the data came from.
- Duplicate model/variant rows are intentional and differentiated.

## Future Admin Fields

An admin panel should eventually make these fields editable:

- Active/inactive state
- Display order
- Vehicle type
- Brand
- Charger type
- Model
- Variant
- Price PKR
- Range km
- Battery capacity kWh
- DC fast-charging support
- Image
- Description
- Source URL
- Source label
- Source checked date/time
- Verification status

Admin workflows should also support review-oriented metadata later, such as a source note, last reviewed by, last reviewed at, and a reason for changing verification status. Those fields are data-management needs, not required public catalog fields today.

## Practical Recommendation

Before admin CRUD or new catalog features, keep the frontend wording conservative. Treat price, range, battery, charging support, and source confidence as helpful catalog signals, not guarantees. New seed additions should prefer fewer, better-sourced rows over broad coverage with weak references.
