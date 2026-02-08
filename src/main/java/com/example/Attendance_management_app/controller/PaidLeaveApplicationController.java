package com.example.Attendance_management_app.controller;
@Controller
@RequestMapping("/paid-leave")
public class PaidLeaveApplicationController {

    private final PaidLeaveApplicationService service;

    public PaidLeaveApplicationController(PaidLeaveApplicationService service) {
        this.service = service;
    }

    // 申請画面
    @GetMapping("/apply")
    public String showApplyForm(Model model) {
        model.addAttribute("application", new PaidLeaveApplication());
        return "paid-leave/apply";
    }

    // 申請処理
    @PostMapping("/apply")
    public String apply(@ModelAttribute PaidLeaveApplication application,
                        @AuthenticationPrincipal Employee employee) {
        service.apply(application, employee);
        return "redirect:/paid-leave/list";
    }

    // 承認
    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id,
                           @AuthenticationPrincipal Employee approver) {
        service.approve(id, approver);
        return "redirect:/admin/applications";
    }

    // 却下
    @PostMapping("/reject/{id}")
    public String reject(@PathVariable Long id,
                          @AuthenticationPrincipal Employee approver) {
        service.reject(id, approver);
        return "redirect:/admin/applications";
    }
}
