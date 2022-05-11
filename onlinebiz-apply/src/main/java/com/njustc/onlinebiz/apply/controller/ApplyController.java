package com.njustc.onlinebiz.apply.controller;

import com.njustc.onlinebiz.apply.model.Apply;
import com.njustc.onlinebiz.apply.model.Outline;
import com.njustc.onlinebiz.apply.service.ApplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ApplyController {

    private final ApplyService applyService;

    public ApplyController(ApplyService applyService) {
        this.applyService = applyService;
    }


    @PostMapping("/apply")
    public ResponseEntity<Apply> createApply(
            @RequestParam("principalId") Long principalId,
            @RequestParam("auditorId") Long auditorId
    ) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    // 查看系统中所有的合同
    @GetMapping("/apply")
    public ResponseEntity<List<Outline>> getAllApplys() {
        return ResponseEntity.ok().body(applyService.findAllApplys());
    }

    // 查看任意合同的详细信息
    @GetMapping("/apply/{applyId}")
    public ResponseEntity<Apply> getApplyById(@PathVariable("applyId") String applyId) {
        Apply contract = applyService.findApplyById(applyId);
        return contract == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok().body(contract);
    }

    // 查看用户自己的所有合同
    @GetMapping("/apply/individual")
    public ResponseEntity<List<Outline>> getIndividualApplys(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok().body(applyService.search().byPrincipalId(userId).getResult());
    }

    // 查看用户自己的合同详情
    @GetMapping("/apply/individual/{applyId}")
    public ResponseEntity<Apply> getIndividualApplyById(
            @RequestParam("userId") Long userId,
            @PathVariable("applyId") String applyId
    ) {
        Apply contract = applyService.findApplyByIdAndPrincipal(applyId, userId);
        return contract == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok().body(contract);
    }

    // 根据组合条件查询合同
    @GetMapping("/apply/search")
    public ResponseEntity<List<Outline>> searchApplys(
            @RequestParam(value = "contactName", required = false) String contactName,
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "targetSoftware", required = false) String targetSoftware
    ) {
        // 检查条件是否全空
        if (contactName == null && companyName == null && targetSoftware == null) {
            return ResponseEntity.badRequest().build();
        }
        // null 参数会被忽略
        return ResponseEntity.ok().body(
                applyService.search()
                        .byContact(contactName)
                        .byCompany(companyName)
                        .byTargetSoftware(targetSoftware)
                        .getResult()
        );
    }

    @PostMapping("/apply/{applyId}")
    public ResponseEntity<Void> updateApplyById(
            @PathVariable("applyId") String applyId,
            @RequestBody Apply apply
    ) {
        return applyId.equals(apply.getId()) ?
                applyService.updateApply(apply) ?
                        ResponseEntity.ok().build() :
                        ResponseEntity.badRequest().build() :
                ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/applyu/{applyId}")
    public ResponseEntity<Void> deleteApplyById(@PathVariable("applyId") String applyId) {
        return applyService.removeApply(applyId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.badRequest().build();
    }
}
