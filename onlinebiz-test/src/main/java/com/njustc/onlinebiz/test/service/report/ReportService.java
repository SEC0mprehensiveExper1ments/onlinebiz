package com.njustc.onlinebiz.test.service.report;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.Report;

public interface ReportService {
  // 生成报告
  String createReport(Report report, Long userId, Role userRole);

  // 由 id 查询报告
  Report findReport(String reportId, Long userId, Role userRole);

  // 更新报告，返回是否更新成功
  boolean updateReport(Report report, Long userId, Role userRole);

  // 删除报告，返回是否删除成功
  boolean deleteReport(String reportId, Long userId, Role userRole);
}
