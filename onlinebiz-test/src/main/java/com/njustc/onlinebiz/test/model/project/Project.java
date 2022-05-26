package com.njustc.onlinebiz.test.model.project;

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
    public static final String COLLECTION_NAME = "project";

    // 项目 id，由 MongoDB 自动生成
    private String id;

    /** 测试项目的基本信息 **/
    // 测试项目编号
    private String serialNumber;
    // 市场部生成项目人(市场部指定员工) id
    private Long marketId;
    // 测试部指定员工 id
    private Long testId;
    // 质量部指定员工 id
    private Long qaId;

    /** 测试项目包含的各种表格 **/
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

    /** 测试项目状态 **/
    private ProjectStatus status;
}
