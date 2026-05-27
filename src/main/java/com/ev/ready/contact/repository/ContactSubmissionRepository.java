package com.ev.ready.contact.repository;

import com.ev.ready.contact.domain.ContactSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactSubmissionRepository extends JpaRepository<ContactSubmission, Long> {
}
