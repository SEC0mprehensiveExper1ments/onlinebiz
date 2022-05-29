package com.njustc.onlinebiz.test.service.review;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.review.ReviewStage;
import com.njustc.onlinebiz.common.model.test.review.ReviewStatus;
import com.njustc.onlinebiz.test.dao.review.ReportReviewDAO;
import com.njustc.onlinebiz.test.exception.review.ReviewDAOFailureException;
import com.njustc.onlinebiz.test.exception.review.ReviewInvalidStageException;
import com.njustc.onlinebiz.test.exception.review.ReviewNotFoundException;
import com.njustc.onlinebiz.test.exception.review.ReviewPermissionDeniedException;
import com.njustc.onlinebiz.common.model.test.review.ReportReview;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class MongoReportReviewService implements ReportReviewService {
    private static final String ENTRUST_SERVICE = "http://onlinebiz-entrust";
    private static final String SCANNED_COPY_DIR = "~/review/";
    private final ReportReviewDAO reportReviewDAO;
    private final RestTemplate restTemplate;

    public MongoReportReviewService(ReportReviewDAO reportReviewDAO, RestTemplate restTemplate) {
        this.reportReviewDAO = reportReviewDAO;
        this.restTemplate = restTemplate;
    }

    @Override
    public String createReportReview(String reportId, Long userId, Role userRole) {
        if (userRole != Role.ADMIN && userRole != Role.MARKETER && userRole != Role.MARKETING_SUPERVISOR) {
            throw new ReviewPermissionDeniedException("只有负责测试项目的市场部人员或主管可以创建测试报告评审表");
        }
        //创建一份空的检查表
        ReportReview reportReview = new ReportReview();
        reportReview.setReportId(reportId);
        reportReview.setStatus(new ReviewStatus(ReviewStage.NOT_COPY_SAVED, null));
        //获取检查表id
        String reportReviewId = reportReviewDAO.insertReportReview(reportReview).getId();
        //TODO: 是否要将检查表id注册到测试报告中

        return reportReviewId;
    }

    @Override
    public ReportReview findReportReview(String reportReviewId, Long userId, Role userRole) {
        ReportReview reportReview = reportReviewDAO.findReportReviewById(reportReviewId);
        if (reportReview == null) {
            throw new ReviewNotFoundException("该测试报告检查表不存在: " + reportReviewId);
        } else if (!hasAuthorityToCheck(reportReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("您没有权限查看该测试报告检查表");
        }
        return reportReview;
    }

    @Override
    public void updateReportReview(String reportReviewId, ReportReview reportReview, Long userId, Role userRole) {
        ReportReview origin = reportReviewDAO.findReportReviewById(reportReviewId);
        if (!origin.getId().equals(reportReview.getId())) {
            throw new ReviewPermissionDeniedException("测试报告检查表ID不一致");
        }
        ReviewStage curStage = origin.getStatus().getStage();
        // 检查测试报告检查表的阶段，如果已上传附件则不能更改
        if (curStage == ReviewStage.NOT_COPY_SAVED) {
            if (!hasAuthorityToUpdateOrDelete(reportReview, userId, userRole)) {
                throw new ReviewPermissionDeniedException("无权修改此测试报告检查表");
            }
        } else {
            throw new ReviewInvalidStageException("此阶段不能修改测试报告检查表");
        }
        // 更新测试报告检查表
        reportReviewDAO.updateReportReview(reportReviewId, reportReview);
    }

    @Override
    public void saveScannedCopy(String reportReviewId, MultipartFile scannedCopy, Long userId, Role userRole) throws IOException {
        if (scannedCopy.isEmpty()) {
            throw new ReviewPermissionDeniedException("不能上传测试报告检查表的扫描件");
        }
        ReportReview reportReview = findReportReview(reportReviewId, userId, userRole);
        // 检查权限
        if (!hasAuthorityToUploadOrDownload(reportReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权上传测试报告检查表");
        }
        // 保存测试报告检查表到磁盘
        String originalFilename = scannedCopy.getOriginalFilename();
        if (originalFilename == null) {
            throw new ReviewPermissionDeniedException("扫描文件名不能为空");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String path = SCANNED_COPY_DIR + reportReviewId + "." + suffix;
        scannedCopy.transferTo(new File(path));
        // 将路径保存到合同对象中
        if (!reportReviewDAO.updateScannedCopyPath(reportReviewId, path)) {
            throw new ReviewDAOFailureException("保存扫描文件路径失败");
        }
        // 更新检查表阶段
        if (!reportReviewDAO.updateStatus(reportReviewId, new ReviewStatus(ReviewStage.COPY_SAVED, null))) {
            throw new ReviewDAOFailureException("更新检查表状态失败");
        }
    }

    @Override
    public Resource getScannedCopy(String reportReviewId, Long userId, Role userRole) throws IOException {
        ReportReview reportReview = findReportReview(reportReviewId, userId, userRole);
        ReviewStage curStage = reportReview.getStatus().getStage();
        // 检查阶段
        if (curStage != ReviewStage.COPY_SAVED) {
            throw new ReviewInvalidStageException("检查表扫描件尚未上传");
        }
        // 检查权限
        if (!hasAuthorityToUploadOrDownload(reportReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权下载扫描件");
        }
        // 从磁盘读取文件
        return new InputStreamResource(new FileInputStream(reportReview.getScannedCopyPath()));
    }

    @Override
    public void removeReportReview(String reportReviewId, Long userId, Role userRole) throws IOException {
        ReportReview reportReview = findReportReview(reportReviewId, userId, userRole);
        if (!hasAuthorityToUpdateOrDelete(reportReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权删除此测试检查报告");
        }
        if (!reportReviewDAO.deleteReportAuditById(reportReview.getId())) {
            throw new ReviewDAOFailureException("删除合同失败");
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
        // TODO: 项目的测试相关人员及质量相关人员可以查看

        return false;
    }

    private Boolean hasAuthorityToUpdateOrDelete(ReportReview reportReview, Long userId, Role userRole) {
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.MARKETING_SUPERVISOR || userRole == Role.QA_SUPERVISOR) {
            return true;
        }
        // TODO: 项目的质量相关人员可以删改
        return false;
    }

    private Boolean hasAuthorityToUploadOrDownload(ReportReview reportReview, Long userId, Role userRole) {
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.MARKETING_SUPERVISOR || userRole == Role.QA_SUPERVISOR) {
            return true;
        }
        // TODO: 项目的质量相关人员也可以上传下载
        return false;
    }
}
