package com.example.attendance_management_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance_management_app.entity.SkillSalary;

@Repository
public interface SkillSalaryRepository extends JpaRepository<SkillSalary, Long> {

    // 同一名称チェック（UC13）
    boolean existsByName(String name);
    List<SkillSalary> findByIdIn(List<Long> ids);
}
