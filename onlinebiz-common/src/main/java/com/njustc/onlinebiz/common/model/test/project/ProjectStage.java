package com.njustc.onlinebiz.common.model.test.project;

public enum ProjectStage {
  // 等待分配质量人员（所有表不能改）（下一个：SCHEME_UNFILLED,）
  WAIT_FOR_QA,

  // 测试方案未填写（可填：测试方案）（下一个：SCHEME_AUDITING,）
  SCHEME_UNFILLED,

  // 测试方案已提交，质量部可审核（可填：测试方案评审表，可看：测试方案、测试方案评审表）（下一个：SCHEME_AUDITING_DENIED,或SCHEME_AUDITING_PASSED,）
  SCHEME_AUDITING,

  // 测试方案经质量部审核不通过（可填：测试方案，可看：测试方案、测试方案评审表）（下一个：SCHEME_AUDITING,）
  SCHEME_AUDITING_DENIED,

  // 测试方案经质量部审核通过（无可填，可看：测试方案、测试方案评审表、8 9 11三个电子表格、测试报告）（下一个：SCHEME_REVIEW_UPLOADED,）
  SCHEME_AUDITING_PASSED,

  // 测试方案评审表已上传，进行相关测试，测试报告填写中（可填：8 9 11三个电子表格、测试报告，可看：测试方案、测试方案评审表、8 9
  // 11三个电子表格、测试报告）（下一个：REPORT_AUDITING,，测试报告提交触发）
  SCHEME_REVIEW_UPLOADED,

  // 测试报告已提交，质量部可审核（可填：测试报告评审表，可看：测试方案、测试方案评审表、8 9
  // 11三个电子表格、测试报告、测试报告检查表）（下一个：REPORT_QA_DENIED,或REPORT_QA_PASSED,）
  REPORT_AUDITING,

  // 测试报告经质量部审核不通过（可填：8 9 11三个电子表格、测试报告，可看：测试方案、测试方案评审表、8 9
  // 11三个电子表格、测试报告、测试报告检查表）（下一个：REPORT_AUDITING,，测试报告提交时触发）
  REPORT_QA_DENIED,

  // 测试报告经质量部审核通过，待上传测试报告检查表（无可填，可看：测试方案、测试方案评审表、8 9
  // 11三个电子表格、测试报告、测试报告检查表）（下一个：REPORT_WAIT_SENT_TO_CUSTOMER,）
  REPORT_QA_PASSED,

  // 测试报告检查表已上传，待市场部点击向用户签发报告（无可填，可看：都行）（下一个：REPORT_WAIT_CUSTOMER,）
  REPORT_WAIT_SENT_TO_CUSTOMER,

  // 测试报告已签发，待客户接受（无可填，客户只看测试报告，工作人员填了的都能看）（下一个：REPORT_CUSTOMER_CONFIRM,或REPORT_CUSTOMER_REJECT,）
  REPORT_WAIT_CUSTOMER,

  // 测试报告被客户接受，质量部可审计测试文档（无可填，可看：全部）（下一个：QA_ALL_REJECTED,或QA_ALL_PASSED,）
  REPORT_CUSTOMER_CONFIRM,

  // 测试报告被客户不接受（可填：8 9 11三个电子表格、测试报告，填过都能看）（下一个：REPORT_AUDITING,，测试报告提交时触发）
  REPORT_CUSTOMER_REJECT,

  // 质量部审计测试文档不合格（可填：可填：测试方案、8 9 11三个、测试报告、测试工作检查表，全可看）（下一个：REPORT_CUSTOMER_CONFIRM,，单独的button触发）
  QA_ALL_REJECTED,

  // 质量部审计测试文档合格，完成（无可填，全可看）
  QA_ALL_PASSED,
}
