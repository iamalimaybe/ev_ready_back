--liquibase formatted sql

--changeset evready:V003-seed-charger-type-data
INSERT INTO charger_type (code, name, description, is_active, display_order, created_by)
VALUES
    ('TYPE2_AC', 'Type 2 AC', 'AC charging connector commonly used by EV cars. Verify vehicle and charger compatibility before use.', TRUE, 10, 'system'),
    ('CCS2_DC', 'CCS2 DC fast charging', 'DC fast charging connector commonly used by many modern EV cars. Verify vehicle and charger compatibility before use.', TRUE, 20, 'system'),
    ('GB_T', 'GB/T', 'Connector standard used by some EVs, especially some Chinese/imported models. Verify compatibility before use.', TRUE, 30, 'system'),
    ('CHADEMO', 'CHAdeMO', 'DC fast charging connector used by some older/imported EVs. Verify compatibility before use.', TRUE, 40, 'system'),
    ('THREE_PIN', '3-pin home charging', 'Standard home/portable charging category often relevant for EV bikes or low-power charging. Verify safety and manufacturer guidance before use.', TRUE, 50, 'system'),
    ('PORTABLE_AC_CHARGER', 'Portable AC charger', 'Portable AC charger supplied with an EV bike, scooter, or compact EV for home or low-power charging. Verify manufacturer guidance before use.', TRUE, 55, 'system'),
    ('PROPRIETARY', 'Proprietary / model-specific', 'Model-specific or non-standard connector category. Confirm with manufacturer/dealer before use.', TRUE, 60, 'system'),
    ('OTHER', 'Other / confirm before use', 'Fallback connector/category where the exact connector is not known.', TRUE, 90, 'system')
ON CONFLICT (code) DO UPDATE
SET name = EXCLUDED.name,
    description = EXCLUDED.description,
    is_active = EXCLUDED.is_active,
    display_order = EXCLUDED.display_order,
    updated_at = CURRENT_TIMESTAMP,
    updated_by = 'system';
