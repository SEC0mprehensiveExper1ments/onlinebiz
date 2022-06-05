package com.njustc.onlinebiz.test.service.review;


import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.dao.review.SchemeReviewDAO;
import com.njustc.onlinebiz.test.exception.review.ReviewDAOFailureException;
import com.njustc.onlinebiz.test.exception.review.ReviewInvalidStageException;
import com.njustc.onlinebiz.test.exception.review.ReviewNotFoundException;
import com.njustc.onlinebiz.test.exception.review.ReviewPermissionDeniedException;
import com.njustc.onlinebiz.common.model.test.review.SchemeReview;
import com.njustc.onlinebiz.common.model.test.review.ReviewStage;
import com.njustc.onlinebiz.common.model.test.review.ReviewStatus;
import com.njustc.onlinebiz.test.service.project.ProjectService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class MongoSchemeReviewService implements SchemeReviewService {

    private static final String ENTRUST_SERVICE = "http://onlinebiz-entrust";
    private static final String SCANNED_COPY_DIR = "~/review/";
    private final SchemeReviewDAO schemeReviewDAO;
    private final ProjectService projectService;

    public MongoSchemeReviewService(SchemeReviewDAO schemeReviewDAO, ProjectService projectService) {
        this.schemeReviewDAO = schemeReviewDAO;
        this.projectService = projectService;
    }

    @Override
    public String createSchemeReview(String projectId, Long qaId, Long testerId) {
/*        if (userRole != Role.ADMIN && userRole != Role.MARKETER && userRole != Role.MARKETING_SUPERVISOR) {
            throw new ReviewPermissionDeniedException("只有负责测试项目的市场部人员或主管可以创建合同");
        }*/
        // 创建一份空的检查表
        SchemeReview schemeReview = new SchemeReview();
        schemeReview.setProjectId(projectId);
//        schemeReview.setQaId(qaId);
//        schemeReview.setTesterId(testerId);
        schemeReview.setStatus(new ReviewStatus(ReviewStage.NOT_COPY_SAVED, null));
        // 获取检查表ID
        String schemeReviewId = schemeReviewDAO.insertSchemeReview(schemeReview).getId();

        return schemeReviewId;
    }

    @Override
    public SchemeReview findSchemeReview(String schemeReviewId, Long userId, Role userRole) {
        SchemeReview schemeReview = schemeReviewDAO.findSchemeReviewById(schemeReviewId);
        if (schemeReview == null) {
            throw new ReviewNotFoundException("该测试方案检查表不存在: " + schemeReviewId);
        } else if (!hasFindAuthority(schemeReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权查看该测试方案检查表");
        }
        return schemeReview;
    }

    private Boolean hasFindAuthority(SchemeReview schemeReview, Long userId, Role userRole) {
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.MARKETING_SUPERVISOR || userRole == Role.QA_SUPERVISOR) {
            return true;
        }
//        // 质量部和测试部相关人员均可查看
//        else if (userId.equals(schemeReview.getQaId()) || userId.equals(schemeReview.getTesterId())) {
//            return true;
//        }
        // 质量部相关人员均可查看
        else if (userId.equals(projectService.getProjectBaseInfo(schemeReview.getProjectId()).getQaId())) {
            return true;
        }
        // 测试部相关人员均可查看
        else if (userId.equals(projectService.getProjectBaseInfo(schemeReview.getProjectId()).getTesterId())) {
            return true;
        }
        return false;
    }

    @Override
    public void updateSchemeReview(String schemeReviewId, SchemeReview schemeReview, Long userId, Role userRole) {
        SchemeReview origin = findSchemeReview(schemeReviewId, userId, userRole);
        if (!origin.getId().equals(schemeReview.getId())) {
            throw new ReviewPermissionDeniedException("测试方案检查表ID不一致");
        }
        ReviewStage curStage = origin.getStatus().getStage();
        // 检查测试方案检查表的阶段，如果已上传附件则不能更改
        if(curStage == ReviewStage.NOT_COPY_SAVED) {
            if (!hasUpdateOrDeleteAuthority(schemeReview, userId, userRole)) {
                throw new ReviewPermissionDeniedException("无权修改此测试方案检查表");
            }
        } else {
            throw new ReviewInvalidStageException("此阶段不能修改测试方案检查表");
        }
        // 更新测试方案检查表
        schemeReviewDAO.updateSchemeReview(schemeReviewId, schemeReview);
    }

    private Boolean hasUpdateOrDeleteAuthority(SchemeReview schemeReview, Long userId, Role userRole) {
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.MARKETING_SUPERVISOR || userRole == Role.QA_SUPERVISOR) {
            return true;
        }
        // 质量部相关人员可以删改
        else if (userId.equals(projectService.getProjectBaseInfo(schemeReview.getProjectId()).getQaId())) {
            return true;
        }
        return false;
    }

    @Override
    public void saveScannedCopy(String schemeReviewId, MultipartFile scannedCopy, Long userId, Role userRole) throws IOException {
        if (scannedCopy.isEmpty()) {
            throw new ReviewPermissionDeniedException("不能上传测试方案检查表的扫描件");
        }
        SchemeReview schemeReview = findSchemeReview(schemeReviewId, userId, userRole);
        // 检查权限
        if (!hasUploadOrDownloadAuthority(schemeReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权上传测试方案检查表");
        }
        // 保存测试方案检查表到磁盘
        String originalFilename = scannedCopy.getOriginalFilename();
        if (originalFilename == null) {
            throw new ReviewPermissionDeniedException("扫描文件名不能为空");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String path = SCANNED_COPY_DIR + schemeReviewId + "." + suffix;
        scannedCopy.transferTo(new File(path));
        // 将路径保存到合同对象中
        if (!schemeReviewDAO.updateScannedCopyPath(schemeReviewId, path)) {
            throw new ReviewDAOFailureException("保存扫描文件路径失败");
        }
        // 更新检查表阶段
        if (!schemeReviewDAO.updateStatus(schemeReviewId, new ReviewStatus(ReviewStage.COPY_SAVED, null))) {
            throw new ReviewDAOFailureException("更新检查表状态失败");
        }
    }

    @Override
    public Resource getScannedCopy(String schemeReviewId, Long userId, Role userRole) throws IOException {
        SchemeReview schemeReview = findSchemeReview(schemeReviewId, userId, userRole);
        ReviewStage curStage = schemeReview.getStatus().getStage();
        // 检查阶段
        if (curStage != ReviewStage.COPY_SAVED) {
            throw new ReviewInvalidStageException("检查表扫描件尚未上传");
        }
        // 检查权限
        if (!hasUploadOrDownloadAuthority(schemeReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权下载扫描件");
        }
        // 从磁盘读取文件
        return new InputStreamResource(new FileInputStream(schemeReview.getScannedCopyPath()));
    }

    private Boolean hasUploadOrDownloadAuthority(SchemeReview schemeReview, Long userId, Role userRole){
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN){
            return true;
        } else if (userRole == Role.MARKETING_SUPERVISOR || userRole == Role.QA_SUPERVISOR) {
            return true;
        }
        // 项目的质量相关人员也可以上传下载
        else if (userId.equals(projectService.getProjectBaseInfo(schemeReview.getProjectId()).getQaId())) {
            return true;
        }
        return false;
    }

    @Override
    public void removeSchemeReview(String schemeReviewId, Long userId, Role userRole) {
        SchemeReview schemeReview = findSchemeReview(schemeReviewId, userId, userRole);
        if (!hasUpdateOrDeleteAuthority(schemeReview, userId, userRole)) {
            throw new ReviewPermissionDeniedException("无权删除此测试检查方案");
        }
        if (!schemeReviewDAO.deleteSchemeAuditById(schemeReview.getId())) {
            throw new ReviewDAOFailureException("删除合同失败");
        }
    }
}
