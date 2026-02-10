package com.example.attendance_management_app.controller.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.attendance_management_app.form.PasswordChangeForm;
import com.example.attendance_management_app.service.UserService;

@Controller
public class PasswordController {

    private final UserService userService;

    public PasswordController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/password/change")
    public String show(Model model) {
        model.addAttribute("form", new PasswordChangeForm());
        return "password_change";
    }

    @PostMapping("/password/change")
    public String change(
            @ModelAttribute PasswordChangeForm form,
            Authentication authentication,
            Model model) {

        if (!form.getNewPassword().equals(form.getNewPasswordConfirm())) {
            model.addAttribute("error", "新しいパスワードが一致しません");
            return "password_change";
        }

        String error = userService.changePasswordByUsername(
                authentication.getName(),
                form.getCurrentPassword(),
                form.getNewPassword());

        if (error != null) {
            model.addAttribute("error", error);
            return "password_change";
        }

        return "redirect:/password/change?success";
    }
}
