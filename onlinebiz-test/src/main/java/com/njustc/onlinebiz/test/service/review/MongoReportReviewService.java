package com.njustc.onlinebiz.test.service.review;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.review.ReportReview;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.dao.review.ReportReviewDAO;
import com.njustc.onlinebiz.test.exception.review.ReviewDAOFailureException;
import com.njustc.onlinebiz.test.exception.review.ReviewInvalidStageException;
import com.njustc.onlinebiz.test.exception.review.ReviewNotFoundException;
import com.njustc.onlinebiz.test.exception.review.ReviewPermissionDeniedException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class MongoReportReviewService implements ReportReviewService {
    private static final String SCANNED_COPY_DIR = "/root/test/review/";
    private final ReportReviewDAO reportReviewDAO;
    private final ProjectDAO projectDAO;

    public MongoReportReviewService(ReportReviewDAO reportReviewDAO, ProjectDAO projectDAO) {
        this.reportReviewDAO = reportReviewDAO;
        this.projectDAO = projectDAO;
    }

    @Override
    public String createReportReview(String projectId, Long userId, Role userRole) {
        if (userRole != Role.ADMIN && userRole != Role.MARKETER && userRole != Role.MARKETING_SUPERVISOR) {
            throw new ReviewPermissionDeniedException("只有负责测试项目的市场部人员或主管可以创建测试报告评审表");
        }
        //创建一份空的检查表
        ReportReview reportReview = new ReportReview();
        reportReview.setProjectId(projectId);
        //获取检查表id
        return reportReviewDAO.insertReportReview(reportReview).getId();
    }

    @Override
    public ReportReview findReportReview(String reportReviewId, Long userId, Role userRole) {
        ReportReview reportReview = reportReviewDAO.findReportReviewById(reportReviewId);
        if (reportReview == null) {
            throw new ReviewNotFoundException("该测试报告检查表不存在: " + reportReviewId);
        } else if (!hasAuthorityToCheck(reportReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("您没有权限查看该测试报告检查表");
        }

        ProjectStage projectStage = projectDAO.findProjectById(reportReview.getProjectId()).getStatus().getStage();
        if (projectStage != ProjectStage.REPORT_AUDITING && projectStage != ProjectStage.REPORT_QA_DENIED
                && projectStage != ProjectStage.REPORT_QA_PASSED && projectStage != ProjectStage.REPORT_WAIT_SENT_TO_CUSTOMER && projectStage != ProjectStage.REPORT_WAIT_CUSTOMER
                && projectStage != ProjectStage.REPORT_CUSTOMER_CONFIRM && projectStage != ProjectStage.REPORT_CUSTOMER_REJECT
                && projectStage != ProjectStage.QA_ALL_REJECTED && projectStage != ProjectStage.QA_ALL_PASSED) {
            throw new ReviewInvalidStageException("无权在此阶段查看测试报告检查表");
        }

        return reportReview;
    }

    @Override
    public void updateReportReview(String reportReviewId, ReportReview reportReview, Long userId, Role userRole) {
        ReportReview origin = reportReviewDAO.findReportReviewById(reportReviewId);
        if (origin == null) {
            throw new ReviewNotFoundException("该测试报告检查表不存在");
        }
        if (!origin.getId().equals(reportReview.getId())) {
            throw new ReviewPermissionDeniedException("测试报告检查表ID不一致");
        }
        if (!hasAuthorityToUpdateOrDelete(reportReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权修改此测试报告检查表");
        }

        ProjectStage projectStage = projectDAO.findProjectById(origin.getProjectId()).getStatus().getStage();
        if(projectStage != ProjectStage.REPORT_AUDITING){
            throw new ReviewInvalidStageException("此阶段不能修改测试报告检查表");
        }

        // 更新测试报告检查表
        if (!reportReviewDAO.updateReportReview(reportReviewId, reportReview)) {
            throw new ReviewDAOFailureException("更新测试报告检查表失败");
        }
    }

    @Override
    public void saveScannedCopy(String reportReviewId, MultipartFile scannedCopy, Long userId, Role userRole) throws IOException {
        ReportReview origin = reportReviewDAO.findReportReviewById(reportReviewId);
        if (scannedCopy.isEmpty()) {
            throw new ReviewNotFoundException("测试报告检查表的扫描件为空");
        }
        ReportReview reportReview = findReportReview(reportReviewId, userId, userRole);

        // 检查权限
        if (!hasAuthorityToUploadOrDownload(reportReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权上传测试报告检查表");
        }

        //检查状态
        ProjectStage projectStage = projectDAO.findProjectById(origin.getProjectId()).getStatus().getStage();
        if(projectStage != ProjectStage.REPORT_QA_PASSED){
            throw new ReviewInvalidStageException("此阶段不能上传测试报告检查表扫描件");
        }

        // 保存测试报告检查表到磁盘
        String originalFilename = scannedCopy.getOriginalFilename();
        if (originalFilename == null) {
            throw new ReviewNotFoundException("扫描文件名不能为空");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String path = SCANNED_COPY_DIR + reportReviewId + suffix;
        scannedCopy.transferTo(new File(path.replaceAll("\\\\", "/")));
        // 将路径保存到合同对象中
        if (!reportReviewDAO.updateScannedCopyPath(reportReviewId, path)) {
            throw new ReviewDAOFailureException("保存扫描文件路径失败");
        }
    }

    @Override
    public Resource getScannedCopy(String reportReviewId, Long userId, Role userRole) throws IOException {
        ReportReview reportReview = findReportReview(reportReviewId, userId, userRole);
        // 检查权限
        if (!hasAuthorityToUploadOrDownload(reportReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权下载扫描件");
        }
        // 从磁盘读取文件
        return new InputStreamResource(new FileInputStream(reportReview.getScannedCopyPath()));
    }

    @Override
    public String getScannedCopyFileName(String reportReviewId, Long userId, Role userRole) {
        ReportReview reportReview = findReportReview(reportReviewId, userId, userRole);
        // 检查权限
        if (!hasAuthorityToUploadOrDownload(reportReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权下载扫描件");
        }
        // 从磁盘读取文件
        return reportReview.getScannedCopyPath().substring(reportReview.getScannedCopyPath().lastIndexOf('/') + 1);
    }

    @Override
    public void removeReportReview(String reportReviewId, Long userId, Role userRole) {
        ReportReview reportReview = findReportReview(reportReviewId, userId, userRole);
        if (!hasAuthorityToUpdateOrDelete(reportReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权删除此测试报告检查表");
        }
        if (!reportReviewDAO.deleteReportAuditById(reportReview.getId())) {
            throw new ReviewDAOFailureException("删除测试报告检查表失败");
        }
    }

    private Boolean hasAuthorityToCheck(ReportReview reportReview, Long userId, Role userRole) {
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.MARKETING_SUPERVISOR || userRole == Role.QA_SUPERVISOR) {
            return true;
        }
        // 质量部相关人员均可查看
        else if (userId.equals(projectDAO.findProjectById(reportReview.getProjectId()).getProjectBaseInfo().getQaId())) {
            return true;
        }
        // 测试部相关人员均可查看
        else return userId.equals(projectDAO.findProjectById(reportReview.getProjectId()).getProjectBaseInfo().getTesterId());
    }

    private Boolean hasAuthorityToUpdateOrDelete(ReportReview reportReview, Long userId, Role userRole) {
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.MARKETING_SUPERVISOR || userRole == Role.QA_SUPERVISOR) {
            return true;
        }
        // 质量部相关人员均可删改
        else if (userId.equals(projectDAO.findProjectById(reportReview.getProjectId()).getProjectBaseInfo().getQaId())) {
            return true;
        }
        // 测试部相关人员均可删改
        else return userId.equals(projectDAO.findProjectById(reportReview.getProjectId()).getProjectBaseInfo().getTesterId());
    }

    private Boolean hasAuthorityToUploadOrDownload(ReportReview reportReview, Long userId, Role userRole) {
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.MARKETING_SUPERVISOR || userRole == Role.QA_SUPERVISOR) {
            return true;
        }
        // 项目的质量相关人员也可以上传下载
        else return userId.equals(projectDAO.findProjectById(reportReview.getProjectId()).getProjectBaseInfo().getQaId());
    }
}
