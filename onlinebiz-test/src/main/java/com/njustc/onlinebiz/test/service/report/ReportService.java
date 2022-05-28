package com.njustc.onlinebiz.test.service.report;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.report.Report;

public interface ReportService {
    // 生成报告
    String createReport(String projectId, String entrustId, Report.ReportContent content, Long userId, Role userRole);

    // 由 id 查询报告
    Report findReport(String reportId, Long userId, Role userRole);

    // 更新报告
    void updateReport(String reportId, Report.ReportContent content, Long userId, Role userRole);

    // 删除报告
    void deleteReport(String reportId, Long userId, Role userRole);

    void QAApprove(String reportId, Long userId, Role userRole);

    void QADeny(String reportId, String message, Long userId, Role userRole);

    void CustomerApprove(String reportId, Long userId, Role userRole);

    void CustomerDeny(String reportId, String message, Long userId, Role userRole);
}
