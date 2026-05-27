--liquibase formatted sql

--changeset evready:V011-prepare-vehicle-seed-batch-003
DROP TABLE IF EXISTS vehicle_seed_batch_003;

CREATE TABLE vehicle_seed_batch_003 (
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

--changeset evready:V011-load-vehicle-seed-batch-003
INSERT INTO vehicle_seed_batch_003 (
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
    ('BIKE', 'REVOO', 'PORTABLE_AC_CHARGER', 'A04', 'Graphene', 100000, 40, 1.000, FALSE, NULL, 'https://www.pakwheels.com/bikes/revoo/a04/', 'PakWheels REVOO A04 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 820),
    ('BIKE', 'REVOO', 'PORTABLE_AC_CHARGER', 'A01', 'Graphene', 210000, 95, 1.800, FALSE, NULL, 'https://www.pakwheels.com/bikes/revoo/a01/', 'PakWheels REVOO A01 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 830),
    ('BIKE', 'REVOO', 'PORTABLE_AC_CHARGER', 'A11', 'Graphene', 167000, 80, 1.560, FALSE, NULL, 'https://www.pakwheels.com/bikes/revoo/a11/', 'PakWheels REVOO A11 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 840),
    ('BIKE', 'REVOO', 'PORTABLE_AC_CHARGER', 'A12', 'Graphene', 187000, 90, 1.870, FALSE, NULL, 'https://www.pakwheels.com/bikes/revoo/a12/', 'PakWheels REVOO A12 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 850),
    ('BIKE', 'REVOO', 'PORTABLE_AC_CHARGER', 'C32', 'Graphene', 267000, 110, 2.520, FALSE, NULL, 'https://www.pakwheels.com/bikes/revoo/c32/', 'PakWheels REVOO C32 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 860),
    ('BIKE', 'REVOO', 'PORTABLE_AC_CHARGER', 'C32 Young', 'Graphene', 227000, 110, 2.520, FALSE, NULL, 'https://www.pakwheels.com/bikes/revoo/c32-young/', 'PakWheels REVOO C32 Young price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 870),
    ('BIKE', 'REVOO', 'PORTABLE_AC_CHARGER', 'B12', 'Graphene', 350000, 120, 3.600, FALSE, NULL, 'https://www.pakwheels.com/bikes/revoo/b12/', 'PakWheels REVOO B12 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 880),
    ('BIKE', 'REVOO', 'PORTABLE_AC_CHARGER', 'E52', 'Graphene', 405000, 110, 3.360, FALSE, NULL, 'https://www.pakwheels.com/bikes/revoo/e52/', 'PakWheels REVOO E52 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 890),
    ('BIKE', 'REVOO', 'PORTABLE_AC_CHARGER', 'Y04', 'Graphene', 119000, 45, 1.000, FALSE, NULL, 'https://www.pakwheels.com/bikes/revoo/y04/', 'PakWheels REVOO Y04 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 900),
    ('BIKE', 'REVOO', 'PORTABLE_AC_CHARGER', 'Y06', 'Graphene', 151000, 60, 0.960, FALSE, NULL, 'https://www.pakwheels.com/bikes/new-bikes/', 'PakWheels new bikes price reference with secondary launch/spec reference for battery and range', '2026-05-26 00:00:00+05', 'UNVERIFIED', 910),
    ('BIKE', 'Saige', 'PORTABLE_AC_CHARGER', 'C200', 'Graphene', 200000, 75, 1.440, FALSE, NULL, 'https://www.pakwheels.com/bikes/saige/c200/', 'PakWheels Saige C200 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 920),
    ('BIKE', 'Saige', 'PORTABLE_AC_CHARGER', 'N300', 'Graphene', 268000, 75, 1.440, FALSE, NULL, 'https://www.pakwheels.com/bikes/saige/n300/', 'PakWheels Saige N300 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 930),
    ('BIKE', 'Crown EV', 'PORTABLE_AC_CHARGER', 'Fairy', 'Graphene', 165000, 80, 1.650, FALSE, NULL, 'https://www.pakwheels.com/bikes/crown/fairy/', 'PakWheels Crown Fairy price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 940),
    ('BIKE', 'Crown EV', 'PORTABLE_AC_CHARGER', 'Champion', 'Lithium', 275000, 150, 2.880, FALSE, NULL, 'https://www.pakwheels.com/bikes/crown/champion/', 'PakWheels Crown Champion price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 950),
    ('BIKE', 'Crown EV', 'PORTABLE_AC_CHARGER', 'Raftaar', 'LFP 3kW', 299000, 110, 2.944, FALSE, NULL, 'https://crownelectricmobility.com/crown-electric-raftaar', 'Crown Electric Mobility Raftaar official specs with public price reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 960),
    ('BIKE', 'Road Prince', 'PORTABLE_AC_CHARGER', 'E-Go', 'Lithium-ion', 260000, 70, 2.160, FALSE, NULL, 'https://www.pakwheels.com/bikes/road-prince/e-go/', 'PakWheels Road Prince E-Go price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 970),
    ('BIKE', 'Road Prince', 'PORTABLE_AC_CHARGER', 'Zeus XR', 'Lead Acid', 278000, 60, 2.000, FALSE, NULL, 'https://www.pakwheels.com/bikes/road-prince/zeus-xr/', 'PakWheels Road Prince Zeus XR price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 980),
    ('BIKE', 'MS Jaguar', 'PORTABLE_AC_CHARGER', 'Miso', 'Graphene', 158000, 70, 1.680, FALSE, NULL, 'https://www.msjaguarmotorcycles.com/products/ms-jaguar-miso', 'MS Jaguar official Miso specs with PakWheels price reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 990),
    ('BIKE', 'MS Jaguar', 'PORTABLE_AC_CHARGER', 'Bolt', 'Graphene', 174000, 80, 1.920, FALSE, NULL, 'https://www.msjaguarmotorcycles.com/products/ms-jaguar-bolt', 'MS Jaguar official Bolt specs with PakWheels price reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 1000),
    ('BIKE', 'MS Jaguar', 'PORTABLE_AC_CHARGER', 'Bolt', 'LFP', 226000, 100, 1.800, FALSE, NULL, 'https://www.msjaguarmotorcycles.com/products/ms-jaguar-bolt', 'MS Jaguar official Bolt LFP specs with PakWheels price reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 1010),
    ('BIKE', 'MS Jaguar', 'PORTABLE_AC_CHARGER', 'M1', 'Graphene', 204000, 90, 2.304, FALSE, NULL, 'https://www.msjaguarmotorcycles.com/products/ms-jaguar-m1', 'MS Jaguar official M1 specs with PakWheels price reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 1020),
    ('BIKE', 'MS Jaguar', 'PORTABLE_AC_CHARGER', 'M1', 'LFP', 246000, 100, 1.800, FALSE, NULL, 'https://www.msjaguarmotorcycles.com/products/ms-jaguar-m1', 'MS Jaguar official M1 LFP specs with PakWheels price reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 1030),
    ('BIKE', 'MS Jaguar', 'PORTABLE_AC_CHARGER', 'E-70 Supreme', 'LFP', 268000, 100, 1.500, FALSE, NULL, 'https://www.pakwheels.com/bikes/ms-jaguar-motorcycle/e-70-supreme/', 'PakWheels MS Jaguar E-70 Supreme price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 1040);

--changeset evready:V011-check-vehicle-seed-brand-refs
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM vehicle_seed_batch_003 s LEFT JOIN brand b ON b.name = s.brand_name WHERE b.id IS NULL;
SELECT 1;

--changeset evready:V011-check-vehicle-seed-charger-type-refs
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM vehicle_seed_batch_003 s LEFT JOIN charger_type ct ON ct.code = s.charger_type_code WHERE ct.id IS NULL;
SELECT 1;

--changeset evready:V011-insert-vehicle-seed-batch-003
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
FROM vehicle_seed_batch_003 s
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

--changeset evready:V011-cleanup-vehicle-seed-batch-003
DROP TABLE IF EXISTS vehicle_seed_batch_003;
