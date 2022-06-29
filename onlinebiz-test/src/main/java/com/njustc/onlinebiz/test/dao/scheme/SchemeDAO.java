package com.njustc.onlinebiz.test.dao.scheme;

import com.njustc.onlinebiz.common.model.test.scheme.Scheme;
import com.njustc.onlinebiz.common.model.test.scheme.SchemeContent;

/**
 * 测试方案
 * dao层接口
 *
 */
public interface SchemeDAO {
    /**
     * 插入方案
     *
     * @param scheme 计划
     * @return {@link Scheme}
     */
    Scheme insertScheme(Scheme scheme);

    /**
     * 发现方案通过id
     *
     * @param schemeId 计划id
     * @return {@link Scheme}
     */
    Scheme findSchemeById(String schemeId);

    /**
     * 更新内容
     *
     * @param schemeId 计划id
     * @param content 内容
     * @return boolean
     */
    boolean updateContent(String schemeId, SchemeContent content);

    /**
     * 删除计划
     *
     * @param schemeId 计划id
     * @return boolean
     */
    boolean deleteScheme(String schemeId);
}
