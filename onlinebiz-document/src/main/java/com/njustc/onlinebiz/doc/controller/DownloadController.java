package com.njustc.onlinebiz.doc.controller;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.contract.Contract;
import com.njustc.onlinebiz.common.model.entrust.Entrust;
import com.njustc.onlinebiz.common.model.entrust.EntrustQuote;
import com.njustc.onlinebiz.common.model.test.report.Report;
import com.njustc.onlinebiz.common.model.test.review.EntrustTestReview;
import com.njustc.onlinebiz.common.model.test.review.ReportReview;
import com.njustc.onlinebiz.common.model.test.review.SchemeReview;
import com.njustc.onlinebiz.common.model.test.scheme.Scheme;
import com.njustc.onlinebiz.common.model.test.testcase.Testcase;
import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;
import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList;
import com.njustc.onlinebiz.doc.model.*;
import com.njustc.onlinebiz.doc.model.JS003.JS003;
import com.njustc.onlinebiz.doc.service.*;
import org.springframework.web.bind.annotation.*;

/**
 * 下载控制器，收到请求后，文档服务向对应服务发出字段获取请求，通过DTO对象转换成文档内定义个的类对象
 *
 */
@RestController
@RequestMapping("/api")
public class DownloadController {

    private final DocServiceJS001 docServiceJS001;
    private final DocServiceJS002 docServiceJS002;
    private final DocServiceJS003 docServiceJS003;
    private final DocServiceJS004 docServiceJS004;
    private final DocServiceJS005 docServiceJS005;
    private final DocServiceJS006 docServiceJS006;
    private final DocServiceJS007 docServiceJS007;
    private final DocServiceJS008 docServiceJS008;
    private final DocServiceJS009 docServiceJS009;
    private final DocServiceJS010 docServiceJS010;
    private final DocServiceJS011 docServiceJS011;
    private final DocServiceJS012 docServiceJS012;
    private final DocServiceJS013 docServiceJS013;
    private final DocServiceJS014 docServiceJS014;
    private final DocServiceEntrustQuote docServiceEntrustQuote;
    private final RestRequestService restRequestService;

    public DownloadController(DocServiceJS001 docServiceJS001,
                              DocServiceJS002 docServiceJS002,
                              DocServiceJS003 docServiceJS003,
                              DocServiceJS004 docServiceJS004,
                              DocServiceJS005 docServiceJS005,
                              DocServiceJS006 docServiceJS006,
                              DocServiceJS007 docServiceJS007,
                              DocServiceJS008 docServiceJS008,
                              DocServiceJS009 docServiceJS009,
                              DocServiceJS010 docServiceJS010,
                              DocServiceJS011 docServiceJS011,
                              DocServiceJS012 docServiceJS012,
                              DocServiceJS013 docServiceJS013,
                              DocServiceJS014 docServiceJS014,
                              DocServiceEntrustQuote docServiceEntrustQuote,
                              RestRequestService restRequestService) {
        this.docServiceJS001 = docServiceJS001;
        this.docServiceJS002 = docServiceJS002;
        this.docServiceJS003 = docServiceJS003;
        this.docServiceJS004 = docServiceJS004;
        this.docServiceJS005 = docServiceJS005;
        this.docServiceJS006 = docServiceJS006;
        this.docServiceJS007 = docServiceJS007;
        this.docServiceJS008 = docServiceJS008;
        this.docServiceJS009 = docServiceJS009;
        this.docServiceJS010 = docServiceJS010;
        this.docServiceJS011 = docServiceJS011;
        this.docServiceJS012 = docServiceJS012;
        this.docServiceJS013 = docServiceJS013;
        this.docServiceJS014 = docServiceJS014;
        this.docServiceEntrustQuote = docServiceEntrustQuote;
        this.restRequestService = restRequestService;
    }

    /**
     * JS001 文档下载接口
     *
     * @return {@link String}
     */
    @GetMapping("/doc/JS001")
    public String downloadJS001() {
        return docServiceJS001.fill();
    }

