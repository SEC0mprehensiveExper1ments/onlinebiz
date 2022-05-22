package com.njustc.onlinebiz.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestcaseStatus {
    //是否需要修改
    private boolean isNeedChange;
    // 附加的说明信息
    private String message;
}
