package com.example.attendance_management_app.controller.admin;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.attendance_management_app.service.AttendanceService;

@Controller
@RequestMapping("/admin/attendance")
public class AdminAttendanceController {

    private final AttendanceService attendanceService;

    public AdminAttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public String list(
            @RequestParam Long userId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,
            Model model) {

        if (startDate == null || endDate == null) {
            endDate = LocalDate.now();
            startDate = endDate.minusDays(30);
        }

        model.addAttribute("events",
                attendanceService.getEvents(userId, startDate, endDate));
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "admin/attendance_events";
    }
}
