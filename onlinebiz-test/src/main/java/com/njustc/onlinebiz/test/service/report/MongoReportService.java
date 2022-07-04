package com.njustc.onlinebiz.test.service.report;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.report.Report;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.dao.report.ReportDAO;
import com.njustc.onlinebiz.test.exception.report.ReportDAOFailureException;
import com.njustc.onlinebiz.test.exception.report.ReportInvalidStageException;
import com.njustc.onlinebiz.test.exception.report.ReportNotFoundException;
import com.njustc.onlinebiz.test.exception.report.ReportPermissionDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MongoReportService implements ReportService {
    private final ReportDAO reportDAO;
    private final ProjectDAO projectDAO;

    public MongoReportService(ReportDAO reportDAO, ProjectDAO projectDAO) {
        this.reportDAO = reportDAO;
        this.projectDAO = projectDAO;
    }

    @Override
    public String createReport(String projectId, String entrustId, Report.ReportContent content, Long userId, Role userRole) {
        if (!(userRole == Role.ADMIN || userRole == Role.MARKETER || userRole == Role.MARKETING_SUPERVISOR)) {
            throw new ReportPermissionDeniedException("无权新建测试报告");
        }
        Report report = new Report();
        report.setProjectId(projectId);
        report.setContent(content);
        report.setEntrustId(entrustId);
        return reportDAO.insertReport(report).getId();
    }

    @Override
    public Report findReport(String reportId, Long userId, Role userRole) {
        Report report = reportDAO.findReportById(reportId);
        if (report == null) {
            throw new ReportNotFoundException("该测试报告不存在");
        }
        if (!hasAuthorityToCheck(report, userId, userRole)) {
            throw new ReportPermissionDeniedException("无权查看测试报告");
        }

        ProjectStage projectStage = projectDAO.findProjectById(report.getProjectId()).getStatus().getStage();
        if (projectStage == ProjectStage.WAIT_FOR_QA || projectStage == ProjectStage.SCHEME_UNFILLED ||
                projectStage == ProjectStage.SCHEME_AUDITING || projectStage == ProjectStage.SCHEME_AUDITING_DENIED) {
            throw new ReportInvalidStageException("此阶段不可查看测试报告");
        }
        // 用户请求时，判断状态是否为REPORT_WAIT_CUSTOMER及其之后的状态，不是则抛出异常
        if (userRole == Role.CUSTOMER) {
            if (projectStage != ProjectStage.REPORT_WAIT_CUSTOMER &&
                    projectStage != ProjectStage.REPORT_CUSTOMER_CONFIRM &&
                    projectStage != ProjectStage.REPORT_CUSTOMER_REJECT &&
                    projectStage != ProjectStage.QA_ALL_REJECTED &&
                    projectStage != ProjectStage.QA_ALL_PASSED) {
                throw new ReportInvalidStageException("此阶段不可查看测试报告");
            }
        }

        return report;
    }

    @Override
    public void updateReport(String reportId, Report.ReportContent content, Long userId, Role userRole) {
        Report report = reportDAO.findReportById(reportId);
        if (report == null) {
            throw new ReportNotFoundException("该测试报告不存在");
        }
        if (!hasAuthorityToFill(report, userId, userRole)) {
            throw new ReportPermissionDeniedException("无权修改测试报告");
        }

        ProjectStage projectStage = projectDAO.findProjectById(report.getProjectId()).getStatus().getStage();
        if (!(projectStage == ProjectStage.SCHEME_REVIEW_UPLOADED || projectStage == ProjectStage.REPORT_QA_DENIED ||
                projectStage == ProjectStage.REPORT_CUSTOMER_REJECT || projectStage == ProjectStage.QA_ALL_REJECTED)) {
            throw new ReportInvalidStageException("此阶段不可修改测试报告");
        }

        if (!reportDAO.updateContent(reportId, content)){
            throw new ReportDAOFailureException("更新测试报告失败");
        }
    }

    @Override
    public void deleteReport(String reportId, Long userId, Role userRole) {
        if (userRole != Role.ADMIN) {
            throw new ReportPermissionDeniedException("无权删除测试报告");
        }
        Report report = reportDAO.findReportById(reportId);
        if (report == null) {
            throw new ReportNotFoundException("该测试报告不存在");
        }
        if (!reportDAO.deleteReport(report.getId())) {
            throw new ReportDAOFailureException("删除测试报告失败");
        }
    }

    private boolean hasAuthorityToCheck(Report report, Long userId, Role userRole) {
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.TESTING_SUPERVISOR || userRole == Role.QA_SUPERVISOR) {
            return true;
        }
        //项目的测试相关人员及质量相关人员可以查看
        Long testerId = projectDAO.findProjectById(report.getProjectId()).getProjectBaseInfo().getTesterId();
        Long qaId = projectDAO.findProjectById(report.getProjectId()).getProjectBaseInfo().getQaId();
        Long customerId = projectDAO.findProjectById(report.getProjectId()).getProjectBaseInfo().getCustomerId();
        if (userRole == Role.TESTER && userId.equals(testerId)) return true;
        else if (userRole == Role.CUSTOMER && userId.equals(customerId)) return true;
        return userRole == Role.QA && userId.equals(qaId);
    }

    private boolean hasAuthorityToFill(Report report, Long userId, Role userRole) {
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.TESTING_SUPERVISOR) {
            return true;
        }
        //项目的测试相关人员可以修改
        Long testerId = projectDAO.findProjectById(report.getProjectId()).getProjectBaseInfo().getTesterId();
        return userRole == Role.TESTER && userId.equals(testerId);
    }

}
