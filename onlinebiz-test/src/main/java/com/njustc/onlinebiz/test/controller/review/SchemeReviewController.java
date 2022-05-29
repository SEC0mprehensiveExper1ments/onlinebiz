package com.njustc.onlinebiz.test.controller.review;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.review.SchemeReview;
import com.njustc.onlinebiz.test.service.review.SchemeReviewService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class SchemeReviewController {
    private final SchemeReviewService schemeReviewService;

    public SchemeReviewController(SchemeReviewService schemeReviewService) {
        this.schemeReviewService = schemeReviewService;
    }

    // 创建接口为测试项目内部调用

    // 获取检查表详情
    @PostMapping("/review/scheme/{schemeReviewId}")
    public SchemeReview getSchemeReview(
            @PathVariable("schemeReviewId") String schemeReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        return schemeReviewService.findSchemeReview(schemeReviewId, userId, userRole);
    }

    // 更新测试方案检查表内容
    @PostMapping("/review/scheme/{schemeReviewId}")
    public void updateSchemeReview(
            @PathVariable("schemeReviewId") String schemeReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody SchemeReview schemeReview
    ) {
        schemeReviewService.updateSchemeReview(schemeReviewId, schemeReview, userId, userRole);
    }

    // 上传合同扫描件
    @PostMapping("/review/scheme/{schemeReviewId}/upload")
    public void updateScannedCopy(
            @PathVariable("schemeReviewId") String schemeReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestPart("scannedCopy") MultipartFile scannedCopy
    ) throws IOException {
        schemeReviewService.saveScannedCopy(schemeReviewId, scannedCopy, userId, userRole);
    }

    // 下载合同扫描件
    @GetMapping("/review/scheme/{schemeReviewId}/download")
    public ResponseEntity<Resource> downloadScannedCopy(
            @PathVariable("schemeReviewId") String schemeReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) throws IOException {
        Resource resource = schemeReviewService.getScannedCopy(schemeReviewId, userId, userRole);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

    // 删除测试方案检查表接口为测试项目内部调用
}
