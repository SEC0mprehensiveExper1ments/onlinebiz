package com.njustc.onlinebiz.test.controller.review;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.review.SchemeReview;
import com.njustc.onlinebiz.test.service.review.SchemeReviewService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SchemeReviewController {
    private final SchemeReviewService schemeReviewService;

    public SchemeReviewController(SchemeReviewService schemeReviewService) {
        this.schemeReviewService = schemeReviewService;
    }

    // 获取检查表详情
    @PostMapping("/review/scheme/{schemeReviewId}")
    public SchemeReview getSchemeReview(
            @PathVariable("schemeReviewId") String schemeReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        return schemeReviewService.findSchemeReview(schemeReviewId, userId, userRole);
    }
}
