package com.njustc.onlinebiz.apply.service;


import com.njustc.onlinebiz.apply.model.Outline;

import java.util.List;

// 用于构造委托申请的搜索条件的接口类，搜索条件之间是 and 关系
public interface ConditionBuilder {

    // 查询委托申请联系人的名字与 contactName 相匹配的申请
    ConditionBuilder byContact(String contactName);

    // 查询委托单位中文全称与 companyName 相匹配的申请
    ConditionBuilder byCompany(String companyName);

    // 查询测试类型与 testType 相匹配的合同
    ConditionBuilder byTestType(String testType);

    // 查询受测软件与 targetSoftware 相匹配的合同
    ConditionBuilder byTargetSoftware(String targetSoftware);

    // 根据委托方用户 id 查询合同
    ConditionBuilder byPrincipalId(Long principalId);

    // 获取查询结果
    List<Outline> getResult();
}
