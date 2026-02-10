package com.example.attendance_management_app.controller.employee;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.attendance_management_app.entity.PaidLeaveApplication;
import com.example.attendance_management_app.service.PaidLeaveApplicationService;

@Controller
@RequestMapping("/employee/paid-leave")
public class EmployeePaidLeaveController {

    private final PaidLeaveApplicationService service;

    public EmployeePaidLeaveController(PaidLeaveApplicationService service) {
        this.service = service;
    }

    // 申請画面
    @GetMapping("/apply")
    public String showApplyForm(Model model) {
        model.addAttribute("application", new PaidLeaveApplication());
        return "/employee/paid-leave/apply";
    }

    // 申請処理
    @PostMapping("/apply")
    public String apply(
            @ModelAttribute PaidLeaveApplication application,
            @AuthenticationPrincipal UserDetails userDetails) {

        service.apply(application, userDetails.getUsername());
        return "redirect:/employee/paid-leave/list?success";
    }

    // 自分の申請一覧
    @GetMapping("/list")
    public String myApplications(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        model.addAttribute(
            "applications",
            service.findByApplicant(userDetails.getUsername())
        );
        return "/employee/paid-leave/list";
    }
}
