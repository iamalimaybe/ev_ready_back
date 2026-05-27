--liquibase formatted sql

--changeset evready:V013-seed-charger-data-batch-001
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM charger_type WHERE code IN ('CCS2_DC');

WITH seed_charger (
    charger_type_code,
    name,
    city,
    area,
    address,
    latitude,
    longitude,
    charging_type,
    status,
    power_kw,
    price_note,
    description,
    image,
    source_url,
    source_label,
    source_checked_at,
    verification_status,
    display_order
) AS (
    VALUES
        ('CCS2_DC', 'PSO Electro Capri Gas Station', 'Islamabad', 'F-7 Markaz', 'Capri Gas Station, F-7 Markaz, Islamabad', NULL::NUMERIC(10,7), NULL::NUMERIC(10,7), 'AC_DC', 'OPERATIONAL', NULL::NUMERIC(8,2), 'Verify current pricing and connector availability before travel.', 'PSO Electro location at Capri Gas Station, F-7 Markaz. Connector details should be verified before travel.', NULL::VARCHAR(255), 'https://psopk.com/en/fuels/electric-vehicle-charger', 'PSO official Electro page', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'OFFICIAL', 10),
        ('CCS2_DC', 'PSO Electro Sunshine Petroleum', 'Lahore', 'Lahore Cantt / Girja Chowk', 'Sunshine Petroleum, Girja Chowk, Abid Majid Road, Lahore Cantt', NULL::NUMERIC(10,7), NULL::NUMERIC(10,7), 'DC', 'OPERATIONAL', NULL::NUMERIC(8,2), 'Verify current pricing and connector availability before travel.', 'PSO Electro location at Sunshine Petroleum, Lahore. DC fast charging is indicated by PSO public communications; verify before travel.', NULL::VARCHAR(255), 'https://psopk.com/en/fuels/electric-vehicle-charger', 'PSO official Electro page', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'OFFICIAL', 20),
        ('CCS2_DC', 'Shell Recharge Askari-4', 'Karachi', 'Askari IV / Rashid Minhas Road', 'Shell Askari-4 forecourt, Rashid Minhas Road, Karachi', NULL::NUMERIC(10,7), NULL::NUMERIC(10,7), 'DC', 'OPERATIONAL', 50.00, 'Verify current pricing before charging.', 'Shell Recharge rapid charger location listed by Shell Pakistan. Verify current connector availability before travel.', NULL::VARCHAR(255), 'https://www.shell.com.pk/motorists/shell-recharge.html', 'Shell Pakistan official Recharge page', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'OFFICIAL', 30),
        ('CCS2_DC', 'Shell Recharge SPL Oasis', 'Karachi', 'Gadap Town', 'SPL Oasis, Gadap Town, Karachi', NULL::NUMERIC(10,7), NULL::NUMERIC(10,7), 'DC', 'OPERATIONAL', 50.00, 'Verify current pricing before charging.', 'Shell Recharge rapid charger location listed by Shell Pakistan. Verify current connector availability before travel.', NULL::VARCHAR(255), 'https://www.shell.com.pk/motorists/shell-recharge.html', 'Shell Pakistan official Recharge page', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'OFFICIAL', 40),
        ('CCS2_DC', 'Shell Defence Filling Station', 'Karachi', 'DHA / Khayaban-e-Bahria', 'Shell Defence Filling Station, Khayaban-e-Bahria, Karachi', NULL::NUMERIC(10,7), NULL::NUMERIC(10,7), 'DC', 'UNKNOWN', 50.00, 'Verify current operation, pricing, and availability before travel.', 'Shell/KE selected site for rapid charging rollout. Current operation should be verified before travel.', NULL::VARCHAR(255), 'https://ke.com.pk/shell-and-k-electric-to-explore-possibility-of-electric-vehicle-charging-stations/', 'K-Electric Shell EV charging station rollout note', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'UNVERIFIED', 50),
        ('CCS2_DC', 'PSO Electro Bhera North Bound', 'Bhera', 'M-2 Motorway North Bound', 'Bhera Service Area North Bound, M-2 Motorway', NULL::NUMERIC(10,7), NULL::NUMERIC(10,7), 'DC', 'OPERATIONAL', NULL::NUMERIC(8,2), 'Verify current pricing, power, and availability before travel.', 'PSO Electro motorway charging station. Power and connector availability should be verified before travel.', NULL::VARCHAR(255), 'https://www.facebook.com/PSOCL/posts/pso-has-inaugurated-2-more-ultra-fast-electric-vehicle-ev-charging-stations-elec/5661256480598896/', 'PSO official social announcement for Bhera EV chargers', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'OFFICIAL', 60),
        ('CCS2_DC', 'PSO Electro Bhera South Bound', 'Bhera', 'M-2 Motorway South Bound', 'Bhera Service Area South Bound, M-2 Motorway', NULL::NUMERIC(10,7), NULL::NUMERIC(10,7), 'DC', 'OPERATIONAL', NULL::NUMERIC(8,2), 'Verify current pricing, power, and availability before travel.', 'PSO Electro motorway charging station. Power and connector availability should be verified before travel.', NULL::VARCHAR(255), 'https://www.facebook.com/PSOCL/posts/pso-has-inaugurated-2-more-ultra-fast-electric-vehicle-ev-charging-stations-elec/5661256480598896/', 'PSO official social announcement for Bhera EV chargers', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'OFFICIAL', 70),
        ('CCS2_DC', 'PSO Hubco Green I-8 Markaz', 'Islamabad', 'I-8 Markaz', 'PSO I-8 Markaz, Islamabad', NULL::NUMERIC(10,7), NULL::NUMERIC(10,7), 'DC', 'OPERATIONAL', NULL::NUMERIC(8,2), 'Verify current pricing, power, and availability before travel.', 'PSO and Hubco Green EV charging station at I-8 Markaz. Verify current operation before travel.', NULL::VARCHAR(255), 'https://www.facebook.com/PSOCL/posts/driving-excellence-and-expanding-pakistans-ev-network-for-a-cleaner-futurepso-an/1293297802838860/', 'PSO official social announcement for I-8 Markaz charger', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'OFFICIAL', 80),
        ('CCS2_DC', 'PSO Q-Star Hubco Green Charging Hub', 'Karachi', 'DHA Phase 1', 'PSO Q-Star, DHA Phase 1, Karachi', NULL::NUMERIC(10,7), NULL::NUMERIC(10,7), 'DC', 'OPERATIONAL', NULL::NUMERIC(8,2), 'Verify current pricing, power, and availability before travel.', 'Hubco Green, BYD, and Mega Motors charging hub at PSO Q-Star. Verify current operation before travel.', NULL::VARCHAR(255), 'https://www.facebook.com/bydpakistan/posts/charging-forward-in-karachis-most-connected-neighborhoodhubco-green-byd-and-mega/122155556894390101/', 'BYD Pakistan official social post for PSO Q-Star charger', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'OFFICIAL', 90),
        ('CCS2_DC', 'Hubco Green Ocean Mall', 'Karachi', 'Clifton', 'Ocean Mall, Karachi', NULL::NUMERIC(10,7), NULL::NUMERIC(10,7), 'DC', 'OPERATIONAL', NULL::NUMERIC(8,2), 'Verify current pricing, power, and availability before travel.', 'Hubco Green charging station reported at Ocean Mall, Karachi. Verify current operation before travel.', NULL::VARCHAR(255), 'https://www.brecorder.com/news/40390045/from-karachi-to-peshawar-hubco-green-accelerates-pakistans-ev-charging-rollout', 'Business Recorder Hubco Green charging rollout report', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'UNVERIFIED', 100),
        ('CCS2_DC', 'Hubco Green Total Parco MM Alam Road', 'Lahore', 'Gulberg III', 'MM Alam Road, Gulberg III, Lahore', NULL::NUMERIC(10,7), NULL::NUMERIC(10,7), 'DC', 'OPERATIONAL', NULL::NUMERIC(8,2), 'Verify current pricing, power, and availability before travel.', 'Hubco Green / Total Parco charging location reported at MM Alam Road. Verify current operation before travel.', NULL::VARCHAR(255), 'https://www.instagram.com/p/DFC9h-ANcGf/', 'Hubco Green public charging station announcement', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'UNVERIFIED', 110),
        ('CCS2_DC', 'Inverex Charging Station Shahra-e-Faisal', 'Karachi', 'Shahra-e-Faisal', 'Main Shahra-e-Faisal, Karachi Administration Employees Housing Society, Bangalore Town, Karachi', NULL::NUMERIC(10,7), NULL::NUMERIC(10,7), 'AC_DC', 'OPERATIONAL', 60.00, 'Reported pricing Rs 115/kWh; verify before charging.', 'Inverex charging station reported with DC 60kW and multiple connectors. Verify current operation before travel.', NULL::VARCHAR(255), 'https://chargingstationradar.com/charging-stations/karachi/inverex-main-shahra-e-faisal-karachi-administration-e', 'ChargingStationRadar Inverex Shahra-e-Faisal listing', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'UNVERIFIED', 120),
        ('CCS2_DC', 'Go Green Avenue Packages Mall', 'Lahore', 'Packages Mall', 'Packages Mall, Main Walton Road, Lahore', NULL::NUMERIC(10,7), NULL::NUMERIC(10,7), 'DC', 'OPERATIONAL', 50.00, 'Verify current pricing and availability before travel.', 'Go Green Avenue charging station reported at Packages Mall. Verify current operation before travel.', NULL::VARCHAR(255), 'https://acharge.com.pk/blogs/ev-charging/electric-vehicle-charging-stations/', 'A-Charge Punjab EV charging station guide', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'UNVERIFIED', 130),
        ('CCS2_DC', 'HUBCO PSO Al Falah', 'Hyderabad', 'Hyderabad Bypass', 'Hyderabad Bypass Road, Hyderabad', 25.4162105, 68.3389406, 'DC', 'OPERATIONAL', NULL::NUMERIC(8,2), 'Verify current pricing, power, and availability before travel.', 'HUBCO / PSO fast charging location reported on public charging maps. Verify current operation before travel.', NULL::VARCHAR(255), 'https://openchargemap.org/poi?CountryIDs=170&CountryName=pakistan', 'OpenChargeMap Pakistan charging locations', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'UNVERIFIED', 140),
        ('CCS2_DC', 'HUBCO PSO New Ali Trucking Station', 'Moro', 'N5 Highway', 'New Ali Trucking Station, N5 Highway, Moro', 26.6904713, 68.0258404, 'DC', 'OPERATIONAL', NULL::NUMERIC(8,2), 'Verify current pricing, power, and availability before travel.', 'HUBCO / PSO fast charging location reported on public charging maps. Verify current operation before travel.', NULL::VARCHAR(255), 'https://openchargemap.org/poi?CountryIDs=170&CountryName=pakistan', 'OpenChargeMap Pakistan charging locations', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'UNVERIFIED', 150),
        ('CCS2_DC', 'HUBCO PSO Zahir Pir South Bound', 'Zahir Pir', 'M-5 Motorway South Bound', 'Zahir Pir Service Area South Bound, M-5 Motorway', 28.9200840, 70.6238510, 'DC', 'OPERATIONAL', NULL::NUMERIC(8,2), 'Verify current pricing, power, and availability before travel.', 'HUBCO / PSO motorway fast charging location reported on public charging maps. Verify current operation before travel.', NULL::VARCHAR(255), 'https://openchargemap.org/poi?CountryIDs=170&CountryName=pakistan', 'OpenChargeMap Pakistan charging locations', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'UNVERIFIED', 160),
        ('CCS2_DC', 'Abdul Hakim EV Charger', 'Abdul Hakim', 'M-4 Motorway', 'M-4 Motorway, outside Abdul Hakim toll plaza', 30.5614509, 72.0808670, 'DC', 'OPERATIONAL', NULL::NUMERIC(8,2), 'Verify current pricing, power, and availability before travel.', 'Motorway EV fast charging location reported on public charging maps. Verify current operation before travel.', NULL::VARCHAR(255), 'https://openchargemap.org/poi?CountryIDs=170&CountryName=pakistan', 'OpenChargeMap Pakistan charging locations', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'UNVERIFIED', 170),
        ('CCS2_DC', 'Zeta Mall EV Charger', 'Rawalpindi', 'GT Road', 'Zeta Mall, next to Giga Mall, GT Road, Rawalpindi', NULL::NUMERIC(10,7), NULL::NUMERIC(10,7), 'DC', 'OPERATIONAL', NULL::NUMERIC(8,2), 'Verify current pricing, power, and availability before travel.', 'EV fast charging location reported at Zeta Mall on public charging maps. Verify current operation before travel.', NULL::VARCHAR(255), 'https://openchargemap.org/poi?CountryIDs=170&CountryName=pakistan', 'OpenChargeMap Pakistan charging locations', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'UNVERIFIED', 180),
        ('CCS2_DC', 'HUBCO Green PSO Magic River', 'Lahore', 'Near Ravi Toll Plaza', 'PSO Magic River, M-2 Motorway near Ravi Toll Plaza, Lahore', 31.5457410, 74.2552059, 'DC', 'OPERATIONAL', NULL::NUMERIC(8,2), 'Verify current pricing, power, and availability before travel.', 'HUBCO Green / PSO fast charging location reported on public charging maps. Verify current operation before travel.', NULL::VARCHAR(255), 'https://openchargemap.org/poi?CountryIDs=170&CountryName=pakistan', 'OpenChargeMap Pakistan charging locations', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'UNVERIFIED', 190),
        ('CCS2_DC', 'EV Charger Hardee''s Peshawar Cantt', 'Peshawar', 'Peshawar Cantt', 'Hardee''s, Peshawar Cantt, Peshawar', 34.0100187, 71.5392001, 'DC', 'OPERATIONAL', NULL::NUMERIC(8,2), 'Verify current pricing, power, and availability before travel.', 'EV fast charging location reported near Hardee''s Peshawar Cantt on public charging maps. Verify current operation before travel.', NULL::VARCHAR(255), 'https://openchargemap.org/poi?CountryIDs=170&CountryName=pakistan', 'OpenChargeMap Pakistan charging locations', '2026-05-26 00:00:00+05'::TIMESTAMP WITH TIME ZONE, 'UNVERIFIED', 200)
),
resolved_charger AS (
    SELECT
        charger_type.id AS charger_type_id,
        seed_charger.name,
        seed_charger.city,
        seed_charger.area,
        seed_charger.address,
        seed_charger.latitude,
        seed_charger.longitude,
        seed_charger.charging_type,
        seed_charger.status,
        seed_charger.power_kw,
        seed_charger.price_note,
        seed_charger.description,
        seed_charger.image,
        seed_charger.source_url,
        seed_charger.source_label,
        seed_charger.source_checked_at,
        seed_charger.verification_status,
        seed_charger.display_order
    FROM seed_charger
    JOIN charger_type ON charger_type.code = seed_charger.charger_type_code
)
INSERT INTO charger (
    charger_type_id,
    name,
    city,
    area,
    address,
    latitude,
    longitude,
    charging_type,
    status,
    power_kw,
    price_note,
    description,
    image,
    source_url,
    source_label,
    source_checked_at,
    verification_status,
    display_order,
    created_by
)
SELECT
    resolved_charger.charger_type_id,
    resolved_charger.name,
    resolved_charger.city,
    resolved_charger.area,
    resolved_charger.address,
    resolved_charger.latitude,
    resolved_charger.longitude,
    resolved_charger.charging_type,
    resolved_charger.status,
    resolved_charger.power_kw,
    resolved_charger.price_note,
    resolved_charger.description,
    resolved_charger.image,
    resolved_charger.source_url,
    resolved_charger.source_label,
    resolved_charger.source_checked_at,
    resolved_charger.verification_status,
    resolved_charger.display_order,
    'system'
FROM resolved_charger
WHERE NOT EXISTS (
    SELECT 1
    FROM charger
    WHERE charger.name = resolved_charger.name
      AND charger.city = resolved_charger.city
      AND COALESCE(charger.area, '') = COALESCE(resolved_charger.area, '')
);
