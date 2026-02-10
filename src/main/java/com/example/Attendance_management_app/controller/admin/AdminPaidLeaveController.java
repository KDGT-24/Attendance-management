package com.example.attendance_management_app.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.attendance_management_app.service.PaidLeaveApplicationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/paid-leave")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminPaidLeaveController {

    private final PaidLeaveApplicationService service;

    // 一覧表示
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("applications", service.getAll());
        return "admin/paid-leave/list";
    }

    // 承認
    @PostMapping("/approve/{id}")
    public String approve(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        service.approve(id, userDetails.getUsername());
        return "redirect:/admin/paid-leave/list";
    }

    // 却下
    @PostMapping("/reject/{id}")
    public String reject(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        service.reject(id, userDetails.getUsername());
        return "redirect:/admin/paid-leave/list";
    }
}
