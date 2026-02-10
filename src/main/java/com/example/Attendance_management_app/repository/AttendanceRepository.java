package com.example.attendance_management_app.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendance_management_app.entity.Attendance;
import com.example.attendance_management_app.entity.User;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    // ユーザーの最新勤怠
    Optional<Attendance> findTopByUserOrderByCheckInTimeDesc(User user);

    // ユーザーの全勤怠（昇順）
    List<Attendance> findByUserOrderByCheckInTimeAsc(User user);

    // 管理者用：全件
    List<Attendance> findAll();

    // punch 用（当日1件）
    Optional<Attendance> findFirstByUserAndCheckInTimeBetween(
            User user,
            LocalDateTime start,
            LocalDateTime end
    );

    // 一覧用（複数件）
    List<Attendance> findAllByUserAndCheckInTimeBetween(
            User user,
            LocalDateTime from,
            LocalDateTime to
    );


}
