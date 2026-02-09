package com.example.Attendance_management_app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.Attendance_management_app.entity.Attendance;
import com.example.Attendance_management_app.entity.User;
import com.example.Attendance_management_app.repository.AttendanceRepository;
import com.example.Attendance_management_app.repository.UserRepository;

@Service
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            UserRepository userRepository
    ) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
    }

    // -------------------------
    // 打刻
    // -------------------------
    public void punch(User user, String type) {

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end   = today.plusDays(1).atStartOfDay();

        Attendance attendance = attendanceRepository
                .findByUserAndCheckInTimeBetween(user, start, end)
                .orElseGet(() -> {
                    Attendance a = new Attendance();
                    a.setUser(user);
                    return a;
                });

        LocalDateTime now = LocalDateTime.now();

        switch (type.toUpperCase()) {
            case "CHECKIN"    -> attendance.setCheckInTime(now);
            case "CHECKOUT"   -> attendance.setCheckOutTime(now);
            case "BREAKSTART" -> attendance.setBreakStartTime(now);
            case "BREAKEND"   -> attendance.setBreakEndTime(now);
            default -> throw new IllegalArgumentException("Unknown punch type: " + type);
        }

        attendanceRepository.save(attendance);
    }

    // -------------------------
    // 取得系
    // -------------------------
    public Attendance getLastAttendance(User user) {
        return attendanceRepository
                .findTopByUserOrderByCheckInTimeDesc(user)
                .orElse(null);
    }

    public Optional<Attendance> getAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }

    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    public List<Attendance> getUserAttendance(User user) {
        return attendanceRepository.findByUserOrderByCheckInTimeAsc(user);
    }

    // 管理者用：期間イベント取得
    public List<Attendance> getEvents(Long userId, LocalDate start, LocalDate end) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        LocalDateTime from = start.atStartOfDay();
        LocalDateTime to   = end.plusDays(1).atStartOfDay();

        return attendanceRepository
                .findByUserAndCheckInTimeBetween(user, from, to)
                .stream()
                .toList();
    }

    public List<String> detectAnomalies() {
        return List.of("未実装の異常検知");
    }
}
