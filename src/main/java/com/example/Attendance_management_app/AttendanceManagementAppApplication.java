package com.example.attendance_management_app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.attendance_management_app.entity.User;
import com.example.attendance_management_app.repository.UserRepository;

@SpringBootApplication
public class AttendanceManagementAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceManagementAppApplication.class, args);
    }

    @Bean
    CommandLineRunner initUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            // ===== ADMIN ユーザー =====
            if (userRepository.findByName("admin").isEmpty()) {
                User admin = new User();
                admin.setName("admin");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setRole("ROLE_ADMIN");
                admin.setEmail("admin@example.com");
                userRepository.save(admin);

                System.out.println("✅ 初期ADMINユーザー作成: admin / password");
            }

            // ===== EMPLOYEE ユーザー =====
            if (userRepository.findByName("employee").isEmpty()) {
                User employee = new User();
                employee.setName("employee");
                employee.setPassword(passwordEncoder.encode("password"));
                employee.setRole("ROLE_EMPLOYEE");
                employee.setEmail("employee@example.com");
                userRepository.save(employee);

                System.out.println("✅ 初期EMPLOYEEユーザー作成: employee / password");
            }
        };
    }
}
