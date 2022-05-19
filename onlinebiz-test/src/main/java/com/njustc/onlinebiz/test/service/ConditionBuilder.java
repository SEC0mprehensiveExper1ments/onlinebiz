package com.njustc.onlinebiz.test.service;

import com.njustc.onlinebiz.common.model.Scheme;

import java.util.List;

// 用于构造测试方案的搜索条件的接口类，搜索条件之间是 and 关系
public interface ConditionBuilder {
    // 查询任意一方联系人的名字与 contactName 相匹配的测试方案
    ConditionBuilder byContact(String contactName);

    // 查询任意一方单位全称与 companyName 相匹配的测试方案
    ConditionBuilder byCompany(String companyName);

    // 查询项目名称与 projectName 相匹配的测试方案
    ConditionBuilder byProjectName(String projectName);

    // 查询受测软件与 targetSoftware 相匹配的测试方案
    ConditionBuilder byTargetSoftware(String targetSoftware);

    // 根据用户（生成方案的测试人员） id 查询测试方案
    ConditionBuilder byCreatorId(Long creator);

    // 获取查询结果
    List<Scheme> getResult();
}
