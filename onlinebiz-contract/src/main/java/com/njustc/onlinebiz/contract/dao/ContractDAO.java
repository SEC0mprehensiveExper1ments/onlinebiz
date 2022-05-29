package com.njustc.onlinebiz.contract.dao;

import com.njustc.onlinebiz.common.model.contract.Contract;
import com.njustc.onlinebiz.common.model.contract.ContractStatus;
import com.njustc.onlinebiz.common.model.contract.NonDisclosureAgreement;

/**
 * 合同管理的数据访问层接口
 */
public interface ContractDAO {

    /**
     * 保存一份合同
     * @param contract 要保存的合同对象
     * @return 返回保存后的合同对象
     */
    Contract insertContract(Contract contract);

    /**
     * 根据合同ID查找合同
     * @param contractId 合同ID
     * @return 如果存在返回该合同对象，不存在返回 null
     */
    Contract findContractById(String contractId);

    /**
     * 更新合同的内容，此操作只更新合同上要填写的字段
     * @param contractId 要更新的合同ID
     * @param contract 更新后的合同
     * @return 成功返回 true，失败返回 false
     */
    Boolean updateContract(String contractId, Contract contract);

    /**
     * 更新合同的客户ID
     * @param contractId 要更新的合同ID
     * @param customerId 更新后的客户ID
     * @return 成功返回 true，失败返回 false
     */
    Boolean updateCustomerId(String contractId, Long customerId);

    /**
     * 更新此合同对应的市场部人员ID
     * @param contractId 要更新的合同ID
     * @param marketerId 更新后的市场部人员ID
     * @return 成功返回 true，失败返回 false
     */
    Boolean updateMarketerId(String contractId, Long marketerId);

    /**
     * 更新合同扫描件的路径
     * @param contractId 要更新的合同ID
     * @param path 更新后的扫描件路径
     * @return 成功返回 true，失败返回 false
     */
    Boolean updateScannedCopyPath(String contractId, String path);

    /**
     * 更新合同对应的保密协议内容
     * @param contractId 保密协议所属的合同ID
     * @param nonDisclosureAgreement 更新后的保密协议
     * @return 成功返回 true，失败返回 false
     */
    Boolean updateNonDisclosure(String contractId, NonDisclosureAgreement nonDisclosureAgreement);

    /**
     * 更新合同的状态
     * @param contractId 要更新的合同ID
     * @param status 更新后的状态
     * @return 成功返回 true，失败返回 false
     */
    Boolean updateStatus(String contractId, ContractStatus status);

    /**
     * 根据ID删除合同
     * @param contractId 要删除的合同ID
     * @return 成功返回 true，失败返回 false
     */
    Boolean deleteContractById(String contractId);

}
