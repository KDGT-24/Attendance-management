package com.example.Attendance_management_app.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Attendance_management_app.entity.Attendance;
import com.example.Attendance_management_app.repository.UserRepository;
import com.example.Attendance_management_app.service.AttendanceService;
import com.example.Attendance_management_app.service.FixRequestService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AttendanceService attendanceService;
    private final FixRequestService fixRequestService;
    private final UserRepository userRepository;

    public AdminController(AttendanceService attendanceService, FixRequestService fixRequestService,
                           UserRepository userRepository) {
        this.attendanceService = attendanceService;
        this.fixRequestService = fixRequestService;
        this.userRepository = userRepository;
    }


    @GetMapping("/attendance")
    public String attendance(
            @RequestParam Long userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            Model model
    ) {
        model.addAttribute(
                "events",
                attendanceService.getEvents(userId, startDate, endDate)
        );
        return "admin_attendance_events";
    }

    @GetMapping("/fix-requests")
    public String listFixRequests(Model model) {
        model.addAttribute("fixRequests", fixRequestService.getAllPendingFixRequests());
        return "fix_request_list";
    }

    @PostMapping("/fix-requests/{id}/approve")
    public String approveFixRequest(@PathVariable("id") Long requestId) {
        fixRequestService.approveFixRequest(requestId);
        return "redirect:/admin/fix-requests?success=approved";
    }

    @PostMapping("/fix-requests/{id}/reject")
    public String rejectFixRequest(@PathVariable("id") Long requestId) {
        fixRequestService.rejectFixRequest(requestId);
        return "redirect:/admin/fix-requests?success=rejected";
    }

    @GetMapping("/alerts")
    public String showAnomalyDetection(Model model) {
        model.addAttribute("anomalies", attendanceService.detectAnomalies());
        return "anomaly_detection";
    }

    @GetMapping("/attendance/csv")
    public void exportAttendanceCsv(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            HttpServletResponse response) throws IOException {

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"attendance_records.csv\"");

        List<Attendance> records = attendanceService.getAllAttendance();

        if (userId != null) {
            records = records.stream()
                    .filter(att -> att.getUser().getId().equals(userId))
                    .collect(Collectors.toList());
        }

        if (startDate != null) {
            records = records.stream()
                    .filter(att ->
                            att.getCheckInTime() != null &&
                            !att.getCheckInTime().toLocalDate().isBefore(startDate)
                    )
                    .collect(Collectors.toList());
        }

        if (endDate != null) {
            records = records.stream()
                    .filter(att ->
                            att.getCheckOutTime() != null &&
                            !att.getCheckOutTime().toLocalDate().isAfter(endDate)
                    )
                    .collect(Collectors.toList());
        }

        try (PrintWriter writer = response.getWriter()) {
            writer.append("ID,ユーザー名,日付,時刻,ステータス,場所\n");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (Attendance record : records) {
            	LocalDateTime checkIn = record.getCheckInTime();

            	writer.append(String.format("%d,%s,%s,%s,%s,%s\n",
            	        record.getId(),
            	        record.getUser().getUsername(),
            	        checkIn != null ? checkIn.toLocalDate() : "",
            	        checkIn != null ? checkIn.format(formatter) : "",
            	        record.getStatus(),
            	        record.getLocation() != null ? record.getLocation() : ""
            	));
            }
        }
    }
}
