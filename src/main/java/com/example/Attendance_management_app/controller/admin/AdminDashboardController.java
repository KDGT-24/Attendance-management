package com.example.attendance_management_app.controller.admin;

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
@RequestMapping("/admin")
public class AdminDashboardController {

    private final AttendanceService attendanceService;
    private final UserRepository userRepository;

    public AdminDashboardController(
            AttendanceService attendanceService,
            UserRepository userRepository) {
        this.attendanceService = attendanceService;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        model.addAttribute("allAttendanceRecords",
                attendanceService.getAllAttendance());

        User user = userRepository.findByName(userDetails.getUsername())
                .orElseThrow();

        model.addAttribute("user", user);
        return "admin/dashboard";
    }
    @GetMapping("/anomalies")
    public String anomalies(Model model) {
        model.addAttribute("anomalies", attendanceService.detectAnomalies());
        return "admin/anomaly_detection";
    }
}
