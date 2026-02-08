package com.example.Attendance_management_app.repository;
public interface PaidLeaveApplicationRepository
        extends JpaRepository<PaidLeaveApplication, Long> {

    List<PaidLeaveApplication> findByEmployee(Employee employee);
}
