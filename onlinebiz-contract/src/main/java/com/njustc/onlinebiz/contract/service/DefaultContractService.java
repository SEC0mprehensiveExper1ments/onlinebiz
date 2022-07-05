package com.njustc.onlinebiz.contract.service;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.contract.dao.ContractDAO;
import com.njustc.onlinebiz.contract.exception.*;
import com.njustc.onlinebiz.common.model.contract.Contract;
import com.njustc.onlinebiz.common.model.contract.ContractStage;
import com.njustc.onlinebiz.common.model.contract.ContractStatus;
import com.njustc.onlinebiz.common.model.contract.NonDisclosureAgreement;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class DefaultContractService implements ContractService {

    private static final String ENTRUST_SERVICE = "http://onlinebiz-entrust";

    private static final String SCANNED_COPY_DIR = "/root/contract/";

    private final ContractDAO contractDAO;

    private final RestTemplate restTemplate;

    public DefaultContractService(ContractDAO contractDAO, RestTemplate restTemplate) {
        this.contractDAO = contractDAO;
        this.restTemplate = restTemplate;
    }

    @Override
    public String createContract(String entrustId, Long userId, Role userRole) {
        if (userRole != Role.ADMIN && userRole != Role.MARKETER) {
            throw new ContractPermissionDeniedException("只有负责此委托的市场部人员和管理员可以创建合同");
        }
        // 检查委托ID和执行此操作的人员是否一致，并获取委托的客户ID
        String params = "?userId=" + userId + "&userRole=" + userRole;
        String url = ENTRUST_SERVICE + "/api/entrust/" + entrustId + "/check_consistency_with_contract";
        Long customerId;
        try {
            customerId = restTemplate.getForObject(url + params, Long.class);
        } catch (HttpClientErrorException e) {
            throw new ContractCreateFailureException(e.getResponseBodyAsString());
        }
        Contract contract = new Contract();
        // 执行此操作的用户成为该合同的市场部人员
        contract.setCustomerId(customerId);
        contract.setMarketerId(userId);
        contract.setEntrustId(entrustId);
        contract.setStatus(new ContractStatus(ContractStage.FILL_CONTRACT, null));
        // 获取合同ID
        String contractId = contractDAO.insertContract(contract).getId();
        // 将合同ID注册到委托对象中
        params = "?contractId=" + contractId;
        url = ENTRUST_SERVICE + "/api/entrust/" + entrustId + "/register_contract";
        try {
            restTemplate.postForEntity(url + params, null, Void.class);
        } catch (HttpClientErrorException e) {
            throw new InternalError("注册合同ID失败：" + e.getResponseBodyAsString());
        }
        return contractId;
    }

    @Override
    public Contract findContract(String contractId, Long userId, Role userRole) {
        Contract contract = contractDAO.findContractById(contractId);
        if (contract == null) {
            throw new ContractNotFoundException("该合同不存在：" + contractId);
        } else if (!hasFindAuthority(contract, userId, userRole)) {
            throw new ContractPermissionDeniedException("无权查看该合同");
        }
        return contract;
    }

    private Boolean hasFindAuthority(Contract contract, Long userId, Role userRole) {
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.MARKETER) {
            return userId.equals(contract.getMarketerId());
        } else if (userRole == Role.CUSTOMER) {
            return userId.equals(contract.getCustomerId());
        }
        return false;
    }

    @Override
    public void updateContract(String contractId, Contract contract, Long userId, Role userRole) {
        Contract origin = findContract(contractId, userId, userRole);
        if (!origin.getId().equals(contract.getId())) {
            throw new ContractPermissionDeniedException("合同ID不一致");
        }
        ContractStage currStage = origin.getStatus().getStage();
        // 检查合同阶段和人员权限
        if (currStage == ContractStage.FILL_CONTRACT || currStage == ContractStage.CUSTOMER_DENY) {
            if (userRole != Role.ADMIN && (userRole != Role.MARKETER || !userId.equals(contract.getMarketerId()))) {
                throw new ContractPermissionDeniedException("无权修改此合同内容");
            }
        } else if (currStage == ContractStage.CUSTOMER_ACCEPT || currStage == ContractStage.MARKETER_DENY) {
            if (userRole != Role.ADMIN && (userRole != Role.CUSTOMER || !userId.equals(contract.getCustomerId()))) {
                throw new ContractPermissionDeniedException("无权修改此合同内容");
            }
        } else {
            throw new ContractInvalidStageException("此阶段不能修改合同内容");
        }
        // 根据人员角色更新合同的不同部分
        if (userRole == Role.ADMIN || userRole == Role.MARKETER) {
            updateFromMarketer(contractId, origin, contract);
        }
        if (userRole == Role.ADMIN || userRole == Role.CUSTOMER) {
            updateFromCustomer(contractId, origin, contract);
        }
    }

    private void updateFromMarketer(String contractId, Contract origin, Contract contract) {
        origin.setSerialNumber(contract.getSerialNumber());
        origin.setProjectName(contract.getProjectName());
        origin.setPartyB(contract.getPartyB());
        origin.setSignedAt(contract.getSignedAt());
        origin.setSignedDate(contract.getSignedDate());
        origin.setTargetSoftware(contract.getTargetSoftware());
        origin.setPrice(contract.getPrice());
        origin.setTotalWorkingDays(contract.getTotalWorkingDays());
        origin.setRectificationLimit(contract.getRectificationLimit());
        origin.setRectificationDaysEachTime(contract.getRectificationDaysEachTime());
        // 写入数据库
        if (!contractDAO.updateContract(contractId, origin)) {
            throw new ContractDAOFailureException("更新合同内容失败");
        }
        ContractStage nextStage = ContractStage.CUSTOMER_CHECKING;
        if (!contractDAO.updateStatus(contractId, new ContractStatus(nextStage, null))) {
            throw new ContractDAOFailureException("更新合同状态失败");
        }
    }

    private void updateFromCustomer(String contractId, Contract origin, Contract contract) {
        origin.setPartyA(contract.getPartyA());
        if (!contractDAO.updateContract(contractId, origin)) {
            throw new ContractDAOFailureException("更新合同内容失败");
        }
        ContractStage nextStage = ContractStage.MARKETER_CHECKING;
        if (!contractDAO.updateStatus(contractId, new ContractStatus(nextStage, null))) {
            throw new ContractDAOFailureException("更新合同状态失败");
        }
    }

    @Override
    public void approveContract(String contractId, Long userId, Role userRole) {
        Contract contract = findContract(contractId, userId, userRole);
        ContractStage currStage = contract.getStatus().getStage();
        ContractStage nextStage;
        // 检查合同阶段和人员权限
        if (currStage == ContractStage.CUSTOMER_CHECKING) {
            nextStage = ContractStage.CUSTOMER_ACCEPT;
            if (userRole != Role.ADMIN && (userRole != Role.CUSTOMER || !userId.equals(contract.getCustomerId()))) {
                throw new ContractPermissionDeniedException("无权同意此合同内容");
            }
        } else if (currStage == ContractStage.MARKETER_CHECKING) {
            nextStage = ContractStage.MARKETER_ACCEPT;
            if (userRole != Role.ADMIN && (userRole != Role.MARKETER || !userId.equals(contract.getMarketerId()))) {
                throw new ContractPermissionDeniedException("无权同意此合同内容");
            }
        } else {
            throw new ContractInvalidStageException("此阶段不能同意或否认合同内容");
        }
        // 更新合同状态
        if (!contractDAO.updateStatus(contractId, new ContractStatus(nextStage, null))) {
            throw new ContractDAOFailureException("同意合同失败");
        }
    }

    @Override
    public void denyContract(String contractId, String message, Long userId, Role userRole) {
        Contract contract = findContract(contractId, userId, userRole);
        ContractStage currStage = contract.getStatus().getStage();
        ContractStage nextStage;
        // 检查合同阶段和人员权限
        if (currStage == ContractStage.CUSTOMER_CHECKING) {
            nextStage = ContractStage.CUSTOMER_DENY;
            if (userRole != Role.ADMIN && (userRole != Role.CUSTOMER || !userId.equals(contract.getCustomerId()))) {
                throw new ContractPermissionDeniedException("无权否认此合同");
            }
        } else if (currStage == ContractStage.MARKETER_CHECKING) {
            nextStage = ContractStage.MARKETER_DENY;
            if (userRole != Role.ADMIN && (userRole != Role.MARKETER || !userId.equals(contract.getMarketerId()))) {
                throw new ContractPermissionDeniedException("无权否认此合同");
            }
        } else {
            throw new ContractInvalidStageException("此阶段不能同意或否认合同内容");
        }
        // 更新合同状态
        if (!contractDAO.updateStatus(contractId, new ContractStatus(nextStage, message))) {
            throw new ContractDAOFailureException("拒绝合同失败");
        }
    }

    @Override
    public void updateCustomerId(String contractId, Long customerId, Long userId, Role userRole) {
        Contract contract = findContract(contractId, userId, userRole);
        if (userRole != Role.ADMIN) {
            throw new ContractPermissionDeniedException("无权修改合同客户");
        }
        if (!contractDAO.updateCustomerId(contract.getId(), customerId)) {
            throw new ContractDAOFailureException("更新合同客户失败");
        }
    }

    @Override
    public void updateMarketerId(String contractId, Long marketerId, Long userId, Role userRole) {
        Contract contract = findContract(contractId, userId, userRole);
        if (userRole != Role.ADMIN) {
            throw new ContractPermissionDeniedException("无权修改合同对应的市场部人员");
        }
        if (!contractDAO.updateMarketerId(contract.getId(), marketerId)) {
            throw new ContractDAOFailureException("更新合同对应的市场部人员失败");
        }
    }

    @Override
    public void updateStatus(String contractId, ContractStatus status, Long userId, Role userRole) {
        Contract contract = findContract(contractId, userId, userRole);
        if (userRole != Role.ADMIN) {
            throw new ContractPermissionDeniedException("无权修改合同状态");
        }
        if (!contractDAO.updateStatus(contract.getId(), status)) {
            throw new ContractDAOFailureException("更新合同状态失败");
        }
    }

    @Override
    public void saveScannedCopy(String contractId, MultipartFile scannedCopy, Long userId, Role userRole) throws IOException {
        if (scannedCopy.isEmpty()) {
            throw new ContractPermissionDeniedException("不能上传空的合同扫描件");
        }
        Contract contract = findContract(contractId, userId, userRole);
        ContractStage currStage = contract.getStatus().getStage();
        // 检查阶段
        if (currStage != ContractStage.MARKETER_ACCEPT) {
            throw new ContractInvalidStageException("此阶段不能上传合同扫描件");
        }
        // 检查权限
        if (userRole != Role.ADMIN && (userRole != Role.MARKETER || !userId.equals(contract.getMarketerId()))) {
            throw new ContractPermissionDeniedException("无权上传合同扫描件");
        }
        // 保存合同扫描件到磁盘
        String originalFilename = scannedCopy.getOriginalFilename();
        if (originalFilename == null) {
            throw new ContractPermissionDeniedException("扫描件文件名不能为空");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String path = SCANNED_COPY_DIR + contractId + suffix;
        scannedCopy.transferTo(new File(path.replaceAll("\\\\", "/")));
        // 将路径保存到合同对象中
        if (!contractDAO.updateScannedCopyPath(contractId, path)) {
            throw new ContractDAOFailureException("保存扫描件路径失败");
        }
        // 更新合同状态
        if (contractDAO.updateStatus(contractId, new ContractStatus(ContractStage.COPY_SAVED, null))) {
            return;
        }
        throw new ContractDAOFailureException("更新合同状态失败");
    }

    @Override
    public Resource getScannedCopy(String contractId, Long userId, Role userRole) throws IOException {
        Contract contract = findContract(contractId, userId, userRole);
        ContractStage currStage = contract.getStatus().getStage();
        // 检查阶段
        if (currStage != ContractStage.COPY_SAVED) {
            throw new ContractInvalidStageException("合同扫描件尚未上传");
        }
        // 检查权限
        if (userRole != Role.ADMIN && (userRole != Role.MARKETER || !userId.equals(contract.getMarketerId()))
                && (userRole != Role.CUSTOMER || !userId.equals(contract.getCustomerId()))) {
            throw new ContractPermissionDeniedException("无权下载此扫描件");
        }
        // 从磁盘读取文件
        return new InputStreamResource(new FileInputStream(contract.getScannedCopyPath()));
    }

    @Override
    public String getScannedCopyFileName(String contractId, Long userId, Role userRole) {
        Contract contract = findContract(contractId, userId, userRole);
        ContractStage currStage = contract.getStatus().getStage();
        // 检查阶段
        if (currStage != ContractStage.COPY_SAVED) {
            throw new ContractInvalidStageException("合同扫描件尚未上传");
        }
        // 检查权限
        if (userRole != Role.ADMIN && (userRole != Role.MARKETER || !userId.equals(contract.getMarketerId()))
                && (userRole != Role.CUSTOMER || !userId.equals(contract.getCustomerId()))) {
            throw new ContractPermissionDeniedException("无权下载此扫描件");
        }
        // 获取文件名
        return contract.getScannedCopyPath().substring(contract.getScannedCopyPath().lastIndexOf('/') + 1);
    }

    @Override
    public void updateNonDisclosure(String contractId, NonDisclosureAgreement nonDisclosureAgreement, Long userId, Role userRole) {
        Contract contract = findContract(contractId, userId, userRole);
        ContractStage currStage = contract.getStatus().getStage();
        ContractStage nextStage;
        // 检查当前阶段和人员权限
        if (currStage == ContractStage.FILL_CONTRACT || currStage == ContractStage.CUSTOMER_DENY) {
            nextStage = ContractStage.CUSTOMER_CHECKING;
            if (userRole != Role.ADMIN && (userRole != Role.MARKETER || !userId.equals(contract.getMarketerId()))) {
                throw new ContractPermissionDeniedException("无权修改此保密协议");
            }
        } else if (currStage == ContractStage.CUSTOMER_ACCEPT || currStage == ContractStage.MARKETER_DENY) {
            nextStage = ContractStage.MARKETER_CHECKING;
            if (userRole != Role.ADMIN && (userRole != Role.CUSTOMER || !userId.equals(contract.getCustomerId()))) {
                throw new ContractPermissionDeniedException("无权修改此保密协议");
            }
        } else {
            throw new ContractInvalidStageException("当前阶段不能修改合同保密协议");
        }
        NonDisclosureAgreement agreement = contract.getNonDisclosureAgreement();
        // 根据人员不同更新保密协议的不同部分
        if (userRole == Role.CUSTOMER || userRole == Role.ADMIN) {
            agreement.setPartyAName(nonDisclosureAgreement.getPartyAName());
        }
        if (userRole == Role.MARKETER || userRole == Role.ADMIN) {
            agreement.setPartyBName(nonDisclosureAgreement.getPartyBName());
        }
        // 写入数据库
        if (!contractDAO.updateNonDisclosure(contractId, agreement)) {
            throw new ContractDAOFailureException("更新保密协议失败");
        } else if (!contractDAO.updateStatus(contractId, new ContractStatus(nextStage, null))) {
            throw new ContractDAOFailureException("更新合同状态失败");
        }
    }

    @Override
    public void removeContract(String contractId, Long userId, Role userRole) {
        Contract contract = findContract(contractId, userId, userRole);
        if (userRole != Role.ADMIN) {
            throw new ContractPermissionDeniedException("无权删除此合同");
        }
        if (contractDAO.deleteContractById(contract.getId())) {
            return;
        }
        throw new ContractDAOFailureException("删除合同失败");
    }

}
