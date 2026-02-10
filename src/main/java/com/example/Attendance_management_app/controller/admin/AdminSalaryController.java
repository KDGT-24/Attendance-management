package com.example.attendance_management_app.controller.admin;

import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.attendance_management_app.service.AdminSalaryService;
import com.example.attendance_management_app.service.JobSalaryService;
import com.example.attendance_management_app.service.SkillSalaryService;

@Controller
@RequestMapping("/admin/salary")
public class AdminSalaryController {

    private final AdminSalaryService adminSalaryService;
    private final JobSalaryService jobSalaryService;
    private final SkillSalaryService skillSalaryService;

    public AdminSalaryController(
            AdminSalaryService adminSalaryService,
            JobSalaryService jobSalaryService,
            SkillSalaryService skillSalaryService) {

        this.adminSalaryService = adminSalaryService;
        this.jobSalaryService = jobSalaryService;
        this.skillSalaryService = skillSalaryService;
    }

    // ===== 一覧（集計） =====
    @GetMapping
    public String list(
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            Model model) {

        YearMonth target = (month != null) ? month : YearMonth.now();

        model.addAttribute(
            "results",
            adminSalaryService.calculateAll(target)
        );
        model.addAttribute("month", target);

        return "admin/salary_list";
    }

    // ===== 職務給 作成 =====
    @PostMapping("/job/create")
    public String createJob(
            @RequestParam String name,
            @RequestParam int salary) {

        jobSalaryService.create(name, salary);
        return "redirect:/admin/salary";
    }

    // ===== 職能給 作成 =====
    @PostMapping("/skill/create")
    public String createSkill(
            @RequestParam String name,
            @RequestParam int salary) {

        skillSalaryService.create(name, salary);
        return "redirect:/admin/salary";
    }
}
