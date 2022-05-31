package com.njustc.onlinebiz.test.service.scheme;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.scheme.Scheme;
import com.njustc.onlinebiz.common.model.test.scheme.SchemeContent;


public interface SchemeService {



    String createScheme(String entrustId, SchemeContent content, Long userId, Role userRole, String projectId);

    // 根据测试方案ID查找测试方案
    Scheme findScheme(String schemeId, Long userId, Role userRole);

    // 更新测试方案信息
    void updateScheme(String schemeId, SchemeContent content, Long userId, Role userRole);

    // 根据测试方案ID删除测试方案
    void removeScheme(String schemeId, Long userId, Role userRole);

    void denyContent(String schemeId, String message, Long userId, Role userRole);

    void approveContent(String schemeId, Long userId, Role userRole);
}
