package com.njustc.onlinebiz.test.model.scheme;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchemeStatus {
    // 测试方案目前所处的阶段
    private SchemeStage stage;

    // 附加的说明信息
    private String message;
}
