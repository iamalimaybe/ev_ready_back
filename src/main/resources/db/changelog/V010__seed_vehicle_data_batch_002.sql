--liquibase formatted sql

--changeset evready:V010-prepare-vehicle-seed-batch-002
DROP TABLE IF EXISTS vehicle_seed_batch_002;

CREATE TABLE vehicle_seed_batch_002 (
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

--changeset evready:V010-load-vehicle-seed-batch-002
INSERT INTO vehicle_seed_batch_002 (
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
    ('CAR', 'Honri', 'TYPE2_AC', 'Ve', '2.0', 3599000, 200, 18.500, FALSE, NULL, 'https://www.pakwheels.com/new-cars/honri/ve/2-0--8/', 'PakWheels Honri Ve 2.0 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 480),
    ('CAR', 'Honri', 'TYPE2_AC', 'Ve', '3.0', 4399000, 300, 29.900, FALSE, NULL, 'https://www.pakwheels.com/new-cars/honri/ve/3-0--5/', 'PakWheels Honri Ve 3.0 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 490),
    ('CAR', 'GUGO', 'CCS2_DC', 'Box', 'E1', 5700000, 330, 31.400, TRUE, NULL, 'https://www.pakwheels.com/new-cars/gugo/box/e1/', 'PakWheels GUGO Box E1 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 500),
    ('CAR', 'GUGO', 'CCS2_DC', 'Box', 'E3', 6650000, 430, 42.300, TRUE, NULL, 'https://www.pakwheels.com/new-cars/gugo/box/e3/', 'PakWheels GUGO Box E3 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 510),
    ('CAR', 'GUGO', 'TYPE2_AC', 'GIGI', 'Standard', 3750000, 220, 16.800, FALSE, NULL, 'https://www.pakwheels.com/new-cars/gugo/gigi/', 'PakWheels GUGO GIGI price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 520),
    ('CAR', 'GUGO', 'CCS2_DC', 'AION V', 'Premium', 11990000, 500, 64.000, TRUE, NULL, 'https://www.pakwheels.com/new-cars/gugo/aion-v/premium--19/', 'PakWheels GUGO AION V Premium spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 530),
    ('CAR', 'Dongfeng', 'CCS2_DC', 'Box', 'Smart 330', 5500000, 330, 32.000, TRUE, NULL, 'https://www.pakwheels.com/new-cars/dongfeng/box--2/', 'PakWheels Dongfeng Box price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 540),
    ('CAR', 'Dongfeng', 'CCS2_DC', 'Box', 'Flagship 430', 6800000, 430, 42.300, TRUE, NULL, 'https://www.pakwheels.com/new-cars/dongfeng/box--2/', 'PakWheels Dongfeng Box price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 550),
    ('CAR', 'Seres', 'CCS2_DC', '3', 'EV', 8390000, 403, 54.000, TRUE, NULL, 'https://www.pakwheels.com/new-cars/seres/3--2/', 'PakWheels Seres 3 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 560),
    ('CAR', 'Omoda', 'CCS2_DC', 'E5', 'Standard', 8990000, 430, 61.000, TRUE, NULL, 'https://www.pakwheels.com/new-cars/omoda/e5/', 'PakWheels Omoda E5 price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 570),
    ('CAR', 'Jaecoo', 'CCS2_DC', 'J6', 'Comfort RWD', 8799000, 329, 49.900, TRUE, NULL, 'https://www.pakwheels.com/new-cars/jaecoo/j6/comfort-rwd/', 'PakWheels Jaecoo J6 Comfort RWD price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 580),
    ('CAR', 'Jaecoo', 'CCS2_DC', 'J6', 'Luxury RWD', 9899000, 426, 65.600, TRUE, NULL, 'https://www.pakwheels.com/new-cars/jaecoo/j6/luxury-rwd/', 'PakWheels Jaecoo J6 Luxury RWD price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 590),
    ('CAR', 'Jaecoo', 'CCS2_DC', 'J6', 'Premium AWD', 10799000, 418, 69.700, TRUE, NULL, 'https://www.pakwheels.com/new-cars/jaecoo/j6/premium-awd/', 'PakWheels Jaecoo J6 Premium AWD price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 600),
    ('CAR', 'MG', 'CCS2_DC', 'ZS EV', 'MCE Essence', 9690000, 360, 51.100, TRUE, NULL, 'https://www.pakwheels.com/new-cars/mg/zs-ev/', 'PakWheels MG ZS EV MCE Essence price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 610),
    ('CAR', 'MG', 'CCS2_DC', 'ZS EV', 'MCE Long Range', 14999000, 505, 72.600, TRUE, NULL, 'https://www.pakwheels.com/new-cars/mg/zs-ev/mce-long-range/', 'PakWheels MG ZS EV MCE Long Range price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 620),
    ('CAR', 'MG', 'CCS2_DC', '5 EV', 'SE Long Range', 11490000, 400, 61.000, TRUE, NULL, 'https://www.pakwheels.com/new-cars/mg/5-ev/', 'PakWheels MG5 EV price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 630),
    ('CAR', 'Forthing', 'CCS2_DC', 'Friday', 'BEV Luxury', 8999000, 500, 64.400, TRUE, NULL, 'https://www.pakwheels.com/new-cars/forthing/friday/', 'PakWheels Forthing Friday BEV Luxury price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 640),
    ('CAR', 'Forthing', 'CCS2_DC', 'Friday', 'BEV Exclusive', 10499000, 500, 64.400, TRUE, NULL, 'https://www.pakwheels.com/new-cars/forthing/friday/bev-exclusive/', 'PakWheels Forthing Friday BEV Exclusive price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 650),
    ('CAR', 'Kaiyi', 'CCS2_DC', 'X3 Pro', 'EV Flagship', 6790000, 401, 53.600, TRUE, NULL, 'https://www.pakwheels.com/new-cars/kaiyi/x3-pro/', 'PakWheels Kaiyi X3 Pro EV price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 660),
    ('CAR', 'Audi', 'CCS2_DC', 'e-tron', '50 quattro 230 kW', 13600000, 300, 60.000, TRUE, NULL, 'https://www.pakwheels.com/new-cars/compare/audi-e-tron-vs-bmw-ix', 'PakWheels Audi e-tron comparison price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 670),
    ('BIKE', 'Pakzon Electric', 'PORTABLE_AC_CHARGER', 'PE-70D', 'Dry Gel', 149900, 80, 1.500, FALSE, NULL, 'https://www.pakwheels.com/bikes/pakzon-electric/pe-70d/', 'PakWheels Pakzon PE-70D price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 680),
    ('BIKE', 'Pakzon Electric', 'PORTABLE_AC_CHARGER', 'PE-100D', 'Dry Gel', 165900, 90, 1.500, FALSE, NULL, 'https://www.urdupoint.com/bikes/detail/pakzon-electric/215/pe-100d.html', 'UrduPoint Pakzon PE-100D price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 690),
    ('BIKE', 'Pakzon Electric', 'PORTABLE_AC_CHARGER', 'PE-70 LP', 'Lithium Iron Phosphate', 225900, 100, 1.800, FALSE, NULL, 'https://pakzonelectricmotors.com/product/pe-70li/', 'Pakzon official PE-70 LP spec page with market price reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 700),
    ('BIKE', 'Pakzon Electric', 'PORTABLE_AC_CHARGER', 'PE-100 LP', 'Dry Gel', 239900, 100, 1.200, FALSE, NULL, 'https://pakzonelectricmotors.com/product/pe-100-lp/', 'Pakzon official PE-100 LP spec page with market price reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 710),
    ('BIKE', 'Pakzon Electric', 'PORTABLE_AC_CHARGER', 'PE-100L-SE', 'New Edition', 278000, 130, 2.700, FALSE, NULL, 'https://pakzonelectricmotors.com/product/pe-100l-se-new-edition/', 'Pakzon official PE-100L-SE spec page with market price reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 720),
    ('BIKE', 'Jolta Electric', 'PORTABLE_AC_CHARGER', 'Neo Pro', 'Graphene', 184900, 90, 2.160, FALSE, NULL, 'https://priceoye.pk/electric-bikes/jolta-electric/jolta-electric-neo-pro', 'PriceOye Jolta Neo Pro price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 750),
    ('BIKE', 'Jolta Electric', 'PORTABLE_AC_CHARGER', 'Zeno', 'Graphene', 199900, 70, 1.872, FALSE, NULL, 'https://joltaelectric.com/zeno/', 'Jolta official Zeno page', '2026-05-26 00:00:00+05', 'OFFICIAL', 760),
    ('BIKE', 'Jolta Electric', 'PORTABLE_AC_CHARGER', 'Zeno Pro', 'Graphene', 221900, 90, 2.736, FALSE, NULL, 'https://joltaelectric.com/zeno-pro/', 'Jolta official Zeno Pro page', '2026-05-26 00:00:00+05', 'OFFICIAL', 770),
    ('BIKE', 'Jolta Electric', 'PORTABLE_AC_CHARGER', 'JE-70D', 'Dry Gel', 149900, 80, 1.200, FALSE, NULL, 'https://www.pakwheels.com/bikes/jolta-electric/je-70d/', 'PakWheels Jolta JE-70D price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 780),
    ('BIKE', 'Jolta Electric', 'PORTABLE_AC_CHARGER', 'JE-70G', 'Graphene', 170900, 70, 1.560, FALSE, NULL, 'https://www.pakwheels.com/bikes/jolta-electric/je-70g/', 'PakWheels Jolta JE-70G price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 790),
    ('BIKE', 'Jolta Electric', 'PORTABLE_AC_CHARGER', 'JE-100L', 'Li-ion', 239900, 100, 1.800, FALSE, NULL, 'https://www.pakwheels.com/bikes/jolta-electric/je-100l/', 'PakWheels Jolta JE-100L price/spec reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 800),
    ('BIKE', 'E Turbo', 'PORTABLE_AC_CHARGER', 'Evo', 'Lithium', 585000, 100, 2.880, FALSE, NULL, 'https://www.gari.pk/bikes/e-turbo/evo/', 'Gari.pk E Turbo EVO spec reference with PakWheels price reference', '2026-05-26 00:00:00+05', 'UNVERIFIED', 810);

--changeset evready:V010-check-vehicle-seed-brand-refs
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM vehicle_seed_batch_002 s LEFT JOIN brand b ON b.name = s.brand_name WHERE b.id IS NULL;
SELECT 1;

--changeset evready:V010-check-vehicle-seed-charger-type-refs
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM vehicle_seed_batch_002 s LEFT JOIN charger_type ct ON ct.code = s.charger_type_code WHERE ct.id IS NULL;
SELECT 1;

--changeset evready:V010-insert-vehicle-seed-batch-002
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
FROM vehicle_seed_batch_002 s
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

--changeset evready:V010-cleanup-vehicle-seed-batch-002
DROP TABLE IF EXISTS vehicle_seed_batch_002;
