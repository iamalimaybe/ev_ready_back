package com.ev.ready.lead.repository;

import com.ev.ready.lead.domain.LeadSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadSubmissionRepository extends JpaRepository<LeadSubmission, Long> {
}
