package com.njustc.onlinebiz.test.service.review;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.review.EntrustTestReview;
import com.njustc.onlinebiz.common.model.test.review.ReviewStage;
import com.njustc.onlinebiz.common.model.test.review.ReviewStatus;
import com.njustc.onlinebiz.test.dao.review.EntrustTestReviewDAO;
import com.njustc.onlinebiz.test.exception.review.ReviewNotFoundException;
import com.njustc.onlinebiz.test.exception.review.ReviewPermissionDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class MongoEntrustTestReviewService implements EntrustTestReviewService {

    private final EntrustTestReviewDAO entrustTestReviewDAO;
    private final RestTemplate restTemplate;

    public MongoEntrustTestReviewService(EntrustTestReviewDAO entrustTestReviewDAO, RestTemplate restTemplate) {
        this.entrustTestReviewDAO = entrustTestReviewDAO;
        this.restTemplate = restTemplate;
    }


    @Override
    public String createEntrustTestReview(String projectId, Long userId, Role userRole) {
        if (userRole != Role.ADMIN && userRole != Role.MARKETER && userRole != Role.MARKETING_SUPERVISOR) {
            throw new ReviewPermissionDeniedException("只有负责测试项目的市场部人员或主管可以创建测试报告评审表");
        }
        // 创建一份空的工作检查表
        EntrustTestReview entrustTestReview = new EntrustTestReview();
        entrustTestReview.setProjectId(projectId);
        entrustTestReview.setStatus(new ReviewStatus(ReviewStage.COPY_SAVED, null));
        String entrustTestReviewId = entrustTestReviewDAO.insertEntrustTestReview(entrustTestReview).getId();
        return entrustTestReviewId;
    }

    @Override
    public EntrustTestReview findEntrustTestReview(String entrustTestReviewId, Long userId, Role userRole) {
        EntrustTestReview entrustTestReview = entrustTestReviewDAO.findEntrustTestReviewById(entrustTestReviewId);
        if (entrustTestReview == null) {
            throw new ReviewNotFoundException("工作评审表不存在");
        }
        if (userRole == Role.CUSTOMER) {
            throw new ReviewPermissionDeniedException("只有测试中心人员可以查看工作评审表");
        }
        return entrustTestReview;
    }

    @Override
    public void updateEntrustTestReview(String entrustTestReviewId, EntrustTestReview entrustTestReview, Long userId, Role userRole) {
        EntrustTestReview origin = entrustTestReviewDAO.findEntrustTestReviewById(entrustTestReviewId);
        if (!origin.getProjectId().equals(entrustTestReview.getProjectId())) {
            throw new ReviewPermissionDeniedException("工作评审表不属于当前项目");
        }
        if (userRole == Role.CUSTOMER) {
            throw new ReviewPermissionDeniedException("只有测试中心人员可以修改工作评审表");
        }
        entrustTestReviewDAO.updateEntrustTestReview(entrustTestReviewId, entrustTestReview);
    }

    @Override
    public void removeEntrustTestReview(String entrustTestReviewId, Long userId, Role userRole) throws IOException {
        EntrustTestReview entrustTestReview = entrustTestReviewDAO.findEntrustTestReviewById(entrustTestReviewId);
        if (entrustTestReview == null) {
            throw new ReviewNotFoundException("工作评审表不存在");
        }
        if (userRole != Role.ADMIN) {
            throw new ReviewPermissionDeniedException("只有管理员可以删除工作评审表");
        }
        entrustTestReviewDAO.deleteEntrustTestReviewById(entrustTestReviewId);
    }
}
