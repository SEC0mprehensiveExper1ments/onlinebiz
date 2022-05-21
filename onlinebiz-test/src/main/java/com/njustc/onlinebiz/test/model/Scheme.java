package com.njustc.onlinebiz.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Scheme {
    // MongoDB 中存放此类型对象的 collection 的名字
    public static final String COLLECTION_NAME = "scheme";

    // 合同 id，由 MongoDB 自动生成
    private String id;

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

    // 额外维护信息

    // 此测试方案对应的创建者 id
    private Long creatorId;

    // 此测试方案对应的委托申请 id
    private String applyId;
}

