package com.example.attendance_management_app.controller.employee;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.attendance_management_app.entity.User;
import com.example.attendance_management_app.repository.UserRepository;
import com.example.attendance_management_app.service.AttendanceService;

@Controller
@RequestMapping("/employee")
public class EmployeeDashboardController {

    private final AttendanceService attendanceService;
    private final UserRepository userRepository;

    public EmployeeDashboardController(
            AttendanceService attendanceService,
            UserRepository userRepository) {
        this.attendanceService = attendanceService;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        User user = userRepository.findByName(userDetails.getUsername())
                .orElseThrow();

        // 一覧（今まで通り）
        model.addAttribute("attendanceRecords",
                attendanceService.getUserAttendance(user));

        // ★ 追加：現在状態判定用
        model.addAttribute("attendance",
                attendanceService.getLastAttendance(user));

        return "employee/dashboard";
    }
}
