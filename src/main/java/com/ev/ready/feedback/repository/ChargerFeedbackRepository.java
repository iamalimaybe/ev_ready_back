package com.ev.ready.feedback.repository;

import com.ev.ready.feedback.domain.ChargerFeedback;
import com.ev.ready.feedback.enums.ChargerFeedbackStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChargerFeedbackRepository extends JpaRepository<ChargerFeedback, Long>, JpaSpecificationExecutor<ChargerFeedback> {

    Page<ChargerFeedback> findByChargerIdAndFeedbackStatus(
            Long chargerId,
            ChargerFeedbackStatus feedbackStatus,
            Pageable pageable
    );
}
