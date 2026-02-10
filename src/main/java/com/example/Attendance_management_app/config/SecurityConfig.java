package com.example.attendance_management_app.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.attendance_management_app.repository.UserRepository;

@Configuration
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService userDetailsService(UserRepository userRepository) {
    return username -> {
			var u = userRepository.findByName(username)
							.orElseThrow(() -> new RuntimeException("User not found: " + username));

			GrantedAuthority auth =
							new SimpleGrantedAuthority(u.getRole());

			return org.springframework.security.core.userdetails.User.builder()
							.username(u.getName())
							.password(u.getPassword())
							.authorities(List.of(auth))
							.build();
    };
}


	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login", "/error", "/css/**", "/js/**", "/images/**").permitAll()
						.requestMatchers("/continue").authenticated()

						.requestMatchers("/admin/**").hasRole("ADMIN")

						// ★開発中の事故を減らす：EMPLOYEE か ADMIN なら employee 側に入れる
						.requestMatchers("/employee/**").hasAnyRole("EMPLOYEE", "ADMIN")

						// パスワード変更はログインしてればOK
						.requestMatchers("/password/**").authenticated()

						.anyRequest().authenticated())
				.formLogin(login -> login
						.loginPage("/login")
						.defaultSuccessUrl("/continue", true)
						.permitAll())
				.logout(logout -> logout.logoutSuccessUrl("/login?logout"));

		return http.build();
	}
}
