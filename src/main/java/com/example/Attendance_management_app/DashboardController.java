package com.example.Attendance_management_app;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.Attendance_management_app.form.PasswordChangeForm;
import com.example.Attendance_management_app.service.UserService;

@Controller
public class DashboardController {

	private final UserService userService;

	public DashboardController(UserService userService) {
		this.userService = userService;
	}

	// ログイン後の分岐先
	@GetMapping("/continue")
	public String continueAfterLogin(Authentication authentication) {

		if (authentication.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
			return "redirect:/admin/dashboard";
		}

		return "redirect:/employee/dashboard";
	}

	@GetMapping("/admin/dashboard")
	public String adminDashboard() {
		return "admin_dashboard";
	}

	@GetMapping("/employee/dashboard")
	public String employeeDashboard() {
		return "employee_dashboard";
	}

	// パスワード変更画面（/password/change を正にする。ついでに /setting-password も受ける）
	@GetMapping({ "/password/change", "/setting-password" })
	public String showPasswordChange(Model model) {
		model.addAttribute("form", new PasswordChangeForm());
		return "password_change";
	}

	// パスワード変更処理
	@PostMapping({ "/password/change", "/setting-password" })
	public String changePassword(
			@ModelAttribute("form") PasswordChangeForm form,
			Authentication authentication,
			Model model) {

		if (!form.getNewPassword().equals(form.getNewPasswordConfirm())) {
			model.addAttribute("error", "新しいパスワードが一致しません");
			return "password_change";
		}

		String username = authentication.getName();

		String errorMsg = userService.changePasswordByUsername(
				username,
				form.getCurrentPassword(),
				form.getNewPassword());

		if (errorMsg != null) {
			model.addAttribute("error", errorMsg);
			return "password_change";
		}

		// 成功後は /password/change に揃える
		return "redirect:/password/change?success";
	}
}
