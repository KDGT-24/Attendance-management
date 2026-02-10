package com.example.attendance_management_app.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.attendance_management_app.entity.User;
import com.example.attendance_management_app.repository.UserRepository;
import com.example.attendance_management_app.service.JobSalaryService;
import com.example.attendance_management_app.service.SkillSalaryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;
    private final JobSalaryService jobSalaryService;
    private final SkillSalaryService skillSalaryService;

    // 一覧＋検索
    @GetMapping
    public String list(
            @RequestParam(required = false) String keyword,
            Model model) {

        List<User> users =
                (keyword == null || keyword.isBlank())
                ? userRepository.findAll()
                : userRepository.findByNameContainingIgnoreCase(keyword);

        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);

        return "admin/user_list";
    }

    // 編集画面
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {

        User user = userRepository.findById(id)
                .orElseThrow();

        model.addAttribute("user", user);
        model.addAttribute("jobSalaries", jobSalaryService.findAll());
        model.addAttribute("skillSalaries", skillSalaryService.findAll());

        return "admin/user_edit";
    }

    // 更新
    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @RequestParam(required = false) Integer[] jobs,
            @RequestParam(required = false) Integer[] skills) {

        User user = userRepository.findById(id)
                .orElseThrow();

        user.setJobs(jobs);
        user.setSkills(skills);

        userRepository.save(user);

        return "redirect:/admin/users";
    }
}
