package com.example.attendance_management_app.service;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.attendance_management_app.dto.AdminMonthlySalaryRow;
import com.example.attendance_management_app.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminSalaryService {

    private final UserRepository userRepository;
    private final MonthlySalaryService monthlySalaryService;

    // 管理者：全社員 月次給与一覧
    public List<AdminMonthlySalaryRow> calculateAll(
            YearMonth yearMonth
    ) {
        return userRepository.findAll().stream()
                .map(user -> {
                    var result =
                            monthlySalaryService.calculate(user, yearMonth);

                    return new AdminMonthlySalaryRow(
                            user.getId(),
                            user.getName(),
                            result.getTotalMinutes(),
                            result.getTotalSalary(),
                            yearMonth
                    );
                })
                .toList();
    }
}
