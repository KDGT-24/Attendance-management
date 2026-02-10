package com.example.attendance_management_app.controller.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/continue")
    public String continueAfterLogin(Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return isAdmin
                ? "redirect:/admin/dashboard"
                : "redirect:/employee/dashboard";
    }
}
