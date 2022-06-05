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
public class ReportReview {
    // MongoDB 中存放此类型对象的 collection 的名字
    public static final String COLLECTION_NAME = "reportAudit";
    /** 一般不更新的部分 **/
    // 方案评审表 id，由 MongoDB 自动生成
    private String id;
    // 此测试报告对应的方案 id
    private String reportId;
    // 评审表目前的状态
    private ReviewStatus status;
    // 评审表扫描件的文件路径
    private String scannedCopyPath;
    /** 每次会经常更新的部分 **/
    // 软件名称
    private String softwareName;
    // 委托单位
    private String principal;
    // 各项评审结果
    List<ReportReview.ConclusionRow> conclusions;
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
}
