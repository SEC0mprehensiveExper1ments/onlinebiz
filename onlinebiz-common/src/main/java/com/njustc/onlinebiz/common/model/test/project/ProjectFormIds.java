package com.njustc.onlinebiz.common.model.test.project;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProjectFormIds {
    /** 测试项目包含的各种表格 */
    // 对应的测试方案 id (006)
    private String testSchemeId;
    // 对应的测试报告 id (007)
    private String testReportId;
    // 对应的测试用例表 id (008)
    private String testcaseListId;
    // 对应的测试记录表 id (009)
    private String testRecordListId;
    // 对应的测试报告检查表 id (010)
    private String testReportCecklistId;
    // 对应的测试问题清单 id (011)
    private String testIssueListId;
    // 对应的工作检查表 id (012)
    private String workChecklistId;
    // 对应的测试方案评审表 (013)
    private String testSchemeChecklistId;

}
