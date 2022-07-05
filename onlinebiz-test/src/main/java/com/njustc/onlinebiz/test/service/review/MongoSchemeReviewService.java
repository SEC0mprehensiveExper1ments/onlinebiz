package com.njustc.onlinebiz.test.service.review;


import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.review.SchemeReview;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.dao.review.SchemeReviewDAO;
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
public class MongoSchemeReviewService implements SchemeReviewService {

    private static final String SCANNED_COPY_DIR = "/root/test/review/";
    private final SchemeReviewDAO schemeReviewDAO;
    private final ProjectDAO projectDAO;
    public MongoSchemeReviewService(SchemeReviewDAO schemeReviewDAO, ProjectDAO projectDAO) {
        this.schemeReviewDAO = schemeReviewDAO;
        this.projectDAO = projectDAO;
    }

    @Override
    public String createSchemeReview(String projectId, String serialNumber, Long qaId, Long testerId) {
        // 创建一份空的检查表
        SchemeReview schemeReview = new SchemeReview();
        schemeReview.setProjectId(projectId);
        schemeReview.setSerialNumber(serialNumber);
        // 获取检查表ID
        return schemeReviewDAO.insertSchemeReview(schemeReview).getId();
    }

    @Override
    public SchemeReview findSchemeReview(String schemeReviewId, Long userId, Role userRole) {
        SchemeReview schemeReview = schemeReviewDAO.findSchemeReviewById(schemeReviewId);
        if (schemeReview == null) {
            throw new ReviewNotFoundException("该测试方案评审表不存在: " + schemeReviewId);
        } else if (!hasFindAuthority(schemeReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权查看该测试方案评审表");
        } else {
            ProjectStage projectStage = projectDAO.findProjectById(schemeReview.getProjectId()).getStatus().getStage();
            if(projectStage == ProjectStage.WAIT_FOR_QA || projectStage == ProjectStage.SCHEME_UNFILLED) {
                throw new ReviewInvalidStageException("此阶段不能查看测试方案评审表");
            }
        }
        return schemeReview;
    }

    private Boolean hasFindAuthority(SchemeReview schemeReview, Long userId, Role userRole) {
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        }
        // 测试部主管 和 质量部主管 都可以查看
        else if (userRole == Role.TESTING_SUPERVISOR || userRole == Role.QA_SUPERVISOR) {
            return true;
        }
        // 质量部相关人员均可查看
        else if (userId.equals(projectDAO.findProjectById(schemeReview.getProjectId()).getProjectBaseInfo().getQaId())) {
            return true;
        }
        // 测试部相关人员均可查看
        else if (userId.equals(projectDAO.findProjectById(schemeReview.getProjectId()).getProjectBaseInfo().getTesterId())) {
            return true;
        }
        return false;
    }

    @Override
    public void updateSchemeReview(String schemeReviewId, SchemeReview schemeReview, Long userId, Role userRole) {
        SchemeReview origin = schemeReviewDAO.findSchemeReviewById(schemeReviewId);
        // 检查测试方案评审表是否存在
        if (origin == null) {
            throw new ReviewNotFoundException("该测试方案评审表不存在");
        }
        if (!origin.getId().equals(schemeReview.getId())) {
            throw new ReviewPermissionDeniedException("测试方案评审表ID不一致");
        }
        // 检查测试项目的阶段
        ProjectStage projectStage = projectDAO.findProjectById(origin.getProjectId()).getStatus().getStage();
        if(projectStage == ProjectStage.SCHEME_AUDITING) {
            if (!hasUpdateOrDeleteAuthority(schemeReview, userId, userRole)) {
                throw new ReviewPermissionDeniedException("无权修改此测试方案评审表");
            }
        } else {
            throw new ReviewInvalidStageException("此阶段不能修改测试方案评审表");
        }
        // 更新测试方案评审表
        if (!schemeReviewDAO.updateSchemeReview(schemeReviewId, schemeReview)) {
            throw new ReviewDAOFailureException("测试方案评审表更新失败");
        }
    }

