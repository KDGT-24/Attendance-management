package com.example.Attendance_management_app.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Attendance_management_app.entity.Attendance;
import com.example.Attendance_management_app.entity.User;
import com.example.Attendance_management_app.repository.UserRepository;
import com.example.Attendance_management_app.service.AttendanceService;
import com.example.Attendance_management_app.service.FixRequestService;

@Controller
// このクラスが Spring MVC のコントローラであることを示すアノテーション。
@RequestMapping("/attendance")
// このコントローラが /attendance 配下の URL を扱うことを指定する。
public class AttendanceController {
	// 勤怠打刻や勤怠履歴画面を担当するコントローラクラスの定義。
	private final AttendanceService attendanceService;
	// 勤怠関連のビジネスロジックを扱うサービスを DI で注入するためのフィールド。
	private final UserRepository userRepository;
	// ユーザーデータの永続化を担当するリポジトリを DI で注入するためのフィールド。
	private final FixRequestService fixRequestService;

	// 勤怠修正依頼のビジネスロジックを扱うサービスを DI で注入するためのフィールド。
	public AttendanceController(AttendanceService attendanceService, UserRepository userRepository,
			FixRequestService fixRequestService) {
		// コンストラクタインジェクションで DI するコンストラクタ。
		this.attendanceService = attendanceService;
		this.userRepository = userRepository;
		this.fixRequestService = fixRequestService;
	}

	@PostMapping("/punch")
	public String punch(
	        @AuthenticationPrincipal UserDetails userDetails,
	        @RequestParam("type") String type
	) {

	    User currentUser = userRepository.findByUsername(userDetails.getUsername())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    // Service 定義に合わせる
	    attendanceService.punch(currentUser, type);

	    return "redirect:/dashboard";
	}


	@GetMapping("/history")
	// HTTP GET リクエストで /attendance/history にアクセスしたときに呼び出されるメソッドを定義するアノテーション。
	public String showAttendanceHistory(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		// ログイン中のユーザーの勤怠履歴画面を表示するメソッド。
		User currentUser = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));
		// ログインユーザー名で DB から User エンティティを取得する。
		model.addAttribute("attendanceRecords", attendanceService.getUserAttendance(currentUser));
		//勤怠履歴をモデルに追加する。
		return "attendance_history";
		//勤怠履歴画面 (attendance_history.html) を表示する。
	}

	@GetMapping("/history/{id}/request-fix")
	//HTTP GET リクエストで /attendance/history/{id}/request-fix にアクセスしたときに呼び出されるメソッド。
	public String showFixRequestForm(@PathVariable("id") Long attendanceId, Model model) {
		//勤怠修正依頼フォーム画面を表示するメソッド。
		Optional<Attendance> attendance = attendanceService.getAttendanceById(attendanceId);
		//指定 ID の勤怠レコードを取得する。
		if (attendance.isEmpty()) {
			return "redirect:/attendance/history";
			//レコードが存在しない場合は履歴画面に戻る。
		}
		model.addAttribute("attendance", attendance.get());
		//勤怠情報をモデルに追加する。
		return "fix_request_form";
		//勤怠修正依頼フォーム画面 (fix_request_form.html) を表示する。
	}

	@PostMapping("/history/{id}/request-fix")
	public String submitFixRequest(
	        @AuthenticationPrincipal UserDetails userDetails,
	        @PathVariable("id") Long attendanceId,
	        @RequestParam("eventType") int eventType,
	        @RequestParam("newTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newTime,
	        @RequestParam("reason") String reason) {

	    User currentUser = userRepository.findByUsername(userDetails.getUsername())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    fixRequestService.createFixRequest(currentUser, attendanceId, eventType, newTime, reason);

	    return "redirect:/attendance/history?success=fixRequestSubmitted";
	}

}