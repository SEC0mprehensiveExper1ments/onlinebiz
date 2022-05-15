package com.njustc.onlinebiz.software.service;

import java.util.List;

// 用于构造合同的搜索条件的接口类，搜索条件之间是 and 关系
public interface ConditionBuilder {
    //查询委托测试软件名称与 softwareName 相匹配的软件功能列表
    ConditionBuilder bySoftwareName(String softwareName);

    // 根据委托方用户 id 查询软件功能列表
    ConditionBuilder byPrincipalId(Long principalId);
}