    private Boolean hasUpdateOrDeleteAuthority(SchemeReview schemeReview, Long userId, Role userRole) {
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        }
        // 质量部主管都可以更改
        else if (userRole == Role.QA_SUPERVISOR) {
            return true;
        }
        // 质量部相关人员可以删改
        else if (userId.equals(projectDAO.findProjectById(schemeReview.getProjectId()).getProjectBaseInfo().getQaId())) {
            return true;
        }
        return false;
    }

    @Override
    public void saveScannedCopy(String schemeReviewId, MultipartFile scannedCopy, Long userId, Role userRole) throws IOException {
        if (scannedCopy.isEmpty()) {
            throw new ReviewPermissionDeniedException("不能上传空的测试方案评审表的扫描件");
        }
        SchemeReview schemeReview = schemeReviewDAO.findSchemeReviewById(schemeReviewId);
        // 检查阶段
        ProjectStage projectStage = projectDAO.findProjectById(schemeReview.getProjectId()).getStatus().getStage();
        if (projectStage != ProjectStage.SCHEME_AUDITING_PASSED) {
            throw new ReviewInvalidStageException("项目当前状态不支持该操作");
        }
        // 检查权限
        if (!hasUploadOrDownloadAuthority(schemeReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权上传测试方案评审表");
        }
        // 保存测试方案评审表到磁盘
        String originalFilename = scannedCopy.getOriginalFilename();
        if (originalFilename == null) {
            throw new ReviewPermissionDeniedException("扫描文件名不能为空");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String path = SCANNED_COPY_DIR + schemeReviewId + suffix;
        scannedCopy.transferTo(new File(path.replaceAll("\\\\", "/")));
        // 将路径保存到合同对象中
        if (!schemeReviewDAO.updateScannedCopyPath(schemeReviewId, path)) {
            throw new ReviewDAOFailureException("保存扫描文件路径失败");
        }
    }

    @Override
    public Resource getScannedCopy(String schemeReviewId, Long userId, Role userRole) throws IOException {
        SchemeReview schemeReview = schemeReviewDAO.findSchemeReviewById(schemeReviewId);
        ProjectStage projectStage = projectDAO.findProjectById(schemeReview.getProjectId()).getStatus().getStage();
        // 检查阶段
        if (projectStage == ProjectStage.WAIT_FOR_QA ||
            projectStage == ProjectStage.SCHEME_UNFILLED ||
            projectStage == ProjectStage.SCHEME_AUDITING ||
            projectStage == ProjectStage.SCHEME_AUDITING_DENIED ||
            projectStage == ProjectStage.SCHEME_AUDITING_PASSED) {
            throw new ReviewInvalidStageException("项目当前状态不支持该操作");
        }
        // 检查权限
        if (!hasUploadOrDownloadAuthority(schemeReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权下载扫描件");
        }
        // 从磁盘读取文件
        return new InputStreamResource(new FileInputStream(schemeReview.getScannedCopyPath()));
    }

    @Override
    public String getScannedCopyFileName(String schemeReviewId, Long userId, Role userRole) {
        SchemeReview schemeReview = schemeReviewDAO.findSchemeReviewById(schemeReviewId);
        ProjectStage projectStage = projectDAO.findProjectById(schemeReview.getProjectId()).getStatus().getStage();
        // 检查阶段
        if (projectStage == ProjectStage.WAIT_FOR_QA ||
            projectStage == ProjectStage.SCHEME_UNFILLED ||
            projectStage == ProjectStage.SCHEME_AUDITING ||
            projectStage == ProjectStage.SCHEME_AUDITING_DENIED ||
            projectStage == ProjectStage.SCHEME_AUDITING_PASSED) {
            throw new ReviewInvalidStageException("项目当前状态不支持该操作");
        }
        // 检查权限
        if (!hasUploadOrDownloadAuthority(schemeReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权下载扫描件");
        }
        // 获取文件名
        return schemeReview.getScannedCopyPath().substring(schemeReview.getScannedCopyPath().lastIndexOf('/') + 1);
    }

    private Boolean hasUploadOrDownloadAuthority(SchemeReview schemeReview, Long userId, Role userRole){
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.TESTING_SUPERVISOR || userRole == Role.QA_SUPERVISOR) {
            return true;
        }
        // 项目的质量相关人员也可以上传下载
        else if (userId.equals(projectDAO.findProjectById(schemeReview.getProjectId()).getProjectBaseInfo().getQaId())) {
            return true;
        }
        return false;
    }

    @Override
    public void removeSchemeReview(String schemeReviewId, Long userId, Role userRole) {
        SchemeReview schemeReview = schemeReviewDAO.findSchemeReviewById(schemeReviewId);
        if (schemeReview == null) {
            throw new ReviewNotFoundException("尝试删除不存在的测试方案评审表");
        }
        if (!hasUpdateOrDeleteAuthority(schemeReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权删除此测试方案评审表");
        }
        if (!schemeReviewDAO.deleteSchemeAuditById(schemeReview.getId())) {
            throw new ReviewDAOFailureException("删除测试方案评审表失败");
        }
    }
}
