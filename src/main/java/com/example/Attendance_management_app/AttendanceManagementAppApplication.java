package com.example.Attendance_management_app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.Attendance_management_app.entity.User;
import com.example.Attendance_management_app.repository.UserRepository;

@SpringBootApplication
public class AttendanceManagementAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceManagementAppApplication.class, args);
	}

	@Bean
	CommandLineRunner initUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.findByName("user").isPresent()) {
				return;
			}

			User user = new User();
			user.setName("user");
			user.setPassword(passwordEncoder.encode("password"));
			user.setRole("ADMIN");
			userRepository.save(user);

			System.out.println("✅ 初期ユーザー作成: user / password");
		};
	}
}
