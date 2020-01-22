package ru.test.persistence.entity;

import ru.test.persistence.enums.SendEmailStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class SendEmail {

    @Id
    @Column(length = 36)
    private String id;

    private String fromEmail;

    private String toEmail;

    private String subject;

    private String bodyText;

    private Date created;

    private Date sent;

    private Integer attempt;

    @Enumerated(EnumType.STRING)
    private SendEmailStatus status;

    public SendEmail() {
    }

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String from) {
        this.fromEmail = from;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String to) {
        this.toEmail = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String text) {
        this.bodyText = text;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }

    public SendEmailStatus getStatus() {
        return status;
    }

    public void setStatus(SendEmailStatus status) {
        this.status = status;
    }
}
