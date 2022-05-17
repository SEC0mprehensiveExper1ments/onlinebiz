package com.njustc.onlinebiz.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Report {
    // MongoDB 中存放此类型对象的 collection 的名字
    public static final String COLLECTION_NAME = "report";

    // 报告 id，由 MongoDB 自动生成
    private String id;

    /* 按照JS007表格字段命名，
       子项先从左往右，从上至下，递增 */
    //    软件名称
    private String softwareName;
    //    版本号
    private String version;
    //    委托单位
    private String principalName;
    //    测试类别
    private String testType;
    //    报告日期
    private Date reportDate;
    //    项目编号
    private String projectId;
    //    样品名称
    private String sampleName;
    //    来样日期
    private Date provideDate;
    //    测试开始日期
    private String testStartDate;
    //    测试结束日期
    private String testEndDate;
    //    样品状态
    private String sampleCondition;
    //    测试依据
    private List<String> testBasis;
    //    样品清单
    private List<String> sampleList;
    //    测试结论
    private String testConclusion;
    //    主测人
    private String mainTester;
    //    测试时间
    private Date testDate;
    //    审核人
    private String auditor;
    //    审核时间
    private String auditDate;
    //    批准人
    private String authorizer;
    //    批准时间
    private String authorizeDate;
    //    委托单位联系方式
    private ReportParty principal;
    //    硬件环境
    private HardwareEnvironment hardwareEnvironment;
    //    软件环境
    private SoftwareEnvironment softwareEnvironment;
    //    网络环境
    private String networkEnvironment;
    //    参考资料
    private String reference;
    //    功能性测试
    private List<TestResult> functionTestResultList;
    //    效率性测试
    private List<TestResult> efficiencyTestResultList;
    //    可移植性测试
    private List<TestResult> portabilityTestResultList;
    //    易用性测试
    private List<TestResult> accessibilityTestResultList;
    //    可靠性测试
    private List<TestResult> reliabilityTestResultList;
    //    可维护性测试
    private List<TestResult> maintainabilityTestResultList;
    //    测试执行记录
    private String testExecutionRecord;
}
