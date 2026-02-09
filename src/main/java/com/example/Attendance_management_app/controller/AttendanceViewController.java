package com.example.Attendance_management_app.controller;

import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Attendance_management_app.dto.MonthlySalaryResult;
import com.example.Attendance_management_app.entity.User;
import com.example.Attendance_management_app.repository.UserRepository;
import com.example.Attendance_management_app.service.MonthlySalaryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AttendanceViewController {

    private final MonthlySalaryService monthlySalaryService;
    private final UserRepository userRepository;

    @GetMapping("/attendance")
    public String viewAttendance(
            @AuthenticationPrincipal(expression = "username") String username,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            Model model
    ) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("ユーザーが存在しません"));

        YearMonth targetMonth =
                (month != null) ? month : YearMonth.now();

        MonthlySalaryResult result =
                monthlySalaryService.calculateMonthly(user, targetMonth);

        model.addAttribute("result", result);
        model.addAttribute("month", targetMonth);

        return "attendance/view"; // Thymeleaf想定
    }
}
