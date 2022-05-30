package com.njustc.onlinebiz.entrust.controller;

import com.njustc.onlinebiz.common.model.*;
import com.njustc.onlinebiz.common.model.entrust.*;
import com.njustc.onlinebiz.entrust.service.EntrustService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 委托管理的控制类
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class EntrustController {

    private final EntrustService entrustService;

    public EntrustController(EntrustService entrustService) {
        this.entrustService = entrustService;
    }

    // 创建委托，返回委托ID
    @PostMapping("/entrust")
    public String createEntrust(
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody EntrustContent content
    ) {
        return entrustService.createEntrust(content, userId, userRole);
    }

    // 获取委托概要列表
    @GetMapping("/entrust")
    public PageResult<EntrustOutline> getEntrusts(
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize
    ) {
        return entrustService.findEntrustOutlines(page, pageSize, userId, userRole);
    }

    // 获取委托的完整信息
    @GetMapping("/entrust/{entrustId}")
    public Entrust getEntrust(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        return entrustService.findEntrust(entrustId, userId, userRole);
    }

    // 分配市场部人员
    @PostMapping("/entrust/{entrustId}/marketer")
    public void allocateMarketer(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestParam("marketerId") Long marketerId
    ) {
        entrustService.updateMarketer(entrustId, marketerId, userId, userRole);
    }

    // 分配测试部人员
    @PostMapping("/entrust/{entrustId}/tester")
    public void allocateTester(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestParam("testerId") Long testerId
    ) {
        entrustService.updateTester(entrustId, testerId, userId, userRole);
    }

    // 修改委托申请
    @PostMapping("/entrust/{entrustId}/content")
    public void updateContent(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody EntrustContent content
    ) {
        entrustService.updateContent(entrustId, content, userId, userRole);
    }

    // 拒绝委托申请
    @PostMapping("/entrust/{entrustId}/content/denial")
    public void denyContent(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestParam(value = "message", required = false) String message
    ) {
        entrustService.denyContent(entrustId, message, userId, userRole);
    }

    // 接受委托申请
    @PostMapping("/entrust/{entrustId}/content/acceptance")
    public void acceptContent(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        entrustService.approveContent(entrustId, userId, userRole);
    }

    // 修改委托报价
    @PostMapping("/entrust/{entrustId}/quote")
    public void updateQuote(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody EntrustQuote quote
    ) {
        entrustService.updateQuote(entrustId, quote, userId, userRole);
    }

    // 拒绝委托报价
    @PostMapping("/entrust/{entrustId}/quote/denial")
    public void denyQuote(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestParam(value = "message", required = false) String message
    ) {
        entrustService.denyQuote(entrustId, message, userId, userRole);
    }

    // 接受委托报价
    @PostMapping("/entrust/{entrustId}/quote/acceptance")
    public void acceptQuote(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        entrustService.approveQuote(entrustId, userId, userRole);
    }

    @PostMapping("/entrust/{entrustId}/software_doc_review")
    public void updateSoftwareDocReview(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody SoftwareDocReview softwareDocReview
    ) {
        entrustService.updateSoftwareDocReview(entrustId, softwareDocReview, userId, userRole);
    }

    // 修改委托评审结果
    @PostMapping("/entrust/{entrustId}/review")
    public void updateReview(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody EntrustReview review
    ) {
        entrustService.updateReview(entrustId, review, userId, userRole);
    }

    // 终止委托流程
    @PostMapping("/entrust/{entrustId}/termination")
    public void terminateEntrust(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        entrustService.terminateEntrust(entrustId, userId, userRole);
    }

    // 修改委托的流程状态
    @PostMapping("/entrust/{entrustId}/status")
    public void updateStatus(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody EntrustStatus status
    ) {
        entrustService.updateStatus(entrustId, status, userId, userRole);
    }

    // 修改委托的客户
    @PostMapping("/entrust/{entrustId}/customer")
    public void updateCustomer(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestParam("customerId") Long customerId
    ) {
        entrustService.updateCustomer(entrustId, customerId, userId, userRole);
    }

    // 删除一份委托
    @DeleteMapping("/entrust/{entrustId}")
    public void removeEntrust(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        entrustService.removeEntrust(entrustId, userId, userRole);
    }

    // 用于合同管理部分检查流程和人员一致性
    @GetMapping("/entrust/{entrustId}/check_consistency_with_contract")
    public Long checkConsistencyWithContract(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        return entrustService.checkConsistencyWithContract(entrustId, userId, userRole);
    }

    // 供合同管理调用此接口来注册合同ID
    @PostMapping("/entrust/{entrustId}/register_contract")
    public void registerContract(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("contractId") String contractId
    ) {
        entrustService.registerContract(entrustId, contractId);
    }

    // 内部调用获取测试人员ID
    @GetMapping("/entrust/{entrustId}/get_tester_id")
    public Long getTesterId(@PathVariable("entrustId") String entrustId) {
        return entrustService.getTesterId(entrustId);
    }

    // 内部调用获取委托的数据传输对象
    @GetMapping("/entrust/{entrustId}/get_dto")
    public EntrustDto getEntrustDto(@PathVariable("entrustId") String entrustId) {
        return entrustService.getEntrustDto(entrustId);
    }

    // 内部调用接口，用于注册测试项目ID到委托中
    @PostMapping("/entrust/{entrustId}/register_project")
    public void registerProject(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("projectId") String projectId
    ) {
        entrustService.registerProject(entrustId, projectId);
    }

}
