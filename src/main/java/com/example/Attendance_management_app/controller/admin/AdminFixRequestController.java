package com.example.attendance_management_app.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.attendance_management_app.service.FixRequestService;

@Controller
@RequestMapping("/admin/fix-requests")
public class AdminFixRequestController {

    private final FixRequestService fixRequestService;

    public AdminFixRequestController(FixRequestService fixRequestService) {
        this.fixRequestService = fixRequestService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("fixRequests",
                fixRequestService.getAllPendingFixRequests());
        return "admin/fix_request_list";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id) {
        fixRequestService.approveFixRequest(id);
        return "redirect:/admin/fix-requests";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id) {
        fixRequestService.rejectFixRequest(id);
        return "redirect:/admin/fix-requests";
    }
}
