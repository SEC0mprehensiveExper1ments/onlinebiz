package com.njustc.onlinebiz.contract.service;

import com.njustc.onlinebiz.contract.model.Contract;
import com.njustc.onlinebiz.contract.model.Outline;

import java.util.List;

// 合同管理的接口类
public interface ContractService {

    // 创建一个新合同，需要从委托单初始化
    Contract createContract(Long principalId, Long creatorId);

    // 查看所有合同
    List<Outline> findAllContracts();

    // 根据 id 查询合同
    Contract findContractById(String contractId);

    // 更新合同信息，成功返回 true，失败返回 false
    boolean updateContract(Contract contract);

    // 根据 id 删除一个合同，成功返回 true, 失败返回 false
    boolean removeContract(String contractId);

}
