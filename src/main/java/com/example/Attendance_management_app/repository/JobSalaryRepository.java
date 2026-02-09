package com.example.Attendance_management_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Attendance_management_app.entity.JobSalary;

@Repository
public interface JobSalaryRepository extends JpaRepository<JobSalary, Long> {

    // 同一名称チェック（UC13）
    boolean existsByName(String name);
}
