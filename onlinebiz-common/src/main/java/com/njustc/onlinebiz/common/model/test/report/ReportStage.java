package com.njustc.onlinebiz.common.model.test.report;

public enum ReportStage {
  // 未全部提交
  UNFINISHED,
  // 已全部提交，待质量部审核
  FINISHED,
  // 质量部审核通过，待客户确认
  QA_PASSED,
  // 质量部审核不通过
  QA_REJECTED,
  // 客户认可
  CUSTOMER_CONFIRM,
  // 客户不认可
  CUSTOMER_REJECT,
}
