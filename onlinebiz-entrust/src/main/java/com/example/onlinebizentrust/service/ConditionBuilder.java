package com.example.onlinebizentrust.service;

import com.example.onlinebizentrust.model.Outline;

import java.util.List;

// 用于构造委托的搜索条件的接口类，搜索条件之间是 and 关系
public interface ConditionBuilder {
    // 查询任意一方联系人的名字与 contactName 相匹配的委托
    ConditionBuilder byContact(String contactName);

    // 查询任意一方单位全称与 companyName 相匹配的委托
    ConditionBuilder byCompany(String companyName);

    // 查询任意一方授权代表与 representativeName 相匹配的委托
    ConditionBuilder byAuthorizedRepresentative(String representativeName);

    // 查询项目名称与 projectName 相匹配的委托
    ConditionBuilder byProjectName(String projectName);

    // 查询受测软件与 targetSoftware 相匹配的委托
    ConditionBuilder byTargetSoftware(String targetSoftware);

    // 根据委托方用户 id 查询委托
    ConditionBuilder byPrincipalId(Long principalId);

    // 获取查询结果
    List<Outline> getResult();
}
