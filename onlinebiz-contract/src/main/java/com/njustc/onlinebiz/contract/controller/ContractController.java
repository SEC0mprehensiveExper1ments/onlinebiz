package com.njustc.onlinebiz.contract.controller;

import com.njustc.onlinebiz.contract.model.Contract;
import com.njustc.onlinebiz.contract.model.Outline;
import com.njustc.onlinebiz.contract.service.MongoContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ContractController {

    private final MongoContractService contractService;

    public ContractController(MongoContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping("/contract")
    public ResponseEntity<Contract> createContract(
            @RequestParam("principalId") Long principalId,
            @RequestParam("userId") Long creatorId
    ) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/contract")
    public ResponseEntity<List<Outline>> getAllContracts() {
        return ResponseEntity.ok().body(contractService.findAllContracts());
    }

    @GetMapping("/contract/{contractId}")
    public ResponseEntity<Contract> getContractById(@PathVariable("contractId") String contractId) {
        Contract contract = contractService.findContractById(contractId);
        return contract == null ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(contract);
    }

    // 根据组合条件查询合同
    @GetMapping("/contract/search")
    public ResponseEntity<List<Outline>> searchContracts(
            @RequestParam(value = "contactName", required = false) String contactName,
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "representativeName", required = false) String representativeName,
            @RequestParam(value = "projectName", required = false) String projectName,
            @RequestParam(value = "targetSoftware", required = false) String targetSoftware,
            @RequestParam(value = "principalId", required = false) Long principalId
    ) {
        try {
            return ResponseEntity.ok().body(
                    contractService.search()
                    .byContact(contactName)
                    .byCompany(companyName)
                    .byAuthorizedRepresentative(representativeName)
                    .byProjectName(projectName)
                    .byTargetSoftware(targetSoftware)
                    .byPrincipalId(principalId)
                    .getResult()
            );
        } catch (Exception e) {
            log.warn("按条件搜索合同失败");
            return ResponseEntity.badRequest().build();
        }
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
