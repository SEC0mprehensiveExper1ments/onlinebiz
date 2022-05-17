package com.example.onlinebiztest.service;

import com.njustc.onlinebiz.common.model.Scheme;


public interface TestService {
    /**
     * 创建一份新的测试方案
     *
     * @param creatorId 生成该测试方案的用户，应当为内部员工ID
     * @param applyId   对应的委托申请ID
     * @param scheme    传入的JS006文件数据
     * @return 新创建的测试方案对象，测试方案ID已被正确设置
     */
    Scheme createScheme(Long creatorId, String applyId, Scheme scheme);

    // 根据测试方案ID查找测试方案
    Scheme findSchemeById(String schemeId);

    /* 由于测试方案本身的具体性，并且为了使一次性处理的数据量不太大，规定：
    无法直接获取所有测试方案信息，一次最多给前端提供一个按测试方案ID查找的测试方案
     */


    // 根据测试方案ID和测试创建者ID查找测试方案
    Scheme findSchemeByIdAndCreator(String schemeId, Long userId);

    // 更新测试方案信息，返回是否成功
    boolean updateScheme(Scheme scheme);

    // 根据测试方案ID删除测试方案，返回是否成功
    boolean removeScheme(String schemeId);

    // 根据条件查询测试方案
    ConditionBuilder search();
}
