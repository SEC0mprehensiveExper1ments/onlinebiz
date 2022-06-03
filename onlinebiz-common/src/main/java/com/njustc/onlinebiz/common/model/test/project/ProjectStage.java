package com.njustc.onlinebiz.common.model.test.project;

public enum ProjectStage {
  // 等待分配质量人员
  WAIT_FOR_QA,

  // 测试方案未填写
  SCHEME_UNFILLED,

  // 测试方案已提交，质量部可审核
  SCHEME_AUDITING,

  // 测试方案经质量部审核不通过
  SCHEME_AUDITING_DENIED,

  // 测试方案经质量部审核通过，进行相关测试，测试报告未填写
  SCHEME_AUDITING_PASSED,

  // 测试报告已填写，质量部可审核
  REPORT_AUDITING,

  // 测试报告经质量部审核不通过
  REPORT_QA_DENIED,

  // 测试报告经质量部审核通过，待客户确认
  REPORT_QA_PASSED,

  // 测试报告被客户接受，质量部可审计已归档的测试文档
  REPORT_CUSTOMER_CONFIRM,

  // 测试报告被客户不接受
  REPORT_CUSTOMER_REJECT,

  // 质量部审计测试文档不合格
  QA_ALL_REJECTED,

  // 质量部审计测试文档合格，完成
  QA_ALL_PASSED,
}
