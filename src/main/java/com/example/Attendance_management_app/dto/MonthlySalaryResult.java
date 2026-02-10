package com.example.attendance_management_app.dto;

import java.time.YearMonth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlySalaryResult {

    private YearMonth yearMonth;

    private int totalMinutes;
    private int nightMinutes;
    private int overtimeMinutes;

    private int totalSalary;
}
