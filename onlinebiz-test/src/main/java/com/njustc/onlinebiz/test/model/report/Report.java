package com.njustc.onlinebiz.test.model.report;

import com.njustc.onlinebiz.common.model.PartyDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Report {
  // MongoDB 中存放此类型对象的 collection 的名字
  public static final String COLLECTION_NAME = "testReport";
  // 委托单位联系方式
  PartyDetail clientContact;
  // 报告 id, 由 MongoDB 自动生成
  private String id;
  // 软件名称
  private String softwareName;
  // 版本号
  private String version;
  // 测试类别
  private String testType;
  // 报告日期
  private String reportDate;
  // 项目编号
  private String projectId;
  // 样品名称
  private String sampleName;
  // 样品版本/型号
  private String sampleVersion;
  // 来样日期
  private String sampleDate;
  // 测试开始时间
  private String testStartTime;
  // 测试结束时间
  private String testEndTime;
  // 样品状态
  private String sampleStatus;
  // 测试依据
  private String testBasis;
  // 样品清单
  private String sampleList;
  // 测试结论
  private String testConclusion;
  // 主测人
  private String mainTester;
  // 主测人日期
  private String mainTesterDate;
  // 审核人
  private String auditor;
  // 审核人日期
  private String auditorDate;
  // 批准人
  private String approver;
  // 批准人日期
  private String approverDate;
  // 报告状态
  private ReportStatus status;
}
