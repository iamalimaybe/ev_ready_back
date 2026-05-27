--liquibase formatted sql

--changeset evready:V008-seed-brand-data
INSERT INTO brand (name, description, is_active, display_order, created_by)
VALUES
    ('BYD', 'Electric and new-energy vehicle brand represented in Pakistan by Mega Motor Company.', TRUE, 10, 'system'),
    ('MG', 'Automotive brand in Pakistan with electric and hybrid vehicle offerings.', TRUE, 20, 'system'),
    ('Deepal', 'Changan''s EV-focused brand represented in Pakistan with electric car models.', TRUE, 30, 'system'),
    ('Hyundai', 'Hyundai Nishat brand offering IONIQ electric vehicles in Pakistan.', TRUE, 40, 'system'),
    ('Metro EV', 'Pakistan electric two-wheeler brand offering electric bikes and scooters.', TRUE, 50, 'system'),
    ('Jolta Electric', 'Pakistan electric two-wheeler brand focused on electric bikes and scooters.', TRUE, 60, 'system'),
    ('Saige', 'Electric two-wheeler brand offering electric motorcycles and scooters in Pakistan.', TRUE, 70, 'system'),
    ('Vlektra', 'Pakistan electric motorcycle brand focused on electric two-wheelers.', TRUE, 80, 'system'),
    ('Evee', 'Electric two-wheeler brand offering electric scooters and bikes in Pakistan.', TRUE, 90, 'system'),
    ('Yadea', 'Electric two-wheeler brand with electric scooters and bikes available in Pakistan through local channels.', TRUE, 100, 'system'),
    ('Pakzon Electric', 'Electric two-wheeler brand offering electric bikes and scooters in Pakistan.', TRUE, 110, 'system'),
    ('E Turbo', 'Electric two-wheeler brand offering electric bikes and scooters in Pakistan.', TRUE, 120, 'system'),
    ('REVOO', 'Electric two-wheeler brand offering electric mobility options in Pakistan.', TRUE, 130, 'system'),
    ('Crown EV', 'Electric two-wheeler brand associated with electric bike and scooter offerings in Pakistan.', TRUE, 140, 'system'),
    ('Road Prince', 'Two-wheeler brand with electric bike and scooter offerings in Pakistan.', TRUE, 150, 'system'),
    ('MS Jaguar', 'Electric two-wheeler brand offering electric bikes and scooters in Pakistan.', TRUE, 160, 'system'),
    ('Honda', 'Automotive and two-wheeler brand with electric mobility models in selected markets and channels.', TRUE, 170, 'system'),
    ('Honri', 'Compact electric vehicle brand available in Pakistan through local automotive channels.', TRUE, 180, 'system'),
    ('Audi', 'Premium automotive brand with electric vehicle models available through import or local channels.', TRUE, 190, 'system'),
    ('Seres', 'Electric vehicle brand available in Pakistan through local automotive channels.', TRUE, 200, 'system'),
    ('Omoda', 'Automotive brand with new-energy and electric vehicle offerings in selected markets.', TRUE, 210, 'system'),
    ('Jaecoo', 'Automotive brand with new-energy and electric vehicle offerings in selected markets.', TRUE, 220, 'system'),
    ('Dongfeng', 'Chinese automotive brand with electric vehicle models available through selected markets and channels.', TRUE, 230, 'system'),
    ('Forthing', 'Dongfeng-backed automotive brand with electric and new-energy vehicle models in selected markets.', TRUE, 240, 'system'),
    ('Kaiyi', 'Automotive brand with electric and new-energy vehicle models in selected markets.', TRUE, 250, 'system'),
    ('GAC Aion', 'Electric vehicle brand under GAC with EV models introduced through local automotive channels.', TRUE, 260, 'system'),
    ('Hyptec', 'Premium electric vehicle brand under GAC with EV models introduced through local automotive channels.', TRUE, 270, 'system'),
    ('GUGO', 'Electric vehicle brand represented in Pakistan through local automotive channels.', TRUE, 280, 'system'),
    ('ORA', 'Electric vehicle brand under Great Wall Motor known for compact EV models in selected markets.', TRUE, 290, 'system')
ON CONFLICT (name) DO UPDATE
SET description = EXCLUDED.description,
    is_active = EXCLUDED.is_active,
    display_order = EXCLUDED.display_order,
    updated_at = CURRENT_TIMESTAMP,
    updated_by = 'system';
