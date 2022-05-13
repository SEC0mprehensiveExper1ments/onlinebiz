package com.njustc.onlinebiz.apply.service;

import com.njustc.onlinebiz.common.model.Apply;
import com.njustc.onlinebiz.common.model.ApplyOutline;

import java.util.List;

public interface ApplyService {

    /**
     * 创建一份新的委托申请
     * @param principalId 委托申请方用户ID
     * @return 新创建的委托申请对象，委托ID已被正确设置
     */
    Apply createApply(Long principalId, Apply apply);

    // 返回所有委托申请信息的概要列表
    List<ApplyOutline> findAllApplys();

    // 根据委托申请ID查找委托申请
    Apply findApplyById(String applyId);

    // 根据委托申请ID和委托方ID查找合同
    Apply findApplyByIdAndPrincipal(String applyId, Long userId);

    // 更新委托申请信息，返回是否成功
    boolean updateApply(Apply apply);

    // 根据委托申请ID删除委托申请，返回是否成功
    boolean removeApply(String applyId);

    // 根据条件查询委托申请
    ConditionBuilder search();

}
