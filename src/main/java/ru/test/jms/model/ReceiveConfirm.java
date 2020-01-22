package ru.test.jms.model;

public class ReceiveConfirm {

    private String userId;

    private Boolean accepted;

    public ReceiveConfirm() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
