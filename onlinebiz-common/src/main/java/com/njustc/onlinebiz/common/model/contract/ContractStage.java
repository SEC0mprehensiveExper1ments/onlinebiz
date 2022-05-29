package com.njustc.onlinebiz.common.model.contract;

// 合同的各个阶段
public enum ContractStage {

    // 市场部人员生成合同
    FILL_CONTRACT,

    // 客户检查合同
    CUSTOMER_CHECKING,

    // 客户拒绝合同内容
    CUSTOMER_DENY,

    // 客户接收合同内容，并填写相关字段
    CUSTOMER_ACCEPT,

    // 市场部人员审核
    MARKETER_CHECKING,

    // 市场部人员拒绝合同内容
    MARKETER_DENY,

    // 市场部人员评审通过，进入线下签字阶段
    MARKETER_ACCEPT,

    // 合同已封存留档，项目进行中
    COPY_SAVED

}
