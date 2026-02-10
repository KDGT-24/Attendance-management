package com.example.attendance_management_app.controller.employee;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.attendance_management_app.entity.Attendance;
import com.example.attendance_management_app.entity.FixEventType;
import com.example.attendance_management_app.entity.PunchType;
import com.example.attendance_management_app.entity.User;
import com.example.attendance_management_app.repository.UserRepository;
import com.example.attendance_management_app.service.AttendanceService;
import com.example.attendance_management_app.service.FixRequestService;

@Controller
@RequestMapping("/employee/attendance")
public class EmployeeAttendanceController {

    private final AttendanceService attendanceService;
    private final UserRepository userRepository;
    private final FixRequestService fixRequestService;

    public EmployeeAttendanceController(
            AttendanceService attendanceService,
            UserRepository userRepository,
            FixRequestService fixRequestService) {
        this.attendanceService = attendanceService;
        this.userRepository = userRepository;
        this.fixRequestService = fixRequestService;
    }

    @PostMapping("/punch")
    public String punch(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String type) {

        User user = userRepository.findByName(userDetails.getUsername())
                .orElseThrow();

				attendanceService.punch(
								user,
								PunchType.valueOf(type.toUpperCase())
				);
        return "redirect:/employee/dashboard";
    }

    @GetMapping("/history")
    public String history(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        User user = userRepository.findByName(userDetails.getUsername())
                .orElseThrow();

        model.addAttribute("attendanceRecords",
                attendanceService.getUserAttendance(user));

        return "employee/attendance_history";
    }

    @GetMapping("/history/{id}/request-fix")
    public String fixForm(@PathVariable Long id, Model model) {

        Optional<Attendance> attendance =
                attendanceService.getAttendanceById(id);

        if (attendance.isEmpty()) {
            return "redirect:/employee/attendance/history";
        }

        model.addAttribute("attendance", attendance.get());
        return "employee/fix_request_form";
    }

    @PostMapping("/history/{id}/request-fix")
    public String submitFix(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestParam FixEventType eventType,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime newTime,
            @RequestParam String reason) {

        User user = userRepository.findByName(userDetails.getUsername())
                .orElseThrow();

        fixRequestService.createFixRequest(
                user, id, eventType, newTime, reason);


        return "redirect:/employee/attendance/history?success";
    }
}
