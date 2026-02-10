-- 既存削除
DROP TABLE IF EXISTS paid_vacation_request CASCADE;
DROP TABLE IF EXISTS document_request CASCADE;
DROP TABLE IF EXISTS skills_salary CASCADE;
DROP TABLE IF EXISTS jobs_salary CASCADE;
DROP TABLE IF EXISTS fix_request CASCADE;
DROP TABLE IF EXISTS attendance CASCADE;
DROP TABLE IF EXISTS users CASCADE;
-- =========================
-- users
-- =========================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    jobs JSON,
    skills JSON
) ENGINE=InnoDB;

-- =========================
-- attendance
-- =========================
CREATE TABLE attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    check_in_time DATETIME,
    check_out_time DATETIME,
    break_start_time DATETIME,
    break_end_time DATETIME,
    location VARCHAR(255),
    status VARCHAR(100),
    CONSTRAINT fk_attendance_user
        FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

-- =========================
-- document_applications
-- =========================
CREATE TABLE document_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    applicant_id BIGINT NOT NULL,
    document_type VARCHAR(255),
    purpose VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    approved_by BIGINT,
    applied_at DATETIME,
    approved_at DATETIME,
    CONSTRAINT fk_document_applicant
        FOREIGN KEY (applicant_id) REFERENCES users(id),
    CONSTRAINT fk_document_approved_by
        FOREIGN KEY (approved_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- =========================
-- paid_leave_applications
-- =========================
CREATE TABLE paid_leave_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    applicant_id BIGINT NOT NULL,
    start_date DATE,
    end_date DATE,
    reason VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    approved_by BIGINT,
    applied_at DATETIME,
    approved_at DATETIME,
    CONSTRAINT fk_paid_leave_applicant
        FOREIGN KEY (applicant_id) REFERENCES users(id),
    CONSTRAINT fk_paid_leave_approved_by
        FOREIGN KEY (approved_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- =========================
-- fix_requests
-- =========================
CREATE TABLE fix_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    attendance_id BIGINT NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    new_time DATETIME,
    reason VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_fix_user
        FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_fix_attendance
        FOREIGN KEY (attendance_id) REFERENCES attendance(id)
) ENGINE=InnoDB;

-- =========================
-- jobs_salary
-- =========================
CREATE TABLE jobs_salary (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    job_salary INT NOT NULL
) ENGINE=InnoDB;

-- =========================
-- skills_salary
-- =========================
CREATE TABLE skills_salary (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    skill_salary INT NOT NULL
) ENGINE=InnoDB;
