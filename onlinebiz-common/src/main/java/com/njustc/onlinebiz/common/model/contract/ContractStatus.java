package com.njustc.onlinebiz.common.model.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 合同当前的状态
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractStatus {

    // 合同所处的阶段
    private ContractStage stage;

    // 阶段附加消息
    private String message;

}
