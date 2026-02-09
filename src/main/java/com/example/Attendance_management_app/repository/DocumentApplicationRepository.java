package com.example.Attendance_management_app.repository;
public interface DocumentApplicationRepository
        extends JpaRepository<DocumentApplication, Long> {

    List<DocumentApplication> findByEmployee(Employee employee);
}
