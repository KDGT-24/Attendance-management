package com.example.attendance_management_app.controller.employee;

import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.attendance_management_app.dto.MonthlySalaryResult;
import com.example.attendance_management_app.entity.User;
import com.example.attendance_management_app.repository.UserRepository;
import com.example.attendance_management_app.service.MonthlySalaryService;

@Controller
@RequestMapping("/employee/salary")
public class EmployeeSalaryController {

    private final MonthlySalaryService monthlySalaryService;
    private final UserRepository userRepository;

    public EmployeeSalaryController(
            MonthlySalaryService monthlySalaryService,
            UserRepository userRepository) {
        this.monthlySalaryService = monthlySalaryService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String view(
            @AuthenticationPrincipal(expression = "username") String username,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            Model model) {

        User user = userRepository.findByName(username).orElseThrow();
        YearMonth target = (month != null) ? month : YearMonth.now();

        MonthlySalaryResult result =
                monthlySalaryService.calculate(user, target);

        model.addAttribute("result", result);
        model.addAttribute("month", target);

        return "employee/salary/view";
    }
}
