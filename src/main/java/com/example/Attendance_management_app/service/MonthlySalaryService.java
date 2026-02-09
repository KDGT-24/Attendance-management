package com.example.Attendance_management_app.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Attendance_management_app.dto.MonthlySalaryResult;
import com.example.Attendance_management_app.dto.SalaryResult;
import com.example.Attendance_management_app.entity.Attendance;
import com.example.Attendance_management_app.entity.User;
import com.example.Attendance_management_app.repository.AttendanceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonthlySalaryService {

    private final AttendanceRepository attendanceRepository;
    private final SalaryCalculationService salaryCalculationService;

    public MonthlySalaryResult calculateMonthly(User user, YearMonth yearMonth) {

        LocalDateTime from = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime to   = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Attendance> attendances =
                attendanceRepository.findByUserOrderByCheckInTimeAsc(user);

        int totalMinutes = 0;
        int nightMinutes = 0;
        int overtimeMinutes = 0;
        int totalSalary = 0;

        for (Attendance a : attendances) {
            if (a.getCheckOut() == null) {
                continue;
            }

            SalaryResult r = salaryCalculationService.calculate(a, user);

            totalMinutes    += r.getTotalMinutes();
            nightMinutes    += r.getNightMinutes();
            overtimeMinutes += r.getOvertimeMinutes();
            totalSalary     += r.getTotalSalary();
        }

        return new MonthlySalaryResult(
                yearMonth,
                totalMinutes,
                nightMinutes,
                overtimeMinutes,
                totalSalary
        );
    }
}
