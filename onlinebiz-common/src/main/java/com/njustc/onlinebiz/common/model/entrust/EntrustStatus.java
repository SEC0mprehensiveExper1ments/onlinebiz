package com.njustc.onlinebiz.common.model.entrust;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrustStatus {

    // 委托目前所处的阶段
    private EntrustStage stage;

    // 附加的说明信息
    private String message;

}
