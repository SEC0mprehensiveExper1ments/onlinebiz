package com.njustc.onlinebiz.test.dao.review;

import com.njustc.onlinebiz.common.model.test.review.EntrustTestReview;

public interface EntrustTestReviewDAO {
    /**
     * 保存一份工作检查表
     * @param entrustTestReview 要保存的工作检查表对象
     * @return 返回保存后的工作检查表对象
     * */
    EntrustTestReview insertEntrustTestReview(EntrustTestReview entrustTestReview);

    /**
     * 根据工作检查表ID查找工作检查表
     * @param entrustTestReviewId 工作检查表ID
     * @return 如果存在则返回工作检查表对象，否则返回null
     */
    EntrustTestReview findEntrustTestReviewById(String entrustTestReviewId);

    /**
     * 更新工作检查表的内容，此操作只更新工作检查表上要填写的字段
     * @param entrustTestReviewId 要更新的工作检查表ID
     * @param entrustTestReview 更新后的工作检查表
     * @return 成功后返回 true，失败返回 false
     * */
    Boolean updateEntrustTestReview(String entrustTestReviewId, EntrustTestReview entrustTestReview);

    /**
     * 根据ID删除工作检查表
     * @param entrustTestReviewId 要删除的工作检查表的ID
     * @return 成功返回 true, 失败返回 false
     * */
    Boolean deleteEntrustTestReviewById(String entrustTestReviewId);
}
