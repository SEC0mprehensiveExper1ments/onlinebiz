package com.njustc.onlinebiz.test.service.report;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.report.Report;
import com.njustc.onlinebiz.common.model.test.report.ReportStage;
import com.njustc.onlinebiz.common.model.test.report.ReportStatus;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.dao.report.ReportDAO;
import com.njustc.onlinebiz.test.exception.report.ReportDAOFailureException;
import com.njustc.onlinebiz.test.exception.report.ReportInvalidStageException;
import com.njustc.onlinebiz.test.exception.report.ReportNotFoundException;
import com.njustc.onlinebiz.test.exception.report.ReportPermissionDeniedException;
import com.njustc.onlinebiz.test.exception.scheme.SchemeDAOFailureException;
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
        report.setStatus(new ReportStatus(ReportStage.UNFINISHED, "未全部提交"));
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
        if (report.getStatus().getStage() == ReportStage.FINISHED ||
                report.getStatus().getStage() == ReportStage.QA_PASSED ||
                report.getStatus().getStage() == ReportStage.CUSTOMER_CONFIRM) {
            throw new ReportInvalidStageException("此阶段不能修改测试报告");
        }
        if (!reportDAO.updateContent(reportId, content) || !reportDAO.updateStatus(reportId, new ReportStatus(ReportStage.FINISHED, "已修改，待质量部审核"))) {
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
            throw new SchemeDAOFailureException("删除测试报告失败");
        }
    }

    @Override
    public void QAApprove(String reportId, Long userId, Role userRole) {
        Report report = reportDAO.findReportById(reportId);
        if (report == null) {
            throw new ReportNotFoundException("该测试报告不存在");
        }
        if (!hasAuthorityToAudit(report, userId, userRole)) {
            throw new ReportPermissionDeniedException("无权审核测试报告");
        }
        if (report.getStatus().getStage() != ReportStage.FINISHED) {
            throw new ReportInvalidStageException("此阶段不能审核测试报告");
        }
        if (!reportDAO.updateStatus(reportId, new ReportStatus(ReportStage.QA_PASSED, "质量部已通过"))) {
            throw new ReportDAOFailureException("通过测试报告失败");
        }
    }

    @Override
    public void QADeny(String reportId, String message, Long userId, Role userRole) {
        Report report = reportDAO.findReportById(reportId);
        if (report == null) {
            throw new ReportNotFoundException("该测试报告不存在");
        }
        if (!hasAuthorityToAudit(report, userId, userRole)) {
            throw new ReportPermissionDeniedException("无权审核测试报告");
        }
        if (report.getStatus().getStage() != ReportStage.FINISHED) {
            throw new ReportInvalidStageException("此阶段不能审核测试报告");
        }
        if (!reportDAO.updateStatus(reportId, new ReportStatus(ReportStage.QA_REJECTED, message))) {
            throw new ReportDAOFailureException("否定测试报告失败");
        }
    }

    @Override
    public void customerApprove(String reportId, Long userId, Role userRole) {
        Report report = reportDAO.findReportById(reportId);
        if (report == null) {
            throw new ReportNotFoundException("该测试报告不存在");
        }
        //获取委托的客户id
        if (!(userRole == Role.CUSTOMER && userId.equals(projectDAO.findProjectById(report.getProjectId()).getProjectBaseInfo().getCustomerId()))) {
            throw new ReportPermissionDeniedException("无权审核测试报告");
        }
        if (report.getStatus().getStage() != ReportStage.QA_PASSED) {
            throw new ReportInvalidStageException("质量部未通过，不得递交客户审阅");
        }
        if (!reportDAO.updateStatus(reportId, new ReportStatus(ReportStage.CUSTOMER_CONFIRM, "客户认可"))) {
            throw new ReportDAOFailureException("通过测试报告失败");
        }
    }

    @Override
    public void customerDeny(String reportId, String message, Long userId, Role userRole) {
        Report report = reportDAO.findReportById(reportId);
        if (report == null) {
            throw new ReportNotFoundException("该测试报告不存在");
        }
        //获取委托的客户id
        if (!(userRole == Role.CUSTOMER && userId.equals(projectDAO.findProjectById(report.getProjectId()).getProjectBaseInfo().getCustomerId()))) {
            throw new ReportPermissionDeniedException("无权审核测试报告");
        }
        if (report.getStatus().getStage() != ReportStage.QA_PASSED) {
            throw new ReportInvalidStageException("质量部未通过，不得递交客户审阅");
        }
        if (!reportDAO.updateStatus(reportId, new ReportStatus(ReportStage.CUSTOMER_REJECT, message))) {
            throw new ReportDAOFailureException("否定测试报告失败");
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
        if (userRole == Role.TESTER && userId.equals(testerId)) return true;
        if (userRole == Role.QA && userId.equals(qaId)) return true;
        return false;
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
        if (userRole == Role.TESTER && userId.equals(testerId)) return true;
        return false;
    }

    private boolean hasAuthorityToAudit(Report report, Long userId, Role userRole) {
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.QA_SUPERVISOR) {
            return true;
        }
        //项目的质量部相关人员可以审核
        Long qaId = projectDAO.findProjectById(report.getProjectId()).getProjectBaseInfo().getQaId();
        if (userRole == Role.QA && userId.equals(qaId)) return true;
        return false;
    }
}
