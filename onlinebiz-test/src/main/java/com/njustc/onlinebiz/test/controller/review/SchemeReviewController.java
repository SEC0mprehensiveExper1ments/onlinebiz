package com.njustc.onlinebiz.test.controller.review;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.review.SchemeReview;
import com.njustc.onlinebiz.test.service.review.SchemeReviewService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 方案审查控制器
 *
 */
@RestController
@RequestMapping("/api")
public class SchemeReviewController {
    private final SchemeReviewService schemeReviewService;

    public SchemeReviewController(SchemeReviewService schemeReviewService) {
        this.schemeReviewService = schemeReviewService;
    }

    // 创建接口为测试项目内部调用

    /**
     * 获取检查表详情
     *
     * @param schemeReviewId 方案审查id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link SchemeReview}
     */
    @GetMapping("/review/scheme/{schemeReviewId}")
    public SchemeReview getSchemeReview(
            @PathVariable("schemeReviewId") String schemeReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        return schemeReviewService.findSchemeReview(schemeReviewId, userId, userRole);
    }

    /**
     * 更新测试方案检查表内容
     *
     * @param schemeReviewId 方案审查id
     * @param userId 用户id
     * @param userRole 用户角色
     * @param schemeReview 方案评审
     */
    @PostMapping("/review/scheme/{schemeReviewId}")
    public void updateSchemeReview(
            @PathVariable("schemeReviewId") String schemeReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody SchemeReview schemeReview
    ) {
        schemeReviewService.updateSchemeReview(schemeReviewId, schemeReview, userId, userRole);
    }

    /**
     * 上传测试方案评审表扫描件
     *
     * @param schemeReviewId 方案审查id
     * @param userId 用户id
     * @param userRole 用户角色
     * @param scannedCopy 扫描副本
     * @throws IOException ioexception
     */
    @PutMapping("/review/scheme/{schemeReviewId}/upload")
    public void updateScannedCopy(
            @PathVariable("schemeReviewId") String schemeReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestPart("scannedCopy") MultipartFile scannedCopy
    ) throws IOException {
        schemeReviewService.saveScannedCopy(schemeReviewId, scannedCopy, userId, userRole);
    }

    /**
     * 下载测试方案评审表扫描件
     *
     * @param schemeReviewId 方案审查id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link ResponseEntity<Resource>}
     * @throws IOException ioexception
     */
    @GetMapping("/review/scheme/{schemeReviewId}/download")
    public ResponseEntity<Resource> downloadScannedCopy(
            @PathVariable("schemeReviewId") String schemeReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) throws IOException {
        String fileName = schemeReviewService.getScannedCopyFileName(schemeReviewId, userId, userRole);
        Resource resource = schemeReviewService.getScannedCopy(schemeReviewId, userId, userRole);
        return ResponseEntity.ok().header("Content-Disposition","attachment; filename=" + fileName).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

    // 删除测试方案检查表接口为测试项目内部调用
}
