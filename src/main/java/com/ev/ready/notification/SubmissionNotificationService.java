package com.ev.ready.notification;

import com.ev.ready.contact.domain.ContactSubmission;
import com.ev.ready.lead.domain.LeadSubmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class SubmissionNotificationService {

    private static final Logger log = LoggerFactory.getLogger(SubmissionNotificationService.class);

    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    private final boolean enabled;
    private final String from;
    private final String leadNotificationTo;
    private final String contactNotificationTo;

    public SubmissionNotificationService(
            ObjectProvider<JavaMailSender> mailSenderProvider,
            @Value("${evready.email.notifications.enabled:false}") boolean enabled,
            @Value("${evready.email.notifications.from:}") String from,
            @Value("${evready.email.notifications.lead-to:}") String leadNotificationTo,
            @Value("${evready.email.notifications.contact-to:}") String contactNotificationTo
    ) {
        this.mailSenderProvider = mailSenderProvider;
        this.enabled = enabled;
        this.from = from;
        this.leadNotificationTo = leadNotificationTo;
        this.contactNotificationTo = contactNotificationTo;
    }

    public void notifyLeadSubmission(LeadSubmission leadSubmission) {
        if (!enabled) {
            return;
        }

        runAfterCommit(() -> sendLeadSubmissionNotification(leadSubmission));
    }

    public void notifyContactSubmission(ContactSubmission contactSubmission) {
        if (!enabled) {
            return;
        }

        runAfterCommit(() -> sendContactSubmissionNotification(contactSubmission));
    }

    private void sendLeadSubmissionNotification(LeadSubmission leadSubmission) {
        if (!isConfigured(leadNotificationTo)) {
            log.warn("Lead notification email skipped for submission id {} because email settings are incomplete.",
                    leadSubmission.getId());
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(leadNotificationTo);
        message.setSubject("New EVReady Get Help submission #" + leadSubmission.getId());
        message.setText("""
                New Get Help submission received.

                Submission ID: %s
                Created At: %s
                Name: %s
                Phone: %s
                City: %s
                Interest Type: %s
                Source Page: %s

                Message:
                %s
                """.formatted(
                value(leadSubmission.getId()),
                value(leadSubmission.getCreatedAt()),
                value(leadSubmission.getName()),
                value(leadSubmission.getPhone()),
                value(leadSubmission.getCity()),
                value(leadSubmission.getInterestType()),
                value(leadSubmission.getSourcePage()),
                value(leadSubmission.getMessage())
        ));

        send(message, "lead", leadSubmission.getId());
    }

    private void sendContactSubmissionNotification(ContactSubmission contactSubmission) {
        if (!isConfigured(contactNotificationTo)) {
            log.warn("Contact notification email skipped for submission id {} because email settings are incomplete.",
                    contactSubmission.getId());
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(contactNotificationTo);
        message.setSubject("New EVReady Contact submission #" + contactSubmission.getId());
        message.setText("""
                New Contact Us submission received.

                Submission ID: %s
                Created At: %s
                Name: %s
                Email: %s
                Phone: %s
                Organization: %s
                Inquiry Type: %s
                Source Page: %s

                Message:
                %s
                """.formatted(
                value(contactSubmission.getId()),
                value(contactSubmission.getCreatedAt()),
                value(contactSubmission.getName()),
                value(contactSubmission.getEmail()),
                value(contactSubmission.getPhone()),
                value(contactSubmission.getOrganization()),
                value(contactSubmission.getInquiryType()),
                value(contactSubmission.getSourcePage()),
                value(contactSubmission.getMessage())
        ));

        send(message, "contact", contactSubmission.getId());
    }

    private void send(SimpleMailMessage message, String submissionType, Long submissionId) {
        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null) {
            log.warn("{} notification email skipped for submission id {} because no mail sender is configured.",
                    submissionType, submissionId);
            return;
        }

        try {
            mailSender.send(message);
        } catch (RuntimeException ex) {
            log.warn("Failed to send {} notification email for submission id {}: {}",
                    submissionType, submissionId, ex.getMessage());
        }
    }

    private boolean isConfigured(String recipient) {
        return hasText(from) && hasText(recipient);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String value(Object value) {
        return value == null ? "(not provided)" : value.toString();
    }

    private void runAfterCommit(Runnable action) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            action.run();
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                action.run();
            }
        });
    }
}
