package com.example.Attendance_management_app.service;
@Service
public class PaidLeaveApplicationService {

    private final PaidLeaveApplicationRepository repository;

    public PaidLeaveApplicationService(PaidLeaveApplicationRepository repository) {
        this.repository = repository;
    }

    public void apply(PaidLeaveApplication application, Employee employee) {
        application.setEmployee(employee);
        application.setStatus(ApplicationStatus.PENDING);
        application.setAppliedAt(LocalDateTime.now());
        repository.save(application);
    }

    public void approve(Long id, Employee approver) {
        PaidLeaveApplication app = repository.findById(id).orElseThrow();
        app.setStatus(ApplicationStatus.APPROVED);
        app.setApprovedBy(approver);
        app.setApprovedAt(LocalDateTime.now());
        repository.save(app);
    }

    public void reject(Long id, Employee approver) {
        PaidLeaveApplication app = repository.findById(id).orElseThrow();
        app.setStatus(ApplicationStatus.REJECTED);
        app.setApprovedBy(approver);
        app.setApprovedAt(LocalDateTime.now());
        repository.save(app);
    }
}
