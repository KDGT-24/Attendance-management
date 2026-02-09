package com.example.Attendance_management_app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Attendance_management_app.entity.SkillSalary;
import com.example.Attendance_management_app.repository.SkillSalaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SkillSalaryService {

    private final SkillSalaryRepository skillSalaryRepository;

    // 一覧取得
    public List<SkillSalary> findAll() {
        return skillSalaryRepository.findAll();
    }

    // 追加
    public SkillSalary create(String name, int salary) {
        if (skillSalaryRepository.existsByName(name)) {
            throw new IllegalArgumentException("同一名称の職能給が既に存在します");
        }
        if (salary <= 0) {
            throw new IllegalArgumentException("職能給は正の値で入力してください");
        }
        return skillSalaryRepository.save(
            new SkillSalary(null, name, salary)
        );
    }

    // 更新
    public SkillSalary update(Long id, String name, int salary) {
        SkillSalary skill = skillSalaryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("職能給が存在しません"));

        if (!skill.getName().equals(name)
                && skillSalaryRepository.existsByName(name)) {
            throw new IllegalArgumentException("同一名称の職能給が既に存在します");
        }

        skill.setName(name);
        skill.setSkillSalary(salary);
        return skill;
    }

    // 削除
    public void delete(Long id) {
        if (!skillSalaryRepository.existsById(id)) {
            throw new IllegalArgumentException("職能給が存在しません");
        }
        skillSalaryRepository.deleteById(id);
    }
}
