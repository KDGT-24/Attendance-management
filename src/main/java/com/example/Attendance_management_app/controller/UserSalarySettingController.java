package com.example.Attendance_management_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Attendance_management_app.entity.User;
import com.example.Attendance_management_app.repository.UserRepository;
import com.example.Attendance_management_app.service.JobSalaryService;
import com.example.Attendance_management_app.service.SkillSalaryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserSalarySettingController {

    private final UserRepository userRepository;

    private final JobSalaryService jobSalaryService;
    private final SkillSalaryService skillSalaryService;
    
    @GetMapping("/admin/user")
    public String userSalaryPage(Model model) {
        model.addAttribute("jobs", jobSalaryService.findAll());
        model.addAttribute("skills", skillSalaryService.findAll());
        return "admin/user";
    }

    @PostMapping("/admin/user/salary/update")
    public String updateUserSalary(
            @RequestParam String employeeNumber,
            @RequestParam(required = false) Integer jobId,
            @RequestParam(required = false) Integer[] skillIds
    ) {

        int empNo;
        try {
            empNo = Integer.parseInt(employeeNumber);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("社員番号が不正です");
        }

        User user = userRepository.findByEmployeeNumber(empNo)
                .orElseThrow(() ->
                        new IllegalArgumentException("社員が存在しません")
                );

        // 職務（単一）
        if (jobId != null) {
            user.setJobs(new Integer[]{jobId});
        }

        // 職能（複数）
        user.setSkills(skillIds);

        userRepository.save(user);
        return "redirect:/admin/user";
    }
}
