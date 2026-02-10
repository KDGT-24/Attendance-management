package com.example.attendance_management_app.service;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.attendance_management_app.dto.MonthlySalaryResult;
import com.example.attendance_management_app.entity.Attendance;
import com.example.attendance_management_app.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonthlySalaryService {

    private final AttendanceService attendanceService;
    private final SalaryCalculationService calculationService;

    /**
     * 月次給与計算
     */
    public MonthlySalaryResult calculate(
            User user,
            YearMonth yearMonth
    ) {
        // 月内の勤怠を取得
        List<Attendance> records =
                attendanceService.getUserAttendanceByMonth(user, yearMonth);

        // 総労働時間
        int totalMinutes =
                calculationService.calculateWorkingMinutes(records);

        // 給与計算
        int salary =
                calculationService.calculateMonthlySalary(user, records);

        return new MonthlySalaryResult(
                yearMonth,
                totalMinutes,
                0, // nightMinutes（後で拡張）
                0, // overtimeMinutes（後で拡張）
                salary
        );
    }
}
