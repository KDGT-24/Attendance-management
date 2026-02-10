package com.example.attendance_management_app.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.attendance_management_app.service.JobSalaryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/salary/jobs")
@RequiredArgsConstructor
public class AdminJobSalaryController {

    private final JobSalaryService jobSalaryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("jobs", jobSalaryService.findAll());
        return "admin/salary/job_list";
    }

    @PostMapping
    public String create(
            @RequestParam String name,
            @RequestParam int salary) {

        jobSalaryService.create(name, salary);
        return "redirect:/admin/salary/jobs";
    }
    @PostMapping("/{id}/update")
    public String update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam int salary) {

        jobSalaryService.update(id, name, salary);
        return "redirect:/admin/salary/jobs";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        jobSalaryService.delete(id);
        return "redirect:/admin/salary/jobs";
    }

}
