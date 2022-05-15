package com.njustc.onlinebiz.software.service;

import com.njustc.onlinebiz.common.model.SoftwareFunctionList;

import java.util.List;

public interface SoftwareService {

    /**
     * 创建一份新的软件功能列表
     * @param principalId 委托方用户ID
     * @return 新创建的合同对象，合同ID已被正确设置
     */
    SoftwareFunctionList createSoftwareFunctionList(Long principalId, SoftwareFunctionList softwareFunctionList);

    // 根据软件功能列表ID查找软件功能列表
    SoftwareFunctionList findSoftwareFunctionListById(String softwareFunctionListId);

    // 根据软件功能列表ID和委托方ID查找软件功能列表
    SoftwareFunctionList findSoftwareFunctionListByIdAndPrincipal(String softwareFunctionListId, Long userId);

    // 更新软件功能列表信息，返回是否成功
    boolean updateSoftwareFunctionList(SoftwareFunctionList softwareFunctionList);

    // 根据软件功能列表ID删除软件功能列表，返回是否成功
    boolean removeSoftwareFunctionList(String softwareFunctionListId);

    // 根据条件查询软件功能列表
    ConditionBuilder search();

}
