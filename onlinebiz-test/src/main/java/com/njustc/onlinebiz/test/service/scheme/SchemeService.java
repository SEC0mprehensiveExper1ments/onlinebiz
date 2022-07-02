package com.njustc.onlinebiz.test.service.scheme;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.scheme.Scheme;
import com.njustc.onlinebiz.common.model.test.scheme.SchemeContent;


/**
 * 计划服务
 *
 */
public interface SchemeService {



    /**
     * 创建测试方案
     *
     * @param entrustId 委托id
     * @param content 内容
     * @param userId 用户id
     * @param userRole 用户角色
     * @param projectId 项目id
     * @return {@link String}
     */
    String createScheme(String entrustId, SchemeContent content, Long userId, Role userRole, String projectId);

    /**
     * 根据测试方案ID查找测试方案
     *
     * @param schemeId 计划id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link Scheme}
     */
    Scheme findScheme(String schemeId, Long userId, Role userRole);

    /**
     * 更新测试方案信息
     *
     * @param schemeId 计划id
     * @param content 内容
     * @param userId 用户id
     * @param userRole 用户角色
     */
    void updateScheme(String schemeId, SchemeContent content, Long userId, Role userRole);

    /**
     * 根据测试方案ID删除测试方案
     *
     * @param schemeId 计划id
     * @param userId 用户id
     * @param userRole 用户角色
     */
    void removeScheme(String schemeId, Long userId, Role userRole);

}
