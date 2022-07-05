package com.njustc.onlinebiz.contract.controller;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.contract.Contract;
import com.njustc.onlinebiz.common.model.contract.ContractStatus;
import com.njustc.onlinebiz.common.model.contract.NonDisclosureAgreement;
import com.njustc.onlinebiz.contract.service.ContractService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    // 创建合同，返回创建的合同ID
    @PostMapping("/contract")
    public String createContract(
            @RequestParam("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        return contractService.createContract(entrustId, userId, userRole);
    }

    // 获取合同详情
    @GetMapping("/contract/{contractId}")
    public Contract getContract(
            @PathVariable("contractId") String contractId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        return contractService.findContract(contractId, userId, userRole);
    }

    // 更新合同内容
    @PostMapping("/contract/{contractId}")
    public void updateContract(
            @PathVariable("contractId") String contractId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody Contract contract
    ) {
        contractService.updateContract(contractId, contract, userId, userRole);
    }

    // 同意合同
    @PostMapping("/contract/{contractId}/acceptance")
    public void approveContract(
            @PathVariable("contractId") String contractId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        contractService.approveContract(contractId, userId, userRole);
    }

    // 拒绝合同
    @PostMapping("/contract/{contractId}/denial")
    public void denyContract(
            @PathVariable("contractId") String contractId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestParam(value = "message", required = false) String message
    ) {
        contractService.denyContract(contractId, message, userId, userRole);
    }

    // 更新合同的客户
    @PostMapping("/contract/{contractId}/customer")
    public void updateCustomer(
            @PathVariable("contractId") String contractId,
            @RequestParam("customerId") Long customerId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        contractService.updateCustomerId(contractId, customerId, userId, userRole);
    }

    // 更新合同对应的市场部人员
    @PostMapping("/contract/{contractId}/marketer")
    public void updateMarketer(
            @PathVariable("contractId") String contractId,
            @RequestParam("marketerId") Long marketerId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        contractService.updateMarketerId(contractId, marketerId, userId, userRole);
    }

    // 更新合同状态
    @PostMapping("/contract/{contractId}/status")
    public void updateStatus(
            @PathVariable("contractId") String contractId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody ContractStatus status
    ) {
        contractService.updateStatus(contractId, status, userId, userRole);
    }

    // 上传合同扫描件
    @PutMapping("/contract/{contractId}/upload")
    public void updateScannedCopy(
            @PathVariable("contractId") String contractId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestPart("scannedCopy") MultipartFile scannedCopy
    ) throws IOException {
        contractService.saveScannedCopy(contractId, scannedCopy, userId, userRole);
    }

    // 下载合同扫描件
    @GetMapping("/contract/{contractId}/download")
    public ResponseEntity<Resource> downloadScannedCopy(
            @PathVariable("contractId") String contractId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) throws IOException {
        String fileName = contractService.getScannedCopyFileName(contractId, userId, userRole);
        Resource resource = contractService.getScannedCopy(contractId, userId, userRole);
        return ResponseEntity.ok().header("Content-Disposition","attachment; filename=" + fileName).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

    // 更新合同保密协议
    @PostMapping("/contract/{contractId}/non-disclosure")
    public void updateNonDisclosureAgreement(
            @PathVariable("contractId") String contractId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody NonDisclosureAgreement nonDisclosureAgreement
    ) {
        contractService.updateNonDisclosure(contractId, nonDisclosureAgreement, userId, userRole);
    }

    // 删除合同
    @DeleteMapping("/contract/{contractId}")
    public void deleteContract(
            @PathVariable("contractId") String contractId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        contractService.removeContract(contractId, userId, userRole);
    }

}
