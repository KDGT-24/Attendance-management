package com.example.Attendance_management_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Attendance_management_app.service.JobSalaryService;
import com.example.Attendance_management_app.service.SkillSalaryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SalaryMasterController {

    private final JobSalaryService jobSalaryService;
    private final SkillSalaryService skillSalaryService;

    // 職務給追加
    @PostMapping("/admin/salary/job/create")
    public String createJobSalary(
            @RequestParam String name,
            @RequestParam int salary
    ) {
        jobSalaryService.create(name, salary);
        return "redirect:/admin/salary";
    }

    // 職能給追加
    @PostMapping("/admin/salary/skill/create")
    public String createSkillSalary(
            @RequestParam String name,
            @RequestParam int salary
    ) {
        skillSalaryService.create(name, salary);
        return "redirect:/admin/salary";
    }
}
