package com.example.attendance_management_app.entity;

public enum ApplicationStatus {

    PENDING("申請中"),
    APPROVED("承認"),
    REJECTED("却下");

    private final String label;

    ApplicationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
