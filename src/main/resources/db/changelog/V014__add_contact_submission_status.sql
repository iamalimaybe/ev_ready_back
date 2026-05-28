--liquibase formatted sql

--changeset evready:V014-add-contact-submission-status
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'contact_submission' AND column_name = 'contact_status';
ALTER TABLE contact_submission ADD COLUMN contact_status VARCHAR(40);
UPDATE contact_submission SET contact_status = 'NEW' WHERE contact_status IS NULL;
ALTER TABLE contact_submission ALTER COLUMN contact_status SET DEFAULT 'NEW';
ALTER TABLE contact_submission ALTER COLUMN contact_status SET NOT NULL;

CREATE INDEX idx_contact_submission_contact_status ON contact_submission (contact_status);
