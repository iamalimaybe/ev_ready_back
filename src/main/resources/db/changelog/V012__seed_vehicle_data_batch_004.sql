--liquibase formatted sql

--changeset evready:V012-prepare-vehicle-seed-batch-004
DROP TABLE IF EXISTS vehicle_seed_batch_004;

CREATE TABLE vehicle_seed_batch_004 (
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

--changeset evready:V012-load-vehicle-seed-batch-004
INSERT INTO vehicle_seed_batch_004 (
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
    ('CAR', 'GAC Aion', 'CCS2_DC', 'Aion UT', 'LMC Launch', 6399000, 335, 44.000, TRUE, NULL, 'https://www.pakwheels.com/blog/gac-aion-ut-lmc-vs-gugo-aion-ut/', 'PakWheels GAC Aion UT LMC vs GUGO Aion UT comparison reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 1050),
    ('CAR', 'GUGO', 'CCS2_DC', 'AION UT', 'Premium', 6990000, 425, 49.000, TRUE, NULL, 'https://www.pakwheels.com/new-cars/gugo/aion-ut/premium--18/', 'PakWheels GUGO AION UT Premium price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 1060),
    ('CAR', 'GUGO', 'CCS2_DC', 'AION UT', 'Luxury', 8499000, 500, 60.000, TRUE, NULL, 'https://www.pakwheels.com/new-cars/gugo/aion-ut/luxury--5/', 'PakWheels GUGO AION UT Luxury price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 1070),
    ('CAR', 'Hyptec', 'CCS2_DC', 'HT', 'Elite Plus', 11999000, 520, 83.000, TRUE, NULL, 'https://www.pakwheels.com/new-cars/hyptec/ht/elite-plus/', 'PakWheels Hyptec HT Elite Plus price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 1090),
    ('CAR', 'Hyptec', 'CCS2_DC', 'HT', 'Ultra Gullwing Door Plus', 13999000, 520, 83.000, TRUE, NULL, 'https://www.pakwheels.com/new-cars/hyptec/ht/ultra-gullwing-door-plus/', 'PakWheels Hyptec HT Ultra Gullwing Door Plus price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 1100),
    ('CAR', 'ORA', 'CCS2_DC', '03', 'Standard', 8999000, 310, 48.000, TRUE, NULL, 'https://www.pakwheels.com/new-cars/ora/03/', 'PakWheels ORA 03 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 1110),
    ('CAR', 'Audi', 'CCS2_DC', 'e-tron', '50 quattro Sportback 230kW', 14900000, 300, 95.000, TRUE, NULL, 'https://www.pakwheels.com/new-cars/audi/e-tron/50-quattro-sportback-230kw/', 'PakWheels Audi e-tron 50 quattro Sportback price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 1120),
    ('CAR', 'Audi', 'CCS2_DC', 'e-tron', '55 quattro 300kW', 16575000, 417, 95.000, TRUE, NULL, 'https://www.pakwheels.com/new-cars/audi/e-tron/55-quattro-300kw/', 'PakWheels Audi e-tron 55 quattro price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 1130),
    ('CAR', 'Audi', 'CCS2_DC', 'Q8 e-tron', '50 quattro', 33020000, 484, 95.000, TRUE, NULL, 'https://www.pakwheels.com/new-cars/audi/q8-e-tron/50-quattro/', 'PakWheels Audi Q8 e-tron 50 quattro price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 1140),
    ('CAR', 'Audi', 'CCS2_DC', 'Q8 e-tron', '55 quattro', 47500000, 573, 114.000, TRUE, NULL, 'https://www.pakwheels.com/new-cars/audi/q8-e-tron/55-quattro/', 'PakWheels Audi Q8 e-tron 55 quattro price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 1150),
    ('CAR', 'Audi', 'CCS2_DC', 'e-tron GT', 'RS quattro', 57000000, 401, 93.400, TRUE, NULL, 'https://www.pakwheels.com/new-cars/audi/e-tron-gt/rs--12/', 'PakWheels Audi e-tron GT RS price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 1160);

--changeset evready:V012-check-vehicle-seed-brand-refs
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM vehicle_seed_batch_004 s LEFT JOIN brand b ON b.name = s.brand_name WHERE b.id IS NULL;
SELECT 1;

--changeset evready:V012-check-vehicle-seed-charger-type-refs
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM vehicle_seed_batch_004 s LEFT JOIN charger_type ct ON ct.code = s.charger_type_code WHERE ct.id IS NULL;
SELECT 1;

--changeset evready:V012-insert-vehicle-seed-batch-004
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
FROM vehicle_seed_batch_004 s
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

--changeset evready:V012-cleanup-vehicle-seed-batch-004
DROP TABLE IF EXISTS vehicle_seed_batch_004;
