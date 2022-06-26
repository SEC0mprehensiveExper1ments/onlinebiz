package com.njustc.onlinebiz.common.model.test.scheme;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchemeContent {
    //    版本号
    private String version;
    //    文档修改记录
    private List<Modification> modificationList;
    //    标识
    private String logo;
    //    系统概述
    private String systemSummary;
    //    文档概述
    private String documentSummary;
    //    基线
    private String baseline;
    //    硬件
    private String hardware;
    //    软件
    private String software;
    //    其他
    private String otherEnvironment;
    //    参与组织
    private String organization;
    //    人员
    private String participant;
    //    测试级别
    private String testLevel;
    //    测试类别
    private String testType;
    //    一般测试条件
    private String testCondition;
    //    计划执行的测试
    private String testToBeExecuted;
    //    测试用例
    private String testSample;
    //    预计工作日
    private String expectedTime;
    //    制定测试计划
    private Schedule planSchedule;
    //    设计测试计划
    private Schedule designSchedule;
    //    执行测试计划
    private Schedule executeSchedule;
    //    评估测试计划
    private Schedule evaluateSchedule;
    //    需求的可追踪性
    private String traceability;
}
