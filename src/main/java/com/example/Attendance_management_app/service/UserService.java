package com.example.attendance_management_app.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.attendance_management_app.entity.User;
import com.example.attendance_management_app.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// 成功: null / 失敗: エラーメッセージ
	public String changePasswordByUsername(String username, String currentPassword, String newPassword) {

		Optional<User> opt = userRepository.findByName(username);
		if (opt.isEmpty()) {
			return "ユーザーが見つかりません";
		}

		User user = opt.get();

		// 現在PWチェック
		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			return "現在のパスワードが違います";
		}

		// 更新
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);

		return null;
	}
}
