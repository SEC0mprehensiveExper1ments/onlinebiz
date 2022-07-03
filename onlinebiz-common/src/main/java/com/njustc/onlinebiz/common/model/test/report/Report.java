package com.njustc.onlinebiz.common.model.test.report;

import com.njustc.onlinebiz.common.model.PartyDetail;
import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Report {
  // MongoDB 中存放此类型对象的 collection 的名字
  public static final String COLLECTION_NAME = "testReport";
  // 报告内容
  private ReportContent content;

  private String entrustId;

  // 测试报告 id，由 MongoDB 自动生成
  private String id;

  private String projectId;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Accessors(chain = true)
  public static class ReportContent {
    // 委托单位联系方式
    PartyDetail clientContact;
    // 测试报告编号，由工作人员自行指定
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
    private String projectSerialNumber;
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
    // 硬件类别
    private String hardwareType;
    // 硬件名称
    private String hardwareName;
    // 硬件配置
    private String hardwareConfig;
    // 硬件数量
    private String hardwareNum;
    // 操作系统-软件名称
    private String osSoftwareName;
    // 操作系统-版本
    private String osVersion;
    // 软件环境
    private List<SoftwareEnvironment> softwareEnvironments;
    // 网络环境
    private String networkEnvironment;
    // 测试依据
    private List<String> testBases;
    // 参考资料
    private List<String> referenceMaterials;
    // 功能性测试
    private List<TestContent> functionalTests;
    // 效率测试
    private List<TestContent> efficiencyTests;
    // 可移植性测试
    private List<TestContent> portableTests;
    // 易用性测试
    private List<TestContent> usabilityTests;
    // 可靠性测试
    private List<TestContent> reliabilityTests;
    // 可维护性测试
    private List<TestContent> maintainabilityTests;
    // 测试执行记录
    private List<TestRecordList.TestRecord> testRecords;
  }
}
