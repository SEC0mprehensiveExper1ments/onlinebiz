package com.njustc.onlinebiz.contract.controller;

import com.njustc.onlinebiz.contract.model.Contract;
import com.njustc.onlinebiz.contract.model.Outline;
import com.njustc.onlinebiz.contract.service.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping("/contract")
    public ResponseEntity<Contract> createContract(
            @RequestParam("principalId") Long principalId,
            @RequestParam("userId") Long creatorId,
            @RequestParam("entrustId") String entrustId
    ) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    // 查看系统中所有的合同
    @GetMapping("/contract")
    public ResponseEntity<List<Outline>> getAllContracts() {
        return ResponseEntity.ok().body(contractService.findAllContracts());
    }

    // 查看任意合同的详细信息
    @GetMapping("/contract/{contractId}")
    public ResponseEntity<Contract> getContractById(@PathVariable("contractId") String contractId) {
        Contract contract = contractService.findContractById(contractId);
        return contract == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok().body(contract);
    }

    // 查看用户自己的所有合同
    @GetMapping("/contract/individual")
    public ResponseEntity<List<Outline>> getIndividualContracts(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok().body(contractService.search().byPrincipalId(userId).getResult());
    }

    // 查看用户自己的合同详情
    @GetMapping("/contract/individual/{contractId}")
    public ResponseEntity<Contract> getIndividualContractById(
            @RequestParam("userId") Long userId,
            @PathVariable("contractId") String contractId
    ) {
        Contract contract = contractService.findContractByIdAndPrincipal(contractId, userId);
        return contract == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok().body(contract);
    }

    // 根据组合条件查询合同
    @GetMapping("/contract/search")
    public ResponseEntity<List<Outline>> searchContracts(
            @RequestParam(value = "contactName", required = false) String contactName,
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "representativeName", required = false) String representativeName,
            @RequestParam(value = "projectName", required = false) String projectName,
            @RequestParam(value = "targetSoftware", required = false) String targetSoftware
    ) {
        // 检查条件是否全空
        if (contactName == null && companyName == null && representativeName == null &&
                projectName == null && targetSoftware == null) {
            return ResponseEntity.badRequest().build();
        }
        // null 参数会被忽略
        return ResponseEntity.ok().body(
                contractService.search()
                .byContact(contactName)
                .byCompany(companyName)
                .byAuthorizedRepresentative(representativeName)
                .byProjectName(projectName)
                .byTargetSoftware(targetSoftware)
                .getResult()
        );
    }

    @PostMapping("/contract/{contractId}")
    public ResponseEntity<Void> updateContractById(
            @PathVariable("contractId") String contractId,
            @RequestBody Contract contract
    ) {
        return contractId.equals(contract.getId()) ?
                contractService.updateContract(contract) ?
                    ResponseEntity.ok().build() :
                    ResponseEntity.badRequest().build() :
                ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/contract/{contractId}")
    public ResponseEntity<Void> deleteContractById(@PathVariable("contractId") String contractId) {
        return contractService.removeContract(contractId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.badRequest().build();
    }
}
