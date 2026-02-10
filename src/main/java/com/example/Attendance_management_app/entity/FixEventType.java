package com.example.attendance_management_app.entity;

public enum FixEventType {
    CHECK_IN("出勤"),
    CHECK_OUT("退勤"),
    BREAK_START("休憩開始"),
    BREAK_END("休憩終了");

    private final String label;

    FixEventType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
