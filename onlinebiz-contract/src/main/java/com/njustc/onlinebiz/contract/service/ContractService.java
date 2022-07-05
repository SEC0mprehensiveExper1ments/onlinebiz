package com.njustc.onlinebiz.contract.service;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.contract.Contract;
import com.njustc.onlinebiz.common.model.contract.ContractStatus;
import com.njustc.onlinebiz.common.model.contract.NonDisclosureAgreement;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 合同管理的服务层接口
 */
public interface ContractService {

    /**
     * 创建一份新合同，只能由负责相应委托的市场部人员或管理员执行此操作。新创建的合同
     * 默认为市场部人员初步填写合同阶段。
     * @param entrustId 合同对应的委托ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * @return 成功返回创建的合同ID，失败返回 null
     */
    String createContract(String entrustId, Long userId, Role userRole);

    /**
     * 查看具体合同内容，只有和该合同有关的人员和管理员可以查看。
     * @param contractId 要查看的合同ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * @return 存在返回该合同对象，不存在返回 null
     */
    Contract findContract(String contractId, Long userId, Role userRole);

    /**
     * 更新合同内容。只能由与此合同相关的客户、市场部人员和管理员可以执行此操作。其中管理员
     * 将更新所有合同内容；客户和市场部人员只更新各自对应的部分内容。
     * @param contractId 要更新的合同ID
     * @param contract 更新后的合同内容
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void updateContract(String contractId, Contract contract, Long userId, Role userRole);

    /**
     * 同意合同内容。只有与此合同相关的客户、市场部人员和管理员可以执行此操作。
     * @param contractId 合同ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void approveContract(String contractId, Long userId, Role userRole);

    /**
     * 拒绝合同内容。只有与此合同相关的客户、市场部人员和管理员可以执行此操作。
     * @param contractId 合同ID
     * @param message 可选的附加说明消息
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void denyContract(String contractId, String message, Long userId, Role userRole);

    /**
     * 更新合同对应的客户ID，只有管理员可以进行此操作
     * @param contractId 要更新的合同ID
     * @param customerId 更新后的客户ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void updateCustomerId(String contractId, Long customerId, Long userId, Role userRole);

    /**
     * 更新合同对应的市场部人员ID，只有管理员可以进行此操作
     * @param contractId 要更新的合同ID
     * @param marketerId 更新后的市场部人员ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void updateMarketerId(String contractId, Long marketerId, Long userId, Role userRole);

    /**
     * 更新合同的状态，只有管理员可以进行此操作
     * @param contractId 要更新的合同ID
     * @param status 更新后的合同状态
     */
    void updateStatus(String contractId, ContractStatus status, Long userId, Role userRole);

    /**
     * 保存合同的扫描件，只有负责此合同的市场部人员和管理员可以执行此操作
     * @param contractId 扫描件对应的合同ID
     * @param scannedCopy 扫描件文件
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * @throws IOException IO异常
     */
    void saveScannedCopy(String contractId, MultipartFile scannedCopy, Long userId, Role userRole) throws IOException;

    /**
     * 获取保存的合同扫描件
     * @param contractId 扫描件对应的合同ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * @throws IOException IO异常
     */
    Resource getScannedCopy(String contractId, Long userId, Role userRole) throws IOException;

    /**
     * 得到扫描复制文件名
     *
     * @param contractId 合同标识
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    String getScannedCopyFileName(String contractId, Long userId, Role userRole);

    /**
     * 更新合同对应的保密协议。只有与此合同相关的人员和管理员可以进行此操作。其中客户和市场部人员
     * 各自更新各自的内容；管理员将更新所有内容。
     * @param contractId 要更新的合同ID
     * @param nonDisclosureAgreement 更新后的保密协议内容
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void updateNonDisclosure(String contractId, NonDisclosureAgreement nonDisclosureAgreement, Long userId, Role userRole);

    /**
     * 删除一份合同。只有管理员可以进行此操作
     * @param contractId 要删除的合同ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void removeContract(String contractId, Long userId, Role userRole);

}
