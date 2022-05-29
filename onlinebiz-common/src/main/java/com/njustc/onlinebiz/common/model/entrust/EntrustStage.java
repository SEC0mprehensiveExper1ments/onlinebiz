package com.njustc.onlinebiz.common.model.entrust;

// 委托的所有阶段
public enum EntrustStage {

    // 等待分配市场部人员
    WAIT_FOR_MARKETER,

    // 等待分配测试部人员
    WAIT_FOR_TESTER,

    // 市场部审核中
    MARKETER_AUDITING,

    // 测试部审核中
    TESTER_AUDITING,

    // 市场部审核不通过
    MARKETER_DENIED,

    // 测试部审核不通过
    TESTER_DENIED,

    // 委托申请审核通过，市场部生成报价
    AUDITING_PASSED,

    // 客户审核报价
    CUSTOMER_CHECK_QUOTE,

    // 客户拒绝报价
    CUSTOMER_DENY_QUOTE,

    // 客户接收报价
    CUSTOMER_ACCEPT_QUOTE,

    // 委托终止
    TERMINATED

}
