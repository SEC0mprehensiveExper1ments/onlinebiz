package com.njustc.onlinebiz.test.service.review;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.review.ReportReview;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ReportReviewService {
    /**
     * 创建一份测试方案检查表，在项目初始阶段生成，由市场部人员(主管)以及管理员来生成，若已有项目已生成测试方案检查表则创建失败
     * @param reportId 对应的测试方案ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * @return 成功返回创建的测试方案检查表ID， 失败返回 null
     * */
    String createReportReview(String reportId, Long userId, Role userRole);

    /**
     * 查看具体测试方案检查表内容，只有和该测试方案检查表(也就是和测试项目)有关的测试部人员(主管)以及
     * 质量部人员(主管) 可以查看
     * @param reportReviewId 要查看的测试方案检查表ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * @return 存在则返回该测试方案检查表，否则返回 null
     * */
    ReportReview findReportReview(String reportReviewId, Long userId, Role userRole);

    /**
     * 更新测试方案检查表内容。只能由与此测试方案检查表相关的质量部人员和质量部主管可以执行此操作。
     * @param reportReviewId 要更新的测试方案检查表ID
     * @param reportReview 更新后的测试方案检查表内容
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void updateReportReview(String reportReviewId, ReportReview reportReview, Long userId, Role userRole);

    /**
     * 保存测试方案评审表的扫描件，只有负责此评审表的质量部人员(主管)和管理员可以执行此操作
     * @param reportReviewId 扫描件对应的测试评审表ID
     * @param scannedCopy 扫描件文件
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * @throws IOException IO异常
     */
    void saveScannedCopy(String reportReviewId, MultipartFile scannedCopy, Long userId, Role userRole) throws IOException;

    /**
     * 获取保存的测试评审表扫描件，只有负责此评审表的质量部人员(主管)和管理员
     * @param reportReviewId 扫描件对应的测试评审表ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * */
    Resource getScannedCopy(String reportReviewId, Long userId, Role userRole) throws IOException;

    /***
     * 删除一份测试评审表，只有管理员可以进行此操作
     * @param reportReviewId 要删除的测试评审表ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void removeReportReview(String reportReviewId, Long userId, Role userRole) throws IOException;
}
