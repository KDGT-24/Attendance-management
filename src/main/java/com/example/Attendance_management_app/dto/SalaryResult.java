package com.example.attendance_management_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalaryResult {

    private int totalMinutes;
    private int nightMinutes;
    private int overtimeMinutes;
    private int totalSalary;
}
