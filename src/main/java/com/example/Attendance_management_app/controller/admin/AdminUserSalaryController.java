package com.example.attendance_management_app.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.attendance_management_app.entity.User;
import com.example.attendance_management_app.repository.UserRepository;

@Controller
@RequestMapping("/admin/user/salary")
public class AdminUserSalaryController {

    private final UserRepository userRepository;

    public AdminUserSalaryController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/update")
    public String update(
            @RequestParam Long employeeNumber,
            @RequestParam(required = false) Integer jobId,
            @RequestParam(required = false) Integer[] skillIds,
            RedirectAttributes redirectAttributes) {

        User user = userRepository.findById(employeeNumber).orElse(null);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "社員が存在しません");
            return "redirect:/admin/user";
        }

        if (jobId != null) {
            user.setJobs(new Integer[]{jobId});
        }

        user.setSkills(skillIds);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "更新しました");
        return "redirect:/admin/user";
    }
}
