package com.njustc.onlinebiz.test.controller.report;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.report.Report;
import com.njustc.onlinebiz.test.service.report.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 查看任意测试报告的详细信息
    @GetMapping("/test/report/{reportId}")
    public Report getReport(
            @PathVariable("reportId") String reportId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole) {
        return reportService.findReport(reportId, userId, userRole);
    }

    // 修改测试报告，并将报告标记为 测试报告已提交，质量部可审核
    @PostMapping("/test/report/{reportId}/content")
    public void updateReport(
            @PathVariable("reportId") String reportId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody Report.ReportContent report) {
        reportService.updateReport(reportId, report, userId, userRole);
    }

    // 质量部拒绝测试报告
    @PostMapping("/test/report/{reportId}/content/denial")
    public void QADeny(
            @PathVariable("reportId") String reportId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestParam(value = "message", required = false) String message
    ) {
        reportService.QADeny(reportId, message, userId, userRole);
    }

    // 质量部通过测试报告
    @PostMapping("/test/report/{reportId}/content/approve")
    public void QAApprove(
            @PathVariable("reportId") String reportId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        reportService.QAApprove(reportId, userId, userRole);
    }

    // 客户通过测试报告
    @PostMapping("/test/report/{reportId}/individual/content/approve")
    public void customerApprove(
            @PathVariable("reportId") String reportId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        reportService.customerApprove(reportId, userId, userRole);
    }

    // 客户拒绝测试报告
    @PostMapping("/test/report/{reportId}/individual/content/denial")
    public void customerDeny(
            @PathVariable("reportId") String reportId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestParam(value = "message", required = false) String message
    ) {
        reportService.customerDeny(reportId, message, userId, userRole);
    }

    // 删除测试报告
    @DeleteMapping("/test/report/{reportId}")
    public void deleteReport(
            @PathVariable("reportId") String reportId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole) {
        reportService.deleteReport(reportId, userId, userRole);
    }
}
