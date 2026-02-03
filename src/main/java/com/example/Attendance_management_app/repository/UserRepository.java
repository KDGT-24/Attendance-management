package com.example.Attendance_management_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Attendance_management_app.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByName(String name);
}
