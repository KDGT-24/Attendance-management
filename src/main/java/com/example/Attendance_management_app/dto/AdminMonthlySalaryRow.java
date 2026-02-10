package com.example.attendance_management_app.dto;

import java.time.YearMonth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminMonthlySalaryRow {

    private Long userId;
    private String username;
    private int totalMinutes;
    private int totalSalary;
    private YearMonth yearMonth;
}

