package com.njustc.onlinebiz.common.model.test.testrecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestRecordList {
    // MongoDB 中存放此类型对象的 collection 的名字
    public static final String COLLECTION_NAME = "testRecord";

    // 软件测试记录表 id，由 MongoDB 自动生成
    private String id;

    // 此测试方案对应的委托 id
    private String entrustId;

    private String projectId;

    //所有软件测试记录
    private List<TestRecord> testRecords;

    //一条软件测试记录
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestRecord {
        //测试分类
        private String category;

        //此条测试用例id
        private String testcaseId;

        //测试用例设计说明
        private String designInstruction;

        //与本测试用例有关的规约说明
        private String statute;

        //前提条件
        private String prerequisites;

        //测试用例执行过程
        private String executionProcess;

        //预期的结果
        private String expectedResult;

        //测试用例设计者
        private String designer;

        //实际结果
        private String actualResult;

        //是否与预期结果一致
        private String isConsistent;

        //相关的BUG编号
        private String bugId;

        //用例执行者
        private String caseExecutor;

        //执行测试时间
        private String time;

        //确认人
        private String confirmationPerson;
    }
}
