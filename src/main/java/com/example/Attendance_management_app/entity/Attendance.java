package com.example.attendance_management_app.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -------------------------------------
    // ユーザー情報
    // -------------------------------------
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // -------------------------------------
    // 勤怠イベントごとの時刻
    // -------------------------------------
    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Column(name = "break_start_time")
    private LocalDateTime breakStartTime;

    @Column(name = "break_end_time")
    private LocalDateTime breakEndTime;

    // -------------------------------------
    // ロケーションやステータスなど
    // -------------------------------------
    private String location;
    private String status;

    // -------------------------------------
    // 補助メソッド（便利関数）
    // -------------------------------------

    /** 当日の出勤時刻を取得 */
    public LocalDateTime getCheckIn() {
        return checkInTime;
    }

    /** 当日の退勤時刻を取得 */
    public LocalDateTime getCheckOut() {
        return checkOutTime;
    }

    /** 当日の休憩開始時刻を取得 */
    public LocalDateTime getBreakStart() {
        return breakStartTime;
    }

    /** 当日の休憩終了時刻を取得 */
    public LocalDateTime getBreakEnd() {
        return breakEndTime;
    }

    /** 出勤日を取得 */
    public LocalDate getRecordDate() {
        if (checkInTime != null) {
            return checkInTime.toLocalDate();
        } else if (checkOutTime != null) {
            return checkOutTime.toLocalDate();
        } else if (breakStartTime != null) {
            return breakStartTime.toLocalDate();
        } else if (breakEndTime != null) {
            return breakEndTime.toLocalDate();
        } else {
            return null; // 日付不明
        }
    }

}
