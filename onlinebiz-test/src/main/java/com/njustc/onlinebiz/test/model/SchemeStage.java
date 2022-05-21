package com.njustc.onlinebiz.test.model;

public enum SchemeStage {

    // 未填写
    UNFILLED,

    // 已提交，质量部审核中
    QUALITY_AUDITING,

    // 质量部审核通过
    AUDITING_PASSED,

    // 质量部审核不通过
    AUDITING_DENIED
}
