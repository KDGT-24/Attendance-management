package com.example.Attendance_management_app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Attendance_management_app.entity.Attendance;
import com.example.Attendance_management_app.entity.FixRequest;
import com.example.Attendance_management_app.entity.User;
import com.example.Attendance_management_app.repository.AttendanceRepository;
import com.example.Attendance_management_app.repository.FixRequestRepository;

@Service
// このクラスが Spring のサービス層のコンポーネントであることを示すアノテーション。
// ビジネスロジックを担当するクラスに付与する。
public class FixRequestService {
	// 勤怠修正依頼に関するビジネスロジックを提供するサービスクラス。
	private final FixRequestRepository fixRequestRepository;
	// 修正依頼データの永続化を担当するリポジトリを DI で注入するためのフィールド。
	private final AttendanceRepository attendanceRepository;

	// 勤怠データの永続化を担当するリポジトリを DI で注入するためのフィールド。
	public FixRequestService(FixRequestRepository fixRequestRepository, AttendanceRepository attendanceRepository) {
		// コンストラクタインジェクションでリポジトリを注入するコンストラクタ。
		this.fixRequestRepository = fixRequestRepository;
		this.attendanceRepository = attendanceRepository;
	}

	@Transactional
	// メソッドをトランザクション境界で実行することを指定するアノテーション。
	// DB への更新をまとめて行いたいときに使う。
	public FixRequest createFixRequest(
	        User user,
	        Long attendanceId,
	        int eventType,
	        LocalDateTime newTime,
	        String reason
	) {
	    FixRequest fr = new FixRequest();
	    fr.setUser(user);
	    fr.setAttendance(attendanceRepository.getReferenceById(attendanceId));
	    fr.setEventType(eventType);
	    fr.setNewTime(newTime);
	    fr.setReason(reason);
	    fr.setStatus(0);
	    return fixRequestRepository.save(fr);
	}




	public List<FixRequest> getAllPendingFixRequests() {
		//ステータスが「pending」の修正依頼を全て取得するメソッド。
		return fixRequestRepository.findByStatus(0);
	}
	
	private int transitionFromCode(String type) {
	    return switch (type.toLowerCase()) {
	        case "checkin" -> 0;
	        case "checkout" -> 1;
	        case "breakstart" -> 2;
	        case "breakend" -> 3;
	        default -> throw new IllegalArgumentException("Unknown transition type: " + type);
	    };
	}

	private int transitionToCode(String type) {
	    return switch (type.toLowerCase()) {
	        case "checkin" -> 0;
	        case "checkout" -> 1;
	        case "breakstart" -> 2;
	        case "breakend" -> 3;
	        default -> throw new IllegalArgumentException("Unknown transition type: " + type);
	    };
	}

	@Transactional
	public void approveFixRequest(Long requestId) {

	    // 修正依頼を取得
	    FixRequest fr = fixRequestRepository.findById(requestId)
	            .orElseThrow(() -> new RuntimeException("FixRequest not found"));

	    // 承認済み・却下済みは処理不可
	    if (fr.getStatus() != 0) {
	        throw new IllegalStateException("FixRequest is not pending");
	    }

	    // 修正対象の Attendance レコード
	    Attendance attendance = fr.getAttendance();

	    if (attendance == null) {
	        throw new RuntimeException("Attendance record not found for this FixRequest");
	    }

	    // イベントタイプに応じてフィールドを更新
	    switch (fr.getEventType()) {
	        case 0 -> attendance.setCheckInTime(fr.getNewTime());    // 出勤
	        case 1 -> attendance.setCheckOutTime(fr.getNewTime());   // 退勤
	        case 2 -> attendance.setBreakStartTime(fr.getNewTime()); // 休憩開始
	        case 3 -> attendance.setBreakEndTime(fr.getNewTime());   // 休憩終了
	        default -> throw new IllegalArgumentException("Unknown event type: " + fr.getEventType());
	    }

	    // Attendance を保存（修正を反映）
	    attendanceRepository.save(attendance);

	    // FixRequest を承認済みに更新
	    fr.setStatus(1);  // 0: pending, 1: approved, 2: rejected
	    fixRequestRepository.save(fr);
	}

	@Transactional
	//メソッドをトランザクション境界で実行することを指定する。
	public void rejectFixRequest(Long requestId) {
		//修正依頼を却下するメソッド。
		//修正依頼 ID を引数に受け取り、ステータスを「rejected」にする。
		FixRequest fixRequest = fixRequestRepository.findById(requestId)
				.orElseThrow(() -> new IllegalArgumentException("Fix request not found"));
		//指定 ID の修正依頼を取得する。
		//存在しない場合は例外を投げる。
		if (!"pending".equals(fixRequest.getStatus())) {
			//修正依頼が pending 状態でない場合
			throw new IllegalStateException("Fix request is not pending.");
			//却下できない旨の例外を投げる。
		}
		fixRequest.setStatus(2);
		//修正依頼のステータスを「rejected」に更新する。
		fixRequestRepository.save(fixRequest);
		//ステータス変更済みの修正依頼を DB に保存する。
	}

	public Optional<FixRequest> getFixRequestById(Long requestId) {
		//修正依頼 ID で修正依頼を取得するメソッド。
		return fixRequestRepository.findById(requestId);
	}
}