package com.njustc.onlinebiz.test.dao.review;


import com.njustc.onlinebiz.common.model.test.review.SchemeReview;

/**
 * 测试方案检查表的数据访问层接口
 * */
public interface SchemeReviewDAO {

    /**
     * 保存一份测试方案检查表
     * @param schemeReview 要保存的测试方案检查表对象
     * @return 返回保存后的测试方案检查表对象
     * */
    SchemeReview insertSchemeReview(SchemeReview schemeReview);

    /**
     * 根据测试方案检查表ID查找测试方案检查表
     * @param schemeReviewId 测试方案检查表ID
     * @return 如果存在则返回测试方案检查表对象，否则返回null
     */
    SchemeReview findSchemeReviewById(String schemeReviewId);

    /**
     * 更新测试方案检查表的内容，此操作只更新测试方案检查表上要填写的字段
     * @param schemeReviewId 要更新的测试方案检查表ID
     * @param schemeReview 更新后的测试方案检查表
     * @return 成功后返回 true，失败返回 false
     * */
    Boolean updateSchemeReview(String schemeReviewId, SchemeReview schemeReview);

    /**
     * 更新测试检查表扫描件的路径
     * @param schemeReviewId 要更新的测试方案检查表的ID
     * @param path 更新后的扫描件的路径
     * */
    Boolean updateScannedCopyPath(String schemeReviewId, String path);

    /**
     * 根据ID删除测试方案检查表
     * @param schemeReviewId 要删除的测试方案检查表的ID
     * @return 成功返回 true, 失败返回 false
     * */
    Boolean deleteSchemeAuditById(String schemeReviewId);

}
