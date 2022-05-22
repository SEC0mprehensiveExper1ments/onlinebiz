package com.njustc.onlinebiz.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
/*
  测试项目模型，由该模块可以直接索引该测试项目文件
 */
public class Project {
    // MongoDB 中存放此类型对象的 collection 的名字
    public static final String COLLECTION_NAME = "testProject";

    // 项目 id，由 MongoDB 自动生成
    private String id;

    // 市场部生成项目人 id
    private Long creatorId;

    // 对应的测试方案 id
    private String testSchemeId;

    // 对应的测试用例表 id
    private String testcaseListId;

    // 对应的测试记录表 id
    private String testRecordListId;

    // 对应的测试报告 id
    private String testReportId;

    // 对应的测试问题清单 id
    private String testIssueListId;

    // 对应的工作检查表 id
    private String workChecklistId;

    // 对应的测试方案评审表
    private String testSchemeChecklistId;

    // 对应的软件文档评审表
    private String softwareDocumentChecklistId;

    // 该项目的状态
    private boolean isFinished;
}
