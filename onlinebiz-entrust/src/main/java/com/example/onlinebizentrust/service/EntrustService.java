package com.example.onlinebizentrust.service;

import com.example.onlinebizentrust.model.Entrust;
import com.example.onlinebizentrust.model.Outline;

import java.util.List;

public interface EntrustService {
    /**
     * 创建一份新的委托
     * @param principalId 委托方用户ID
     * @param creatorId 创建者的用户ID，应当是内部员工
     * @param entrustId 该委托对应的委托ID
     * @return 新创建的委托对象，委托ID已被正确设置
     */
    Entrust createEntrust(Long principalId, Long creatorId, String entrustId);

    // 返回所有委托信息的概要列表
    List<Outline> findAllEntrusts();

    // 根据委托ID查找委托
    Entrust findEntrustById(String entrustId);

    // 根据委托ID和委托方ID查找委托
    Entrust findEntrustByIdAndPrincipal(String entrustId, Long userId);

    // 更新委托信息，返回是否成功
    boolean updateEntrust(Entrust entrust);

    // 根据委托ID删除委托，返回是否成功
    boolean removeEntrust(String entrustId);

    // 根据条件查询委托
    ConditionBuilder search();
}
