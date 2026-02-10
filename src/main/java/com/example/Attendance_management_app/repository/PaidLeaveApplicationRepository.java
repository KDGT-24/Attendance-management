package com.example.attendance_management_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendance_management_app.entity.PaidLeaveApplication;
import com.example.attendance_management_app.entity.User;

public interface PaidLeaveApplicationRepository
	extends JpaRepository<PaidLeaveApplication, Long> {
		List<PaidLeaveApplication> findByApplicantOrderByAppliedAtDesc(User applicant);

		List<PaidLeaveApplication> findAllByOrderByAppliedAtDesc();
}
