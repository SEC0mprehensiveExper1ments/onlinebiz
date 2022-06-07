package com.njustc.onlinebiz.test.dao.review;

import com.njustc.onlinebiz.common.model.test.review.ReportReview;

public interface ReportReviewDAO {
    /**
     * 保存一份测试报告检查表
     * @param reportReview 要保存的测试报告检查表对象
     * @return 返回保存后的测试报告检查表对象
     * */
    ReportReview insertReportReview(ReportReview reportReview);

    /**
     * 根据测试报告检查表ID查找测试报告检查表
     * @param reportReviewId 测试报告检查表ID
     * @return 如果存在则返回测试报告检查表对象，否则返回null
     */
    ReportReview findReportReviewById(String reportReviewId);

    /**
     * 更新测试报告检查表的内容，此操作只更新测试报告检查表上要填写的字段
     * @param reportReviewId 要更新的测试报告检查表ID
     * @param reportReview 更新后的测试报告检查表
     * @return 成功后返回 true，失败返回 false
     * */
    Boolean updateReportReview(String reportReviewId, ReportReview reportReview);

    /**
     * 更新测试检查表扫描件的路径
     * @param reportReviewId 要更新的测试报告检查表的ID
     * @param path 更新后的扫描件的路径
     * */
    Boolean updateScannedCopyPath(String reportReviewId, String path);

    /**
     * 根据ID删除测试报告检查表
     * @param reportReviewId 要删除的测试报告检查表的ID
     * @return 成功返回 true, 失败返回 false
     * */
    Boolean deleteReportAuditById(String reportReviewId);
}
