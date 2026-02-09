package com.example.Attendance_management_app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Attendance_management_app.entity.JobSalary;
import com.example.Attendance_management_app.repository.JobSalaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class JobSalaryService {

    private final JobSalaryRepository jobSalaryRepository;

    // 一覧取得
    public List<JobSalary> findAll() {
        return jobSalaryRepository.findAll();
    }

    // 追加
    public JobSalary create(String name, int salary) {
        if (jobSalaryRepository.existsByName(name)) {
            throw new IllegalArgumentException("同一名称の職務給が既に存在します");
        }
        if (salary <= 0) {
            throw new IllegalArgumentException("職務給は正の値で入力してください");
        }
        return jobSalaryRepository.save(
            new JobSalary(null, name, salary)
        );
    }

    // 更新
    public JobSalary update(Long id, String name, int salary) {
        JobSalary job = jobSalaryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("職務給が存在しません"));

        if (!job.getName().equals(name)
                && jobSalaryRepository.existsByName(name)) {
            throw new IllegalArgumentException("同一名称の職務給が既に存在します");
        }

        job.setName(name);
        job.setJobSalary(salary);
        return job;
    }

    // 削除
    public void delete(Long id) {
        if (!jobSalaryRepository.existsById(id)) {
            throw new IllegalArgumentException("職務給が存在しません");
        }
        jobSalaryRepository.deleteById(id);
    }
}
