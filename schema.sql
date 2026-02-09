-- 既存削除
DROP TABLE IF EXISTS paid_vacation_request CASCADE;
DROP TABLE IF EXISTS document_request CASCADE;
DROP TABLE IF EXISTS skills_salary CASCADE;
DROP TABLE IF EXISTS jobs_salary CASCADE;
DROP TABLE IF EXISTS fix_request CASCADE;
DROP TABLE IF EXISTS attendance CASCADE;
DROP TABLE IF EXISTS users CASCADE;

------------------------------------------------
-- users
------------------------------------------------
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    employee_number INT NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    email VARCHAR(255) UNIQUE,
    slack_webhook VARCHAR(255),
    enabled BOOLEAN DEFAULT TRUE,
    status INT NOT NULL,           -- 0勤務中/1退勤済み/2休憩中
    jobs INT[],                    -- 職務ID配列
    skills INT[]                   -- 職能ID配列
);

------------------------------------------------
-- attendance（イベント方式）
------------------------------------------------
CREATE TABLE attendance_events (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),
    event_type INT NOT NULL,
    event_time TIMESTAMP NOT NULL
);


CREATE TABLE attendance (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),

    check_in_time TIMESTAMP,
    check_out_time TIMESTAMP,
    break_start_time TIMESTAMP,
    break_end_time TIMESTAMP,

    location VARCHAR(255),
    status VARCHAR(20)
);


------------------------------------------------
-- fix_request（new_time のみ）
------------------------------------------------
CREATE TABLE fix_requests (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),
    attendance_id INT NOT NULL REFERENCES attendance(id),
    event_type INT NOT NULL,
    new_time TIMESTAMP NOT NULL,
    reason TEXT NOT NULL,
    status INT NOT NULL DEFAULT 0
);




------------------------------------------------
-- document_request
------------------------------------------------
CREATE TABLE document_request (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    type INT NOT NULL,             -- 0経費/1出張費/2その他
    detail TEXT,
    status INT DEFAULT 0,          -- 0未確認/1確認済/2対応済
    FOREIGN KEY (user_id) REFERENCES users(id)
);

------------------------------------------------
-- paid_vacation_request
------------------------------------------------
CREATE TABLE paid_vacation_request (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    date DATE,
    reason TEXT,
    status INT DEFAULT 0,          -- 0未承認/1承認/2却下
    FOREIGN KEY (user_id) REFERENCES users(id)
);

------------------------------------------------
-- jobs_salary
------------------------------------------------
CREATE TABLE jobs_salary (
    id SERIAL PRIMARY KEY,
    job_salary INT NOT NULL,
    name VARCHAR(50) NOT NULL
);

------------------------------------------------
-- skills_salary
------------------------------------------------
CREATE TABLE skills_salary (
    id SERIAL PRIMARY KEY,
    skill_salary INT NOT NULL,
    name VARCHAR(50) NOT NULL
);