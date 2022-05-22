package com.njustc.onlinebiz.test.controller.report;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.report.Report;
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

  // 修改测试报告，并将报告标记为 未全部提交
  @PostMapping("/test/report/{reportId}/content")
  public Report updateReport(
      @PathVariable("reportId") String reportId,
      @RequestParam("userId") Long userId,
      @RequestParam("userRole") Role userRole,
      @RequestBody Report report) {
    reportService.updateReport(reportId, report, userId, userRole);
    return reportService.findReport(reportId, userId, userRole);
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
