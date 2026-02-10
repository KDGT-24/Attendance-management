package com.example.attendance_management_app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.attendance_management_app.entity.ApplicationStatus;
import com.example.attendance_management_app.entity.PaidLeaveApplication;
import com.example.attendance_management_app.entity.User;
import com.example.attendance_management_app.repository.PaidLeaveApplicationRepository;
import com.example.attendance_management_app.repository.UserRepository;

@Service
public class PaidLeaveApplicationService {

    private final PaidLeaveApplicationRepository repository;
    private final UserRepository userRepository;

    public PaidLeaveApplicationService(
            PaidLeaveApplicationRepository repository,
            UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // =========================
    // 社員：申請
    // =========================
    public void apply(PaidLeaveApplication application, String username) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        application.setApplicant(user);
        application.setStatus(ApplicationStatus.PENDING);
        application.setAppliedAt(LocalDateTime.now());

        repository.save(application);
    }

    // =========================
    // 社員：自分の申請一覧
    // =========================
    public List<PaidLeaveApplication> findByApplicant(String username) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return repository.findByApplicantOrderByAppliedAtDesc(user);
    }

    // =========================
    // 管理者：全件取得
    // =========================
    public List<PaidLeaveApplication> getAll() {
        return repository.findAllByOrderByAppliedAtDesc();
    }

    // =========================
    // 管理者：承認
    // =========================
    public void approve(Long id, String approverUsername) {
        User approver = userRepository.findByName(approverUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PaidLeaveApplication app = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus(ApplicationStatus.APPROVED);
        app.setApprovedBy(approver);
        app.setApprovedAt(LocalDateTime.now());

        repository.save(app);
    }

    // =========================
    // 管理者：却下
    // =========================
    public void reject(Long id, String approverUsername) {
        User approver = userRepository.findByName(approverUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PaidLeaveApplication app = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus(ApplicationStatus.REJECTED);
        app.setApprovedBy(approver);
        app.setApprovedAt(LocalDateTime.now());

        repository.save(app);
    }
}
