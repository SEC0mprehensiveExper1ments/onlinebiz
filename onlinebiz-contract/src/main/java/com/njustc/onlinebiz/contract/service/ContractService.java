package com.njustc.onlinebiz.contract.service;

import com.njustc.onlinebiz.common.model.Contract;
import com.njustc.onlinebiz.common.model.ContractOutline;

import java.util.List;

public interface ContractService {

    /**
     * 创建一份新的合同
     * @param principalId 委托方用户ID
     * @param creatorId 创建者的用户ID，应当是内部员工
     * @param applyId 该合同对应的委托ID
     * @return 新创建的合同对象，合同ID已被正确设置
     */
    Contract createContract(Long principalId, Long creatorId, String applyId);

    // 返回所有合同信息的概要列表
    List<ContractOutline> findAllContracts();

    // 根据合同ID查找合同
    Contract findContractById(String contractId);

    // 根据合同ID和委托方ID查找合同
    Contract findContractByIdAndPrincipal(String contractId, Long userId);

    // 更新合同信息，返回是否成功
    boolean updateContract(Contract contract);

    // 根据合同ID删除合同，返回是否成功
    boolean removeContract(String contractId);

    // 根据条件查询合同
    ConditionBuilder search();

}
