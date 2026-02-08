package com.example.Attendance_management_app.entity;
@Entity
@Table(name = "document_applications")
public class DocumentApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    // 書類の種類（在職証明書、源泉徴収票など）
    private String documentType;

    private String purpose;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;

    private LocalDateTime appliedAt;
    private LocalDateTime approvedAt;

    // getter / setter
}
