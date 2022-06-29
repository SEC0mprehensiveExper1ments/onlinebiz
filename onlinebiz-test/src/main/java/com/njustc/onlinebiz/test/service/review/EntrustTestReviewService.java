package com.njustc.onlinebiz.test.service.review;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.review.EntrustTestReview;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 委托测试评估服务
 *
 */
public interface EntrustTestReviewService {
    /**
     * 创建一份工作检查表，在项目初始阶段生成，由市场部人员(主管)以及管理员来生成，若已有项目已生成工作检查表则创建失败
     * @param projectId 对应的测试项目ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * @return 成功返回创建的工作检查表ID， 失败返回 null
     * */
    String createEntrustTestReview(String projectId, Long userId, Role userRole);

    /**
     * 查看具体工作检查表内容，只有和该工作检查表(也就是和测试项目)有关的测试部人员(主管)以及
     * 质量部人员(主管) 可以查看
     * @param entrustTestReviewId 要查看的工作检查表ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * @return 存在则返回该工作检查表，否则返回 null
     * */
    EntrustTestReview findEntrustTestReview(String entrustTestReviewId, Long userId, Role userRole);

    /**
     * 更新工作检查表内容。只能由与此工作检查表相关的质量部人员和质量部主管可以执行此操作。
     * @param entrustTestReviewId 要更新的工作检查表ID
     * @param entrustTestReview 更新后的工作检查表内容
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void updateEntrustTestReview(String entrustTestReviewId, EntrustTestReview entrustTestReview, Long userId, Role userRole);

    /***
     * 删除一份测试评审表，只有管理员可以进行此操作
     * @param entrustTestReviewId 要删除的测试评审表ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void removeEntrustTestReview(String entrustTestReviewId, Long userId, Role userRole) throws IOException;
}
