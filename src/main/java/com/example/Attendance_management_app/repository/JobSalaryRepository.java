package com.example.attendance_management_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance_management_app.entity.JobSalary;

@Repository
public interface JobSalaryRepository extends JpaRepository<JobSalary, Long> {

    // 同一名称チェック（UC13）
    boolean existsByName(String name);
    List<JobSalary> findByIdIn(List<Long> ids);
}