    /**
     * JS002 文档下载接口
     *
     * @param entrustId 委托id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    @GetMapping("/doc/JS002/{entrustId}")
    public String downloadJS002(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {

        Entrust entrust = restRequestService.getEntrustById(entrustId, userId, userRole);
        return docServiceJS002.fill(entrustId.replaceAll("////",""), new JS002(entrust));
    }

    /**
     * JS003 文档下载接口
     *
     * @param entrustId 委托id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    @GetMapping("/doc/JS003/{entrustId}")
    public String downloadJS003(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        Entrust entrust = restRequestService.getEntrustById(entrustId, userId, userRole);
        return docServiceJS003.fill(entrustId.replaceAll("////",""), new JS003(entrust));
    }

    /**
     * JS004 文档下载接口
     *
     * @param contractId 合同标识
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    @GetMapping("/doc/JS004/{contractId}")
    public String downloadJS004(
            @PathVariable("contractId") String contractId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        Contract contract = restRequestService.getContractById(contractId, userId, userRole);
        return docServiceJS004.fill(contractId.replaceAll("////",""), new JS004(contract));
    }

    /**
     * JS005 文档下载接口
     *
     * @param contractId 合同标识
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    @GetMapping("/doc/JS005/{contractId}")
    public String downloadJS005(
            @PathVariable("contractId") String contractId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        Contract contract = restRequestService.getContractById(contractId, userId, userRole);
        return docServiceJS005.fill(contractId.replaceAll("////",""), new JS005(contract));
    }

    /**
     * JS006 文档下载接口
     *
     * @param schemeId 计划id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    @GetMapping("/doc/JS006/{schemeId}")
    public String downloadJS006(
            @PathVariable("schemeId") String schemeId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        Scheme scheme = restRequestService.getScheme(schemeId, userId, userRole);
        JS006 newJson = new JS006(scheme);
        return docServiceJS006.fill(schemeId.replaceAll("////",""), newJson);
    }

    /**
     * JS007 文档下载接口
     *
     * @param reportId 报告id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    @GetMapping("/doc/JS007/{reportId}")
    public String downloadJS007(
            @PathVariable("reportId") String reportId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        Report report = restRequestService.getReport(reportId, userId, userRole);
        JS007 newJson = new JS007(report);
        return docServiceJS007.fill(reportId.replaceAll("////",""), newJson);
    }

    /**
     * JS008 文档下载接口
     *
     * @param testcaseId testcase id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    @GetMapping("/doc/JS008/{testcaseId}")
    public String downloadJS008(
            @PathVariable("testcaseId") String testcaseId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        Testcase testcase = restRequestService.getTestcaseById(testcaseId, userId, userRole);
        return docServiceJS008.fill(testcaseId.replaceAll("////",""), new JS008(testcase));
    }

    /**
     * JS009 文档下载接口
     *
     * @param testRecordId 测试记录id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    @GetMapping("/doc/JS009/{testRecordId}")
    public String downloadJS009(
            @PathVariable("testRecordId") String testRecordId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        TestRecordList testRecordList = restRequestService.getTestRecordListById(testRecordId, userId, userRole);
        return docServiceJS009.fill(testRecordId.replaceAll("////",""), new JS009(testRecordList));
    }

    /**
     * JS010 文档下载接口
     *
     * @param reportReviewId 报告评论id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    @GetMapping("/doc/JS010/{reportReviewId}")
    public String downloadJS010(
            @PathVariable("reportReviewId") String reportReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        ReportReview reportReview = restRequestService.getReportReviewById(reportReviewId, userId, userRole);
        return docServiceJS010.fill(reportReviewId.replaceAll("////",""), new JS010(reportReview));
    }

    /**
     * JS011 文档下载接口
     *
     * @param testIssueId 测试问题id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    @GetMapping("/doc/JS011/{testIssueId}")
    public String downloadJS011(
            @PathVariable("testIssueId") String testIssueId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        TestIssueList testIssueList = restRequestService.getTestIssueListById(testIssueId, userId, userRole);
        return docServiceJS011.fill(testIssueId.replaceAll("////",""), new JS011(testIssueList));
    }

    /**
     * JS012 文档下载接口
     *
     * @param entrustTestReviewId 委托测试评论id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    @GetMapping("/doc/JS012/{entrustTestReviewId}")
    public String downloadJS012(
            @PathVariable("entrustTestReviewId") String entrustTestReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        EntrustTestReview entrustTestReview = restRequestService.getEntrustTestReviewById(entrustTestReviewId, userId, userRole);
        return docServiceJS012.fill(entrustTestReviewId.replaceAll("////",""), new JS012(entrustTestReview));
    }

    /**
     * JS013 文档下载接口
     *
     * @param schemeReviewId 方案审查id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    @GetMapping("/doc/JS013/{schemeReviewId}")
    public String downloadJS013(
            @PathVariable("schemeReviewId") String schemeReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        SchemeReview schemeReview = restRequestService.getSchemeReviewById(schemeReviewId, userId, userRole);
        return docServiceJS013.fill(schemeReviewId.replaceAll("////",""), new JS013(schemeReview));
    }

    /**
     * JS014 文档下载接口
     *
     * @param entrustId 委托id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    @GetMapping("/doc/JS014/{entrustId}")
    public String downloadJS014(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        Entrust entrust = restRequestService.getEntrustById(entrustId, userId, userRole);
        return docServiceJS014.fill(entrustId.replaceAll("////",""), new JS014(entrust));
    }

    /**
     * 报价单 文档下载接口
     *
     * @param entrustId 委托id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    @GetMapping("/doc/entrustQuote/{entrustId}")
    public String downloadEntrustQuote(
            @PathVariable("entrustId") String entrustId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        EntrustQuote entrustQuote = restRequestService.getEntrustQuoteById(entrustId, userId, userRole);
        return docServiceEntrustQuote.fill(entrustId.replaceAll("////",""), new EntrustQuoteDoc(entrustQuote));
    }

}
