package com.example.attendance_management_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendance_management_app.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByName(String name);

    List<User> findByNameContainingIgnoreCase(String keyword);
}
