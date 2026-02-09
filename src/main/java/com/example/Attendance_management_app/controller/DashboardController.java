package com.example.Attendance_management_app.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.Attendance_management_app.entity.User;
import com.example.Attendance_management_app.repository.UserRepository;
import com.example.Attendance_management_app.service.AttendanceService;

@Controller
// このクラスが Spring MVC のコントローラであることを示すアノテーション。
public class DashboardController {
	// ダッシュボード画面表示を担当するコントローラクラスの定義。
	private final AttendanceService attendanceService;
	// 勤怠関連のビジネスロジックを扱うサービスを DI で注入するためのフィールド。
	private final UserRepository userRepository;

	// ユーザーデータの永続化を担当するリポジトリを DI で注入するためのフィールド。
	public DashboardController(AttendanceService attendanceService, UserRepository userRepository) {
		// コンストラクタインジェクションで DI するコンストラクタ。
		this.attendanceService = attendanceService;
		this.userRepository = userRepository;
	}

	@GetMapping("/dashboard")
	// HTTP GET リクエストで /dashboard にアクセスしたときに呼び出されるメソッドを定義するアノテーション。
	public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		// ダッシュボード画面を表示するメソッド。
		// ログインユーザー情報を取得して画面に反映する。
		// @AuthenticationPrincipal で Spring Security の認証ユーザー情報を受け取る。
		User currentUser = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));
		// ログイン中のユーザー名で DB から User エンティティを取得する。
		// 見つからない場合は例外を投げる。
		if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
			// ログインユーザーが ADMIN 権限を持つか判定する。
			model.addAttribute("allAttendanceRecords", attendanceService.getAllAttendance());
			// 全ユーザーの勤怠記録を取得してモデルに追加する。
			return "admin_dashboard";
			// 管理者用のダッシュボード画面 (admin_dashboard.html) を表示する。
		} else {
			model.addAttribute("attendanceRecords", attendanceService.getUserAttendance(currentUser));
			// 社員本人の勤怠履歴を取得してモデルに追加する。
			return "employee_dashboard";
			// 社員用のダッシュボード画面 (employee_dashboard.html) を表示する。
		}
	}
}