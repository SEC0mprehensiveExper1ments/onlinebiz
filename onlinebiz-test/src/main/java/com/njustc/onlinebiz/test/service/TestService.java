package com.njustc.onlinebiz.test.service;

import com.njustc.onlinebiz.common.model.Scheme;


public interface TestService {
    /**
     * 创建一份新的测试方案
     * @param applyId 委托申请ID
     * @param contractId 委托合同ID
     * @return 新创建的测试方案对象，测试方案ID已被正确设置
     */
    Scheme createScheme(String applyId, String contractId);

    // 根据测试方案ID查找测试方案
    Scheme findSchemeById(String schemeId);

    // 根据测试方案ID和委托申请ID查找测试方案
    Scheme findSchemeByIdAndApply(String schemeId, String applyId);

    // 根据测试方案ID和委托申请ID查找测试方案
    Scheme findSchemeByIdAndContract(String schemeId, String contractId);

    // 更新测试方案信息，返回是否成功
    boolean updateScheme(Scheme scheme);

    // 根据测试方案ID删除测试方案，返回是否成功
    boolean removeScheme(String schemeId);

    // 根据条件查询测试方案
    ConditionBuilder search();
}
