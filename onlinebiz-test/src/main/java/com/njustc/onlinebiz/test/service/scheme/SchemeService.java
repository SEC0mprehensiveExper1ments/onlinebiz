package com.njustc.onlinebiz.test.service.scheme;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.Scheme;
import com.njustc.onlinebiz.test.model.SchemeContent;


public interface SchemeService {



    String createScheme(String entrustId, SchemeContent schemeContent, Long userId, Role userRole);

    // 根据测试方案ID查找测试方案
    Scheme findScheme(String schemeId, Long userId, Role userRole);

    // 更新测试方案信息，返回是否成功
    boolean updateScheme(Scheme scheme, Long userId, Role userRole);

    // 根据测试方案ID删除测试方案，返回是否成功
    boolean removeScheme(String schemeId, Long userId, Role userRole);
}
