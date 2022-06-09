package com.njustc.onlinebiz.common.model.test.testissue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestIssueList {
    // MongoDB 中存放此类型对象的 collection 的名字
    public static final String COLLECTION_NAME = "testIssue";

    // 软件测试问题清单 id，由 MongoDB 自动生成
    private String id;

    // 此测试方案对应的委托 id
    private String entrustId;

    private String projectId;

    //所有软件测试问题
    private List<TestIssue> testIssues;

    //一条软件测试问题
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestIssue {
        //此条问题序号
        private String testIssueId;

        //问题（缺陷）简要描述
        private String description;

        //对应需求条目
        private String correspondingRequirement;

        //发现缺陷的初始条件
        private String initialConditions;

        //发现缺陷用例及具体操作路径（要具体）
        private String specificOperation;

        //关联用例
        private String associatedCase;

        //发现时间
        private String findTime;

        //责任人
        private String responsiblePerson;

        //修改建议
        private String suggestion;
    }
}
