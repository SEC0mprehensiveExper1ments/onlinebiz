package com.njustc.onlinebiz.common.model.test.review;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SchemeReview {
    // MongoDB 中存放此类型对象的 collection 的名字
    public static final String COLLECTION_NAME = "schemeAudit";
    /** 一般不更新的部分 **/
    // 方案评审表 id，由 MongoDB 自动生成
    private String id;
    // 此测试方案所属的测试项目编号 id
    private String projectId;
    // (手填)测试项目编号
    private String serialNumber;

    // 评审表扫描件的文件路径
    private String scannedCopyPath;
//    // 与评审表相关的质量部人员ID
//    private Long qaId;
//    // 与评审表相关的测试部人员ID
//    private Long testerId;

    /** 每次会经常更新的部分 **/
    // 软件名称
    private String softwareName;
    // 版本号
    private String version;
    // 测试类型
    private String testType;
    // 各项评审结果
    List<ConclusionRow> conclusions;

    /** 评审结果类型的定义 **/
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConclusionRow {
        // 是否通过
        private boolean passed;
        // 不通过的原因
        private String message;
    }


    //    // 书写规范性
//    private SchemeAuditConclusion writingNorm;
//    // 测试环境是否具有典型意义及是否符合用户需求
//    private SchemeAuditConclusion environmentNorm;
//    // 测试内容的完整性，是否覆盖了整个系统
//    private SchemeAuditConclusion testContentNorm;
//    // 测试方法的选取是否合理
//    private SchemeAuditConclusion schemeReasonNorm;
//    // 测试用例能否覆盖问题
//    private SchemeAuditConclusion testCaseCoverNorm;
//    // 输入、输出数据设计合理性
//    private SchemeAuditConclusion ioDataDesignNorm;
//    // 测试时间安排是否合理
//    private SchemeAuditConclusion testTimeReasonNorm;
//    // 测试人力资源安排是否合理
//    private SchemeAuditConclusion testPeopleArrangeNorm;
}
