package com.njustc.onlinebiz.test.controller.review;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.review.ReportReview;
import com.njustc.onlinebiz.test.service.review.ReportReviewService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 报告审查控制器
 *
 */
@RestController
@RequestMapping("/api")
public class ReportReviewController {
    private final ReportReviewService reportReviewService;

    public ReportReviewController(ReportReviewService reportReviewService) {
        this.reportReviewService = reportReviewService;
    }

    /**
     * 获取检查表详情
     *
     * @param reportReviewId 报告评论id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link ReportReview}
     */
    @GetMapping("/review/report/{reportReviewId}")
    public ReportReview getReportReview(
            @PathVariable("reportReviewId") String reportReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole) {
        return reportReviewService.findReportReview(reportReviewId, userId, userRole);
    }

    /**
     * 更新检查表内容
     *
     * @param reportReviewId 报告评论id
     * @param userId 用户id
     * @param userRole 用户角色
     * @param reportReview 报告审查
     */
    @PostMapping("/review/report/{reportReviewId}")
    public void updateReportReview(
            @PathVariable("reportReviewId") String reportReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody ReportReview reportReview
    ) {
        reportReviewService.updateReportReview(reportReviewId, reportReview, userId, userRole);
    }

    /**
     * 上传测试报告检查表扫描件
     *
     * @param reportReviewId 报告评论id
     * @param userId 用户id
     * @param userRole 用户角色
     * @param scannedCopy 扫描副本
     * @throws IOException ioexception
     */
    @PutMapping("/review/report/{reportReviewId}/upload")
    public void updateScannedCopy(
            @PathVariable("reportReviewId") String reportReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestPart("scannedCopy") MultipartFile scannedCopy
    ) throws IOException {
        reportReviewService.saveScannedCopy(reportReviewId, scannedCopy, userId, userRole);
    }

    /**
     * 下载测试报告检查表扫描件
     *
     * @param reportReviewId 报告评论id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link ResponseEntity<Resource>}
     * @throws IOException ioexception
     */
    @GetMapping("/review/report/{reportReviewId}/download")
    public ResponseEntity<Resource> downloadScannedCopy(
            @PathVariable("reportReviewId") String reportReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) throws IOException {
        String fileName = reportReviewService.getScannedCopyFileName(reportReviewId, userId, userRole);
        Resource resource = reportReviewService.getScannedCopy(reportReviewId, userId, userRole);
        return ResponseEntity.ok().header("Content-Disposition","attachment; filename=" + fileName).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }
}
