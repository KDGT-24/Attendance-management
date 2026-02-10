// package com.example.attendance_management_app.config;

// import java.time.LocalDateTime;
// import java.time.YearMonth;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Profile;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Component;

// import com.example.attendance_management_app.entity.Attendance;
// import com.example.attendance_management_app.entity.JobSalary;
// import com.example.attendance_management_app.entity.SkillSalary;
// import com.example.attendance_management_app.entity.User;
// import com.example.attendance_management_app.repository.AttendanceRepository;
// import com.example.attendance_management_app.repository.JobSalaryRepository;
// import com.example.attendance_management_app.repository.SkillSalaryRepository;
// import com.example.attendance_management_app.repository.UserRepository;

// import lombok.RequiredArgsConstructor;

// @Component
// @Profile("dev") // ★ 開発環境のみ
// @RequiredArgsConstructor
// public class DataInitializer implements CommandLineRunner {

//     private final UserRepository userRepository;
//     private final AttendanceRepository attendanceRepository;
//     private final JobSalaryRepository jobSalaryRepository;
//     private final SkillSalaryRepository skillSalaryRepository;
//     private final PasswordEncoder passwordEncoder;
     
//     @Override
//     public void run(String... args) {

//         // ----------------------
//         // 職務給
//         // ----------------------
//         if (jobSalaryRepository.count() == 0) {
//             jobSalaryRepository.save(new JobSalary(null, "一般社員", 0));
//             jobSalaryRepository.save(new JobSalary(null, "主任", 200));
//             jobSalaryRepository.save(new JobSalary(null, "管理職", 500));
//         }

//         // ----------------------
//         // 職能給
//         // ----------------------
//         if (skillSalaryRepository.count() == 0) {
//             skillSalaryRepository.save(new SkillSalary(null, "Java", 200));
//             skillSalaryRepository.save(new SkillSalary(null, "Spring", 300));
//             skillSalaryRepository.save(new SkillSalary(null, "DB", 150));
//         }

//         // ----------------------
//         // ユーザー（既存2名想定）
//         // ----------------------
//         User user1 = userRepository.findByName("employee")
//         .orElseGet(() -> {
//             User u = new User();
//             u.setName("employee");
//             u.setPassword(passwordEncoder.encode("password"));
//             u.setRole("ROLE_EMPLOYEE");
//             return userRepository.save(u);
//         });

//         User user2 = userRepository.findByName("admin")
//         .orElseGet(() -> {
//             User u = new User();
//             u.setName("admin");
//             u.setPassword(passwordEncoder.encode("password"));
//             u.setRole("ROLE_ADMIN");
//             return userRepository.save(u);
//         });

//         // ----------------------
//         // 勤怠（今月分）
//         // ----------------------
//         YearMonth ym = YearMonth.now();

//         if (attendanceRepository.count() == 0) {

//             for (int day = 1; day <= 5; day++) {

//                 LocalDateTime in  = ym.atDay(day).atTime(9, 0);
//                 LocalDateTime out = ym.atDay(day).atTime(18, 0);

//                 attendanceRepository.save(
//                     Attendance.builder()
//                         .user(user1)
//                         .checkInTime(in)
//                         .checkOutTime(out)
//                         .breakStartTime(in.plusHours(3))
//                         .breakEndTime(in.plusHours(4))
//                         .build()
//                 );

//                 attendanceRepository.save(
//                     Attendance.builder()
//                         .user(user2)
//                         .checkInTime(in.plusMinutes(30))
//                         .checkOutTime(out.plusMinutes(30))
//                         .build()
//                 );
//             }
//         }

//         System.out.println("==== テストデータ投入完了 ====");
//     }
// }
