package com.example.attendance_management_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendance_management_app.entity.DocumentApplication;
import com.example.attendance_management_app.entity.User;

public interface DocumentApplicationRepository
        extends JpaRepository<DocumentApplication, Long> {

    List<DocumentApplication> findByApplicant(User applicant);
}
