package com.example.attendance_management_app.service;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.attendance_management_app.entity.Attendance;
import com.example.attendance_management_app.entity.JobSalary;
import com.example.attendance_management_app.entity.SkillSalary;
import com.example.attendance_management_app.entity.User;
import com.example.attendance_management_app.repository.JobSalaryRepository;
import com.example.attendance_management_app.repository.SkillSalaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalaryCalculationService {

    private final JobSalaryRepository jobSalaryRepository;
    private final SkillSalaryRepository skillSalaryRepository;

    private static final int BASE_HOURLY_WAGE = 1000;

    public int calculateMonthlySalary(User user, List<Attendance> records) {

        int totalMinutes = calculateWorkingMinutes(records);

        int jobSalary = calculateJobSalary(user);
        int skillSalary = calculateSkillSalary(user);

        int hourlyWage = BASE_HOURLY_WAGE + jobSalary + skillSalary;

        return (int) Math.round((totalMinutes / 60.0) * hourlyWage);
    }

    // --------------------
    // 勤務時間
    // --------------------
    public int calculateWorkingMinutes(List<Attendance> records) {

        int minutes = 0;

        for (Attendance att : records) {
            if (att.getCheckInTime() != null && att.getCheckOutTime() != null) {
                minutes += Duration.between(
                        att.getCheckInTime(),
                        att.getCheckOutTime()
                ).toMinutes();
            }
        }
        return minutes;
    }

    // --------------------
    // 職務給（最大1つ）
    // --------------------
    private int calculateJobSalary(User user) {

        if (user.getJobs() == null || user.getJobs().length == 0) {
            return 0;
        }

        List<JobSalary> jobs =
                jobSalaryRepository.findByIdIn(
                        Arrays.stream(user.getJobs())
                              .map(Long::valueOf)
                              .toList()
                );

        return jobs.stream()
                   .mapToInt(JobSalary::getJobSalary)
                   .max()
                   .orElse(0);
    }

    // --------------------
    // 職能給（加算）
    // --------------------
    private int calculateSkillSalary(User user) {

        if (user.getSkills() == null || user.getSkills().length == 0) {
            return 0;
        }

        List<SkillSalary> skills =
                skillSalaryRepository.findByIdIn(
                        Arrays.stream(user.getSkills())
                              .map(Long::valueOf)
                              .toList()
                );

        return skills.stream()
                     .mapToInt(SkillSalary::getSkillSalary)
                     .sum();
    }
}
