package com.example.attendance_management_app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.attendance_management_app.entity.Attendance;
import com.example.attendance_management_app.entity.PunchType;
import com.example.attendance_management_app.entity.User;
import com.example.attendance_management_app.repository.AttendanceRepository;
import com.example.attendance_management_app.repository.UserRepository;

import jakarta.transaction.Transactional;

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
    @Transactional
    public void punch(User user, PunchType type) {

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end   = today.plusDays(1).atStartOfDay();

        Attendance attendance = attendanceRepository
                .findFirstByUserAndCheckInTimeBetween(user, start, end)
                .orElseGet(() -> {
                    Attendance a = new Attendance();
                    a.setUser(user);
                    return a;
                });

        LocalDateTime now = LocalDateTime.now();

        switch (type) {
            case CHECKIN -> {
                if (attendance.getCheckInTime() == null) {
                    attendance.setCheckInTime(now);
                }
            }
            case BREAK_START -> {
                if (attendance.getCheckInTime() != null
                        && attendance.getBreakStartTime() == null) {
                    attendance.setBreakStartTime(now);
                }
            }
            case BREAK_END -> {
                if (attendance.getBreakStartTime() != null
                        && attendance.getBreakEndTime() == null) {
                    attendance.setBreakEndTime(now);
                }
            }
            case CHECKOUT -> {
                if (attendance.getCheckInTime() != null
                        && attendance.getCheckOutTime() == null) {
                    attendance.setCheckOutTime(now);
                }
            }
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
                .findAllByUserAndCheckInTimeBetween(user, from, to);
    }

    public List<Attendance> getUserAttendanceByMonth(
        User user,
        YearMonth yearMonth
    ) {
        LocalDateTime from = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime to   = yearMonth.plusMonths(1).atDay(1).atStartOfDay();

        return attendanceRepository.findAllByUserAndCheckInTimeBetween(
            user, from, to
        );
    }

    public List<String> detectAnomalies() {
        return List.of("未実装の異常検知");
    }
}
