package com.example.Attendance_management_app.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.Attendance_management_app.dto.SalaryResult;
import com.example.Attendance_management_app.entity.Attendance;
import com.example.Attendance_management_app.entity.JobSalary;
import com.example.Attendance_management_app.entity.SkillSalary;
import com.example.Attendance_management_app.entity.User;
import com.example.Attendance_management_app.repository.JobSalaryRepository;
import com.example.Attendance_management_app.repository.SkillSalaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalaryCalculationService {

    private final JobSalaryRepository jobSalaryRepository;
    private final SkillSalaryRepository skillSalaryRepository;

    // ============================
    // メイン計算
    // ============================
    public SalaryResult calculate(Attendance attendance, User user) {

        if (attendance.getCheckIn() == null || attendance.getCheckOut() == null) {
            throw new IllegalStateException("出勤・退勤が未確定です");
        }

        // 出勤日基準
        LocalDate workDate = attendance.getRecordDate();

        // マスタ取得
        Map<Long, JobSalary> jobMap = jobSalaryRepository.findAll()
            .stream().collect(Collectors.toMap(JobSalary::getId, j -> j));

        Map<Long, SkillSalary> skillMap = skillSalaryRepository.findAll()
            .stream().collect(Collectors.toMap(SkillSalary::getId, s -> s));

        // 基本時給
        int baseHourly = getBaseHourly(user, jobMap, skillMap, workDate);

        // 分単位計算
        return calculateByMinute(attendance, baseHourly, workDate);
    }

    // ============================
    // 時給算出
    // ============================
    private int getBaseHourly(
            User user,
            Map<Long, JobSalary> jobMap,
            Map<Long, SkillSalary> skillMap,
            LocalDate workDate
    ) {
        int hourly = 0;

        // 職務給（単一）
        if (user.getJobs() != null && user.getJobs().length > 0) {
            JobSalary job = jobMap.get(Long.valueOf(user.getJobs()[0]));
            if (job != null) {
                hourly += job.getJobSalary();
            }
        }

        // 職能給（複数）
        if (user.getSkills() != null) {
            for (Integer skillId : user.getSkills()) {
                SkillSalary skill = skillMap.get(Long.valueOf(skillId));
                if (skill != null) {
                    hourly += skill.getSkillSalary();
                }
            }
        }

        // 土日加給（+50円）
        if (isWeekend(workDate)) {
            hourly += 50;
        }

        return hourly;
    }

    // ============================
    // 分単位処理
    // ============================
    private SalaryResult calculateByMinute(
            Attendance attendance,
            int baseHourly,
            LocalDate workDate
    ) {
        LocalDateTime start = attendance.getCheckIn();
        LocalDateTime end   = attendance.getCheckOut();

        int totalMinutes = 0;
        int nightMinutes = 0;
        int overtimeMinutes = 0;
        int totalSalary = 0;

        for (LocalDateTime t = start; t.isBefore(end); t = t.plusMinutes(1)) {

            if (isBreakTime(t, attendance)) {
                continue;
            }

            totalMinutes++;

            boolean isNight = isNightTime(t);
            boolean isOvertime = isOvertime(t, start);

            double multiplier = 1.0;
            if (isNight) multiplier *= 1.25;
            if (isOvertime) multiplier *= 1.25;

            if (isNight) nightMinutes++;
            if (isOvertime) overtimeMinutes++;

            double minuteWage = (baseHourly / 60.0) * multiplier;
            totalSalary += Math.floor(minuteWage);
        }

        return new SalaryResult(
            totalMinutes,
            nightMinutes,
            overtimeMinutes,
            totalSalary
        );
    }

    // ============================
    // 判定系
    // ============================
    private boolean isBreakTime(LocalDateTime t, Attendance a) {
        if (a.getBreakStart() == null || a.getBreakEnd() == null) {
            return false;
        }
        return !t.isBefore(a.getBreakStart()) && t.isBefore(a.getBreakEnd());
    }

    private boolean isNightTime(LocalDateTime t) {
        int hour = t.getHour();
        return (hour >= 22 || hour < 5);
    }

    private boolean isOvertime(LocalDateTime t, LocalDateTime start) {
        return t.isAfter(start.plusHours(8));
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek d = date.getDayOfWeek();
        return d == DayOfWeek.SATURDAY || d == DayOfWeek.SUNDAY;
    }
}
