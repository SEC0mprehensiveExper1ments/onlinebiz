package com.njustc.onlinebiz.common.model.test.testrecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestRecordStatus {
    //是否需要修改
    private boolean isNeedChange;
    // 附加的说明信息
    private String message;
}
