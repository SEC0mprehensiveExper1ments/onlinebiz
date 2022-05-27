package com.njustc.onlinebiz.test.controller.review;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.review.ReportReview;
import com.njustc.onlinebiz.test.service.review.ReportReviewService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReportReviewController {
    private final ReportReviewService reportReviewService;

    public ReportReviewController(ReportReviewService reportReviewService) {
        this.reportReviewService = reportReviewService;
    }

    //获取检查表详情
    @PostMapping("/review/report/{reportReviewId}")
    public ReportReview getReportReview(
            @PathVariable("reportReviewId") String reportReviewId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole) {
        return reportReviewService.findReportReview(reportReviewId, userId, userRole);
    }
}
