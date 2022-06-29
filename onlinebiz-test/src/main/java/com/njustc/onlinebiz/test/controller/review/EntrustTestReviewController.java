package com.njustc.onlinebiz.test.controller.review;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.review.EntrustTestReview;
import com.njustc.onlinebiz.test.service.review.EntrustTestReviewService;
import org.springframework.web.bind.annotation.*;

/**
 * 委托测试检查控制器
 *
 */
@RestController
@RequestMapping("/api")
public class EntrustTestReviewController {
    private final EntrustTestReviewService entrustTestReviewService;

    public EntrustTestReviewController(EntrustTestReviewService entrustTestReviewService) {
        this.entrustTestReviewService = entrustTestReviewService;
    }

    /**
     * 获取检查表详情
     *
     * @param entrustTestReviewId 委托测试评论id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link EntrustTestReview}
     */
    @GetMapping("/review/entrustTest/{entrustTestReviewId}")
    public EntrustTestReview getEntrustTestReview(
            @PathVariable("entrustTestReviewId") String entrustTestReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole) {
        return entrustTestReviewService.findEntrustTestReview(entrustTestReviewId, userId, userRole);
    }

    /**
     * 更新检查表内容
     *
     * @param entrustTestReviewId 委托测试评论id
     * @param userId 用户id
     * @param userRole 用户角色
     * @param entrustTestReview 委托测试评估
     */
    @PostMapping("/review/entrustTest/{entrustTestReviewId}")
    public void updateEntrustTestReview(
            @PathVariable("entrustTestReviewId") String entrustTestReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody EntrustTestReview entrustTestReview
    ) {
        entrustTestReviewService.updateEntrustTestReview(entrustTestReviewId, entrustTestReview, userId, userRole);
    }
}
