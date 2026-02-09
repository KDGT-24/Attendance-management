-- default（時給マスタ設定用）
INSERT INTO users (
    employee_number, username, password, role,
    email, enabled, status
) VALUES (
    0,
    'default',
    '$2a$10$7QyZzZzZzZzZzZzZzZzZzeJXQJqkY1k0ZQvG7e2sQb1y5zHq',
    'ADMIN',
    NULL,
    false,
    1
);

-- 管理者
INSERT INTO users (
    employee_number, username, password, role,
    email, enabled, status
) VALUES (
    1,
    'admin',
    '$2a$10$7QyZzZzZzZzZzZzZzZzZzeJXQJqkY1k0ZQvG7e2sQb1y5zHq',
    'ADMIN',
    'admin@example.com',
    true,
    1
);

-- 一般社員
INSERT INTO users (
    employee_number, username, password, role,
    email, enabled, status
) VALUES (
    1001,
    'user01',
    '$2a$10$7QyZzZzZzZzZzZzZzZzZzeJXQJqkY1k0ZQvG7e2sQb1y5zHq',
    'USER',
    'user01@example.com',
    true,
    1
);
INSERT INTO jobs_salary (name, job_salary) VALUES
('一般職', 1200),
('主任',   1400),
('管理職', 1600);
INSERT INTO skills_salary (name, skill_salary) VALUES
('フォークリフト', 100),
('危険物取扱者',   150),
('英語対応',       200);
-- 管理者：管理職 + 英語対応
UPDATE users
SET jobs = ARRAY[3],
    skills = ARRAY[3]
WHERE username = 'admin';

-- 一般社員：一般職 + フォークリフト・危険物
UPDATE users
SET jobs = ARRAY[1],
    skills = ARRAY[1,2]
WHERE username = 'user01';
-- user01 の通常勤務（9:00-18:00、12:00-13:00 休憩）
INSERT INTO attendance (
    user_id,
    check_in_time,
    check_out_time,
    break_start_time,
    break_end_time,
    location,
    status
) VALUES (
    (SELECT id FROM users WHERE username = 'user01'),
    '2026-02-03 09:00:00',
    '2026-02-03 18:00:00',
    '2026-02-03 12:00:00',
    '2026-02-03 13:00:00',
    '本社',
    'DONE'
);

-- 残業＋深夜（18:00-23:30）
INSERT INTO attendance (
    user_id,
    check_in_time,
    check_out_time,
    break_start_time,
    break_end_time,
    location,
    status
) VALUES (
    (SELECT id FROM users WHERE username = 'user01'),
    '2026-02-05 18:00:00',
    '2026-02-05 23:30:00',
    NULL,
    NULL,
    '本社',
    'DONE'
);
INSERT INTO fix_requests (
    user_id,
    attendance_id,
    event_type,
    new_time,
    reason,
    status
) VALUES (
    (SELECT id FROM users WHERE username = 'user01'),
    (SELECT id FROM attendance ORDER BY id DESC LIMIT 1),
    1, -- 例：退勤修正
    '2026-02-05 23:45:00',
    '業務都合で15分延長',
    0
);
INSERT INTO document_request (
    user_id,
    type,
    detail,
    status
) VALUES (
    (SELECT id FROM users WHERE username = 'user01'),
    0,
    '2月分交通費精算',
    0
);
INSERT INTO paid_vacation_request (
    user_id,
    date,
    reason,
    status
) VALUES (
    (SELECT id FROM users WHERE username = 'user01'),
    '2026-02-10',
    '私用',
    0
);
