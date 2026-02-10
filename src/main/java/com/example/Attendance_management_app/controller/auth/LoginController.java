package com.example.attendance_management_app.controller.auth;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal UserDetails userDetails) {

        // すでにログイン済みなら /continue へ
        if (userDetails != null) {
            return "redirect:/continue";
        }

        // 未ログインならログイン画面
        return "login";
    }
}
