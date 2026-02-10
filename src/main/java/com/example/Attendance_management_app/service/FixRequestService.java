package com.example.attendance_management_app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance_management_app.entity.Attendance;
import com.example.attendance_management_app.entity.FixEventType;
import com.example.attendance_management_app.entity.FixRequest;
import com.example.attendance_management_app.entity.FixRequestStatus;
import com.example.attendance_management_app.entity.User;
import com.example.attendance_management_app.repository.AttendanceRepository;
import com.example.attendance_management_app.repository.FixRequestRepository;

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
	public FixRequest createFixRequest(
					User user,
					Long attendanceId,
					FixEventType eventType,
					LocalDateTime newTime,
					String reason
	) {
			FixRequest fr = new FixRequest();
			fr.setUser(user);
			fr.setAttendance(attendanceRepository.getReferenceById(attendanceId));
			fr.setEventType(eventType);
			fr.setNewTime(newTime);
			fr.setReason(reason);
			fr.setStatus(FixRequestStatus.PENDING); // ★ここ
			return fixRequestRepository.save(fr);
	}

	public List<FixRequest> getAllPendingFixRequests() {
		//ステータスが「pending」の修正依頼を全て取得するメソッド。
		return fixRequestRepository.findByStatus(FixRequestStatus.PENDING);
	}
	
	@Transactional
	public void approveFixRequest(Long requestId) {

			FixRequest fr = fixRequestRepository.findById(requestId)
							.orElseThrow(() -> new IllegalArgumentException("FixRequest not found"));

			if (fr.getStatus() != FixRequestStatus.PENDING) {
					throw new IllegalStateException("FixRequest is not pending");
			}

			Attendance attendance = fr.getAttendance();
			if (attendance == null) {
					throw new IllegalStateException("Attendance record not found");
			}

			switch (fr.getEventType()) {
					case CHECK_IN     -> attendance.setCheckInTime(fr.getNewTime());
					case CHECK_OUT    -> attendance.setCheckOutTime(fr.getNewTime());
					case BREAK_START  -> attendance.setBreakStartTime(fr.getNewTime());
					case BREAK_END    -> attendance.setBreakEndTime(fr.getNewTime());
			}

			attendanceRepository.save(attendance);

			fr.setStatus(FixRequestStatus.APPROVED);
			fixRequestRepository.save(fr);
	}

	@Transactional
	public void rejectFixRequest(Long requestId) {

			FixRequest fr = fixRequestRepository.findById(requestId)
							.orElseThrow(() -> new IllegalArgumentException("FixRequest not found"));

			if (fr.getStatus() != FixRequestStatus.PENDING) {
					throw new IllegalStateException("FixRequest is not pending");
			}

			fr.setStatus(FixRequestStatus.REJECTED);
			fixRequestRepository.save(fr);
	}

	public Optional<FixRequest> getFixRequestById(Long requestId) {
		//修正依頼 ID で修正依頼を取得するメソッド。
		return fixRequestRepository.findById(requestId);
	}
}