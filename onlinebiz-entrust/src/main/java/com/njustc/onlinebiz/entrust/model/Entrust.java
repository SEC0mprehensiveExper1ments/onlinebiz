package com.njustc.onlinebiz.entrust.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 代表一份委托
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entrust {

    // 委托ID
    private String id;

    // 提交委托的用户ID
    private Long customerId;

    // 分配的市场部人员ID
    private Long marketerId;

    // 分配的测试部人员ID
    private Long testerId;

    // 委托状态
    private EntrustStatus status;

    // 委托申请内容
    private EntrustContent content;

    // 委托评审结果
    private EntrustReview review;

    // 委托报价
    private EntrustQuote quote;

}
