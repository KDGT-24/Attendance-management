package com.example.attendance_management_app.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "paid_leave_applications")
@Getter
@Setter
public class PaidLeaveApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 申請者
	@ManyToOne
	@JoinColumn(name = "applicant_id", nullable = false)
	private User applicant;

	private LocalDate startDate;
	private LocalDate endDate;

	private String reason;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ApplicationStatus status;

	// 承認者
	@ManyToOne
	@JoinColumn(name = "approved_by")
	private User approvedBy;

	private LocalDateTime appliedAt;
	private LocalDateTime approvedAt;
}
