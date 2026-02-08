package com.example.Attendance_management_app.entity;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "paid_leave_applications")
public class PaidLeaveApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 申請者
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	private LocalDate startDate;
	private LocalDate endDate;

	private String reason;

	@Enumerated(EnumType.STRING)
	private ApplicationStatus status;

	// 承認者
	@ManyToOne
	@JoinColumn(name = "approved_by")
	private Employee approvedBy;

	private LocalDateTime appliedAt;
	private LocalDateTime approvedAt;

	// getter / setter
}
