package com.njustc.onlinebiz.test.service.report;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.report.Report;

/**
 * 报告服务
 *
 */
public interface ReportService {
    /**
     * 创建报告
     *
     * @param projectId 项目id
     * @param entrustId 委托id
     * @param content 内容
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    String createReport(String projectId, String entrustId, Report.ReportContent content, Long userId, Role userRole);

    /**
     * 由 id 查询报告
     *
     * @param reportId 报告id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link Report}
     */
    Report findReport(String reportId, Long userId, Role userRole);

    /**
     * 更新报告
     *
     * @param reportId 报告id
     * @param content 内容
     * @param userId 用户id
     * @param userRole 用户角色
     */
    void updateReport(String reportId, Report.ReportContent content, Long userId, Role userRole);

    /**
     * 删除报告
     *
     * @param reportId 报告id
     * @param userId 用户id
     * @param userRole 用户角色
     */
    void deleteReport(String reportId, Long userId, Role userRole);
}
