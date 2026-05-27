--liquibase formatted sql

--changeset evready:V009-prepare-vehicle-seed-batch-001
DROP TABLE IF EXISTS vehicle_seed_batch_001;

CREATE TABLE vehicle_seed_batch_001 (
    vehicle_type VARCHAR(20) NOT NULL,
    brand_name VARCHAR(100) NOT NULL,
    charger_type_code VARCHAR(50) NOT NULL,
    model VARCHAR(150) NOT NULL,
    variant VARCHAR(150),
    price_pkr BIGINT,
    range_km INTEGER,
    battery_capacity_kwh NUMERIC(8,3),
    dc_fast_charging BOOLEAN NOT NULL,
    image VARCHAR(255),
    source_url VARCHAR(500),
    source_label VARCHAR(150),
    source_checked_at TIMESTAMP WITH TIME ZONE,
    verification_status VARCHAR(30) NOT NULL,
    display_order INTEGER NOT NULL
);

--changeset evready:V009-load-vehicle-seed-batch-001
INSERT INTO vehicle_seed_batch_001 (
    vehicle_type,
    brand_name,
    charger_type_code,
    model,
    variant,
    price_pkr,
    range_km,
    battery_capacity_kwh,
    dc_fast_charging,
    image,
    source_url,
    source_label,
    source_checked_at,
    verification_status,
    display_order
)
VALUES
    ('CAR', 'Deepal', 'CCS2_DC', 'L07', 'Sedan', 13999000, 540, 66.80, TRUE, NULL, 'https://deepal.com.pk/l7/', 'Deepal Pakistan official L07 page', '2026-05-26 00:00:00+05', 'OFFICIAL', 10),
    ('CAR', 'Deepal', 'CCS2_DC', 'S07', 'SUV', 14999000, 485, 66.80, TRUE, NULL, 'https://deepal.com.pk/s7/', 'Deepal Pakistan official S07 page', '2026-05-26 00:00:00+05', 'OFFICIAL', 20),
    ('CAR', 'Hyundai', 'CCS2_DC', 'IONIQ 5', 'AWD', 22500000, 500, 84.00, TRUE, NULL, 'https://hyundai-nishat.com/ioniq-5-specification/', 'Hyundai Nishat official IONIQ 5 specification page', '2026-05-26 00:00:00+05', 'OFFICIAL', 30),
    ('CAR', 'Hyundai', 'CCS2_DC', 'IONIQ 6', 'AWD', 23000000, 519, 77.40, TRUE, NULL, 'https://hyundai-nishat.com/ioniq-6-specification/', 'Hyundai Nishat official IONIQ 6 specification page', '2026-05-26 00:00:00+05', 'OFFICIAL', 40),
    ('CAR', 'MG', 'CCS2_DC', 'MG4 EV Urban', 'Urban', 6949000, 316, 43.00, TRUE, NULL, 'https://www.pakwheels.com/blog/mg4-ev-urban-launched-pakistan-price-specs/', 'PakWheels MG4 EV Urban launch price reference; specs from MG Pakistan official MG4 EV Urban page', '2026-05-26 00:00:00+05', 'UNVERIFIED', 50),
    ('CAR', 'MG', 'CCS2_DC', 'Binguo EV', 'Pro', 5699000, 333, 31.90, TRUE, NULL, 'https://www.pakwheels.com/blog/mg-reduces-binguo-ev-price-in-pakistan/', 'PakWheels MG Binguo updated price reference; specs from MG Pakistan official Binguo page', '2026-05-26 00:00:00+05', 'UNVERIFIED', 60),
    ('CAR', 'BYD', 'CCS2_DC', 'Atto 2', 'Premium', 7290000, 380, 45.12, TRUE, NULL, 'https://www.pakwheels.com/new-cars/pricelist/byd', 'PakWheels BYD price reference; specs from BYD Mega official Atto 2 page', '2026-05-26 00:00:00+05', 'UNVERIFIED', 70),
    ('CAR', 'BYD', 'CCS2_DC', 'Atto 3', 'Advance', 8990000, 410, 49.92, TRUE, NULL, 'https://www.pakwheels.com/new-cars/pricelist/byd', 'PakWheels BYD price reference; specs from BYD Mega official Atto 3 page', '2026-05-26 00:00:00+05', 'UNVERIFIED', 80),
    ('CAR', 'BYD', 'CCS2_DC', 'Seal', 'Premium', 16990000, 650, 82.56, TRUE, NULL, 'https://www.pakwheels.com/new-cars/pricelist/byd', 'PakWheels BYD price reference; specs from BYD Mega official Seal page', '2026-05-26 00:00:00+05', 'UNVERIFIED', 90),
    ('CAR', 'BYD', 'CCS2_DC', 'Sealion 7', 'Advanced', 15490000, 567, 82.56, TRUE, NULL, 'https://www.pakwheels.com/new-cars/pricelist/byd', 'PakWheels BYD price reference; specs from BYD Mega official Sealion 7 page', '2026-05-26 00:00:00+05', 'UNVERIFIED', 100),
    ('BIKE', 'Metro EV', 'PORTABLE_AC_CHARGER', 'Dabang', 'NCF', 190000, 105, 1.944, FALSE, NULL, 'https://metroev.pk/metro-dabang/', 'Metro EV official Dabang page', '2026-05-26 00:00:00+05', 'OFFICIAL', 110),
    ('BIKE', 'Metro EV', 'PORTABLE_AC_CHARGER', 'Dabang', 'LFP', 240000, 120, 2.304, FALSE, NULL, 'https://metroev.pk/metro-dabang/', 'Metro EV official Dabang page', '2026-05-26 00:00:00+05', 'OFFICIAL', 120),
    ('BIKE', 'Metro EV', 'PORTABLE_AC_CHARGER', 'Metrix', 'NCF', 250000, 115, 2.736, FALSE, NULL, 'https://metroev.pk/metro-metrix/', 'Metro EV official Metrix page', '2026-05-26 00:00:00+05', 'OFFICIAL', 130),
    ('BIKE', 'Metro EV', 'PORTABLE_AC_CHARGER', 'Metrix', 'LFP', 310000, 150, 3.456, FALSE, NULL, 'https://metroev.pk/metro-metrix/', 'Metro EV official Metrix page', '2026-05-26 00:00:00+05', 'OFFICIAL', 140),
    ('BIKE', 'Metro EV', 'PORTABLE_AC_CHARGER', 'T9 Sport', 'Lithium', 234000, 140, 2.304, FALSE, NULL, 'https://metroev.pk/metro-t9-sport-lithium/', 'Metro EV official T9 Sport Lithium page', '2026-05-26 00:00:00+05', 'OFFICIAL', 150),
    ('BIKE', 'Metro EV', 'PORTABLE_AC_CHARGER', 'Thrill Pro', 'Lithium', 254000, 120, 2.304, FALSE, NULL, 'https://metroev.pk/metro-thrill-pro-lithium/', 'Metro EV official Thrill Pro Lithium page', '2026-05-26 00:00:00+05', 'OFFICIAL', 160),
    ('BIKE', 'Vlektra', 'PORTABLE_AC_CHARGER', 'Velocity 180', 'Standard', 339000, 180, 2.592, FALSE, NULL, 'https://vlektra.com/product-velocity-180/', 'Vlektra official Velocity 180 page', '2026-05-26 00:00:00+05', 'OFFICIAL', 170),
    ('BIKE', 'Vlektra', 'PORTABLE_AC_CHARGER', 'Velocity 180 SE', 'Special Edition', 384000, 180, 2.592, FALSE, NULL, 'https://vlektra.com/product-v180se/', 'Vlektra official Velocity 180 SE page', '2026-05-26 00:00:00+05', 'OFFICIAL', 180),
    ('BIKE', 'Vlektra', 'PORTABLE_AC_CHARGER', 'Bolt', 'Standard', 549000, 150, 2.736, FALSE, NULL, 'https://vlektra.com/product-bolt/', 'Vlektra official Bolt page', '2026-05-26 00:00:00+05', 'OFFICIAL', 190),
    ('BIKE', 'Vlektra', 'PORTABLE_AC_CHARGER', '1969', 'Standard', 619000, 150, 2.592, FALSE, NULL, 'https://vlektra.com/product-1969/', 'Vlektra official 1969 page', '2026-05-26 00:00:00+05', 'OFFICIAL', 200),
    ('BIKE', 'Saige', 'PORTABLE_AC_CHARGER', 'N21', 'Standard', 278000, 110, 1.440, FALSE, NULL, 'https://saigepk.com/product-n21/', 'Saige official N21 page', '2026-05-26 00:00:00+05', 'OFFICIAL', 210),
    ('BIKE', 'Saige', 'PORTABLE_AC_CHARGER', 'E90', 'Standard', 248000, 85, 1.440, FALSE, NULL, 'https://saigepk.com/product-e90/', 'Saige official E90 page', '2026-05-26 00:00:00+05', 'OFFICIAL', 220),
    ('BIKE', 'Yadea', 'PORTABLE_AC_CHARGER', 'M3', 'Standard', 174000, 80, 1.380, FALSE, NULL, 'https://yadea.com.pk/product/yadea-m3-electric-scooter-e-bike/', 'Yadea Pakistan official M3 page', '2026-05-26 00:00:00+05', 'OFFICIAL', 230),
    ('BIKE', 'Yadea', 'PORTABLE_AC_CHARGER', 'GT30', 'Standard', 194000, 75, 1.380, FALSE, NULL, 'https://yadea.com.pk/product/yadea-gt30-electric-scooter-e-bike/', 'Yadea Pakistan official GT30 page', '2026-05-26 00:00:00+05', 'OFFICIAL', 240),
    ('BIKE', 'Yadea', 'PORTABLE_AC_CHARGER', 'Ruibin', 'Standard', 209000, 90, 1.872, FALSE, NULL, 'https://yadea.com.pk/product/yadea-ruibin-electric-scooter-e-bike/', 'Yadea Pakistan official Ruibin page', '2026-05-26 00:00:00+05', 'OFFICIAL', 250),
    ('BIKE', 'Yadea', 'PORTABLE_AC_CHARGER', 'T5', 'Standard', 253500, 85, 1.800, FALSE, NULL, 'https://yadea.com.pk/product/yadea-t5-electric-scooter-e-bike/', 'Yadea Pakistan official T5 page', '2026-05-26 00:00:00+05', 'OFFICIAL', 260),
    ('BIKE', 'Yadea', 'PORTABLE_AC_CHARGER', 'G5', 'Standard', 280000, 80, 1.920, FALSE, NULL, 'https://yadea.com.pk/product/yadea-g5-electric-scooter-e-bike/', 'Yadea Pakistan official G5 page', '2026-05-26 00:00:00+05', 'OFFICIAL', 270),
    ('BIKE', 'Yadea', 'PORTABLE_AC_CHARGER', 'T5L', 'Standard', 290000, 140, 2.160, FALSE, NULL, 'https://yadea.com.pk/product/yadea-t5l-electric-scooter-e-bike/', 'Yadea Pakistan official T5L page', '2026-05-26 00:00:00+05', 'OFFICIAL', 280),
    ('BIKE', 'Yadea', 'PORTABLE_AC_CHARGER', 'EPOC-H', 'Standard', 355000, 125, 2.736, FALSE, NULL, 'https://yadea.com.pk/product/yadea-epoc-h-scooter-e-bike/', 'Yadea Pakistan official EPOC-H page', '2026-05-26 00:00:00+05', 'OFFICIAL', 290),
    ('BIKE', 'Evee', 'PORTABLE_AC_CHARGER', 'S1 Pro', 'Graphene', 272000, 100, 2.736, FALSE, NULL, 'https://evee.pk/product/evee-s1-pro/', 'Evee official S1 Pro page', '2026-05-26 00:00:00+05', 'OFFICIAL', 300),
    ('BIKE', 'Evee', 'PORTABLE_AC_CHARGER', 'S1 Air', 'Graphene', 275000, 110, 2.736, FALSE, NULL, 'https://evee.pk/product/evee-s1-air/', 'Evee official S1 Air page', '2026-05-26 00:00:00+05', 'OFFICIAL', 310),
    ('BIKE', 'Evee', 'PORTABLE_AC_CHARGER', 'S1 Air', 'Lithium', 315000, 110, 2.458, FALSE, NULL, 'https://evee.pk/product/evee-s1-air/', 'Evee official S1 Air page', '2026-05-26 00:00:00+05', 'OFFICIAL', 320),
    ('BIKE', 'Evee', 'PORTABLE_AC_CHARGER', 'S1', 'Graphene', 210000, 90, 2.592, FALSE, NULL, 'https://evee.pk/product/evee-s1/', 'Evee official S1 page', '2026-05-26 00:00:00+05', 'OFFICIAL', 330),
    ('BIKE', 'Evee', 'PORTABLE_AC_CHARGER', 'S1', 'Lithium', 249000, 90, 2.458, FALSE, NULL, 'https://evee.pk/product/evee-s1/', 'Evee official S1 page', '2026-05-26 00:00:00+05', 'OFFICIAL', 340),
    ('BIKE', 'Evee', 'PORTABLE_AC_CHARGER', 'Gen-Z', 'Graphene', 175000, 90, 2.304, FALSE, NULL, 'https://evee.pk/product/evee-gen-z/', 'Evee official Gen-Z page', '2026-05-26 00:00:00+05', 'OFFICIAL', 350),
    ('BIKE', 'Evee', 'PORTABLE_AC_CHARGER', 'Gen-Z', 'Lithium', 215000, 90, 2.074, FALSE, NULL, 'https://evee.pk/product/evee-gen-z/', 'Evee official Gen-Z page', '2026-05-26 00:00:00+05', 'OFFICIAL', 360),
    ('BIKE', 'Evee', 'PORTABLE_AC_CHARGER', 'Gen-Z Pro', 'Lithium', 239000, 80, 2.304, FALSE, NULL, 'https://evee.pk/product/evee-gen-z-pro/', 'Evee official Gen-Z Pro page', '2026-05-26 00:00:00+05', 'OFFICIAL', 370),
    ('BIKE', 'Evee', 'PORTABLE_AC_CHARGER', 'Mito+', 'Graphene', 135000, 90, 1.920, FALSE, NULL, 'https://evee.pk/product/evee-mito/', 'Evee official Mito+ page', '2026-05-26 00:00:00+05', 'OFFICIAL', 380),
    ('BIKE', 'Evee', 'PORTABLE_AC_CHARGER', 'Nisa', 'Graphene', 155000, 90, 1.920, FALSE, NULL, 'https://evee.pk/product/evee-nisa/', 'Evee official Nisa page', '2026-05-26 00:00:00+05', 'OFFICIAL', 390),
    ('BIKE', 'Evee', 'PORTABLE_AC_CHARGER', 'Flipper', 'Graphene', 95000, 35, 0.624, FALSE, NULL, 'https://evee.pk/product/evee-flipper/', 'Evee official Flipper page', '2026-05-26 00:00:00+05', 'OFFICIAL', 400),
    ('BIKE', 'Evee', 'PORTABLE_AC_CHARGER', 'SQUBE', 'Graphene', 235000, 90, 2.736, FALSE, NULL, 'https://evee.pk/product/evee-sqube/', 'Evee official SQUBE page', '2026-05-26 00:00:00+05', 'OFFICIAL', 410),
    ('BIKE', 'Evee', 'PORTABLE_AC_CHARGER', 'Nisa 3W', 'Graphene', 235000, 70, 1.560, FALSE, NULL, 'https://evee.pk/product/evee-nisa-3w/', 'Evee official Nisa 3W page', '2026-05-26 00:00:00+05', 'OFFICIAL', 420),
    ('BIKE', 'Jolta Electric', 'PORTABLE_AC_CHARGER', 'Sparrow', 'Graphene', 145900, 35, 1.248, FALSE, NULL, 'https://joltaelectric.com/sparrow/', 'Jolta Electric official Sparrow page', '2026-05-26 00:00:00+05', 'OFFICIAL', 430),
    ('BIKE', 'Jolta Electric', 'PORTABLE_AC_CHARGER', 'Neo', 'Graphene', 174900, 70, 1.560, FALSE, NULL, 'https://joltaelectric.com/neo/', 'Jolta Electric official Neo page', '2026-05-26 00:00:00+05', 'OFFICIAL', 440),
    ('BIKE', 'Jolta Electric', 'PORTABLE_AC_CHARGER', 'Storm Pro', 'Graphene', 177900, 90, 2.160, FALSE, NULL, 'https://joltaelectric.com/storm-pro/', 'Jolta Electric official Storm Pro page', '2026-05-26 00:00:00+05', 'OFFICIAL', 450),
    ('BIKE', 'Jolta Electric', 'PORTABLE_AC_CHARGER', 'Aero Pro', 'LFP', 309900, 90, 3.000, FALSE, NULL, 'https://joltaelectric.com/aero-pro/', 'Jolta Electric official Aero Pro page', '2026-05-26 00:00:00+05', 'OFFICIAL', 460),
    ('BIKE', 'Jolta Electric', 'PORTABLE_AC_CHARGER', 'Mecha', 'Standard', 271900, 90, 2.736, FALSE, NULL, 'https://joltaelectric.com/?page_id=17265', 'Jolta Electric official Mecha page', '2026-05-26 00:00:00+05', 'OFFICIAL', 470);

