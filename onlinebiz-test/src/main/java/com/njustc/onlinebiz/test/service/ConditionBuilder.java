package com.njustc.onlinebiz.test.service;

import com.njustc.onlinebiz.common.model.Scheme;

import java.util.List;

public interface ConditionBuilder {
    // 根据委托申请 id 查询测试方案
    ConditionBuilder byApplyId(String applyId);

    // 根据委托合同 id 查询测试方案
    ConditionBuilder byContractId(String contractId);

    // 获取查询结果
    List<Scheme> getResult();
}
