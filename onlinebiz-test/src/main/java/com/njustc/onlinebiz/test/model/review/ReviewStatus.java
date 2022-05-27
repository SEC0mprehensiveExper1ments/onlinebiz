package com.njustc.onlinebiz.test.model.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 评审表当前的状态
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewStatus {
    // 评审表所处的阶段
    private ReviewStage stage;
    // 阶段附加消息
    private String message;
}
