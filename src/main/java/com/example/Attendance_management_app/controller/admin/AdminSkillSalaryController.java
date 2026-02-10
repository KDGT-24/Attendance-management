package com.example.attendance_management_app.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.attendance_management_app.service.SkillSalaryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/salary/skills")
@RequiredArgsConstructor
public class AdminSkillSalaryController {

    private final SkillSalaryService skillSalaryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("skills", skillSalaryService.findAll());
        return "admin/salary/skill_list";
    }

    @PostMapping
    public String create(
            @RequestParam String name,
            @RequestParam int salary) {

        skillSalaryService.create(name, salary);
        return "redirect:/admin/salary/skills";
    }
    @PostMapping("/{id}/update")
    public String update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam int salary) {

        skillSalaryService.update(id, name, salary);
        return "redirect:/admin/salary/skills";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        skillSalaryService.delete(id);
        return "redirect:/admin/salary/skills";
    }

}
