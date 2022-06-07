package com.njustc.onlinebiz.common.model.test.testcase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Testcase {
    // MongoDB 中存放此类型对象的 collection 的名字
    public static final String COLLECTION_NAME = "testcase";

    // 测试用例表 id，由 MongoDB 自动生成
    private String id;

    // 此测试方案对应的委托 id
    private String entrustId;

    private String projectId;

    //所有测试用例记录
    private List<TestcaseList> testcases;

    //一条测试用例记录
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestcaseList {
        //测试分类
        private String category;

        //此条测试用例id
        private String testcaseId;

        //测试用例设计说明
        private String designInstruction;

        //与本测试用例有关的规约说明
        private String statute;

        //预期的结果
        private String expectedResult;

        //测试用例设计者
        private String designer;

        //测试时间
        private String time;
    }
}
