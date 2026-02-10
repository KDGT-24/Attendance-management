package com.example.attendance_management_app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fix_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FixRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Attendance attendance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FixEventType eventType;

    private LocalDateTime newTime;
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FixRequestStatus status = FixRequestStatus.PENDING;
}


