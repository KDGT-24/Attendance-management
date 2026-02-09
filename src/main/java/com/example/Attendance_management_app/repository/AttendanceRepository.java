package com.example.Attendance_management_app.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Attendance_management_app.entity.Attendance;
import com.example.Attendance_management_app.entity.User;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // 今日の勤怠（1日1レコード）
    Optional<Attendance> findByUserAndCheckInTimeBetween(
            User user,
            LocalDateTime start,
            LocalDateTime end
    );

    // ユーザーの最新勤怠
    Optional<Attendance> findTopByUserOrderByCheckInTimeDesc(User user);

    // ユーザーの全勤怠（昇順）
    List<Attendance> findByUserOrderByCheckInTimeAsc(User user);

    // 管理者用：全件
    List<Attendance> findAll();
}