--changeset evready:V009-check-vehicle-seed-brand-refs
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM vehicle_seed_batch_001 s LEFT JOIN brand b ON b.name = s.brand_name WHERE b.id IS NULL;
SELECT 1;

--changeset evready:V009-check-vehicle-seed-charger-type-refs
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM vehicle_seed_batch_001 s LEFT JOIN charger_type ct ON ct.code = s.charger_type_code WHERE ct.id IS NULL;
SELECT 1;

--changeset evready:V009-insert-vehicle-seed-batch-001
INSERT INTO vehicle (
    vehicle_type,
    brand_id,
    charger_type_id,
    model,
    variant,
    price_pkr,
    range_km,
    battery_capacity_kwh,
    dc_fast_charging,
    image,
    description,
    source_url,
    source_label,
    source_checked_at,
    verification_status,
    display_order,
    created_by
)
SELECT
    s.vehicle_type,
    b.id,
    ct.id,
    s.model,
    s.variant,
    s.price_pkr,
    s.range_km,
    s.battery_capacity_kwh,
    s.dc_fast_charging,
    s.image,
    CONCAT('Source-backed listing for ', s.brand_name, ' ', s.model, ' ', s.variant, '. Claimed price, range, and battery values should be verified before purchase.'),
    s.source_url,
    s.source_label,
    s.source_checked_at,
    s.verification_status,
    s.display_order,
    'system'
FROM vehicle_seed_batch_001 s
JOIN brand b ON b.name = s.brand_name
JOIN charger_type ct ON ct.code = s.charger_type_code
WHERE NOT EXISTS (
    SELECT 1
    FROM vehicle v
    WHERE v.vehicle_type = s.vehicle_type
      AND v.brand_id = b.id
      AND v.model = s.model
      AND v.variant IS NOT DISTINCT FROM s.variant
);

--changeset evready:V009-cleanup-vehicle-seed-batch-001
DROP TABLE IF EXISTS vehicle_seed_batch_001;
