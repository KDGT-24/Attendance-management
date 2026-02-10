package com.example.attendance_management_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance_management_app.entity.FixRequest;
import com.example.attendance_management_app.entity.FixRequestStatus;
import com.example.attendance_management_app.entity.User;

@Repository
// このインタフェースがリポジトリ層のコンポーネントであることを示すアノテーション。
// Spring が自動的にインスタンス化して DI 対象にしてくれる。
public interface FixRequestRepository extends JpaRepository<FixRequest, Long> {
// FixRequest エンティティのデータアクセスを担当するインタフェース。
// JpaRepository を継承することで基本的な CRUD 操作が全て利用可能になる。
// 第 1 引数はエンティティの型、第 2 引数は主キーの型。
List<FixRequest> findByUser(User user);
// 指定した User に紐づく修正依頼を全て取得するメソッド。
List<FixRequest> findByStatus(FixRequestStatus status);
// 指定したステータスの修正依頼を全て取得するメソッド。
// 例: pending, approved, rejected など。
}