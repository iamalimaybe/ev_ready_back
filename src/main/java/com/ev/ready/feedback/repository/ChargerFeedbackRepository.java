package com.ev.ready.feedback.repository;

import com.ev.ready.feedback.domain.ChargerFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChargerFeedbackRepository extends JpaRepository<ChargerFeedback, Long>, JpaSpecificationExecutor<ChargerFeedback> {
}
