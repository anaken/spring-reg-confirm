package ru.test.service.dto;

public class ConfirmRegistrationDto {

    private String userId;

    private boolean accepted;

    public ConfirmRegistrationDto() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
