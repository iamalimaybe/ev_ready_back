package com.ev.ready.feedback.domain;

import com.ev.ready.feedback.enums.ChargerFeedbackStatus;
import com.ev.ready.feedback.enums.ChargerFeedbackType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "charger_feedback")
public class ChargerFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "charger_id", nullable = false)
    private Long chargerId;

    @Column
    private Integer rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_type", nullable = false, length = 40)
    private ChargerFeedbackType feedbackType;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "display_name", length = 120)
    private String displayName;

    @Column(length = 100)
    private String city;

    @Column(name = "reported_by_contact", length = 160)
    private String reportedByContact;

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_status", nullable = false, length = 40)
    private ChargerFeedbackStatus feedbackStatus = ChargerFeedbackStatus.PENDING;

    @Column(name = "reviewed_at")
    private OffsetDateTime reviewedAt;

    @Column(name = "reviewed_by", length = 100)
    private String reviewedBy;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChargerId() {
        return chargerId;
    }

    public void setChargerId(Long chargerId) {
        this.chargerId = chargerId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public ChargerFeedbackType getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(ChargerFeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getReportedByContact() {
        return reportedByContact;
    }

    public void setReportedByContact(String reportedByContact) {
        this.reportedByContact = reportedByContact;
    }

    public ChargerFeedbackStatus getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setFeedbackStatus(ChargerFeedbackStatus feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    public OffsetDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(OffsetDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChargerFeedback chargerFeedback)) {
            return false;
        }
        return id != null && id.equals(chargerFeedback.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
