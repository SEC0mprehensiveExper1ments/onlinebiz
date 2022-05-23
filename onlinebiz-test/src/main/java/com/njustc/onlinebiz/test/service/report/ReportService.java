package com.njustc.onlinebiz.test.service.report;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.report.Report;

public interface ReportService {
  // 生成报告
  String createReport(Report report, Long userId, Role userRole);

  // 由 id 查询报告
  Report findReport(String reportId, Long userId, Role userRole);

  // 更新报告
  void updateReport(String reportId, Report report, Long userId, Role userRole);

  // 删除报
  void deleteReport(String reportId, Long userId, Role userRole);
}
