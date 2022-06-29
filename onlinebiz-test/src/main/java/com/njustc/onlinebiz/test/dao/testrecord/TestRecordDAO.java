package com.njustc.onlinebiz.test.dao.testrecord;

import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList;

import java.util.List;

/**
 * 测试记录dao层接口
 *
 */
public interface TestRecordDAO {
    /**
     * 插入测试记录列表
     *
     * @param testRecordList 测试记录列表
     * @return {@link TestRecordList}
     */
    TestRecordList insertTestRecordList(TestRecordList testRecordList);

    /**
     * 测试发现记录通过id列表
     *
     * @param testRecordListId 测试记录列表id
     * @return {@link TestRecordList}
     */
    TestRecordList findTestRecordListById(String testRecordListId);

    /**
     * 更新内容
     *
     * @param testRecordListId 测试记录列表id
     * @param content 内容
     * @return boolean
     */
    boolean updateContent(String testRecordListId, List<TestRecordList.TestRecord> content);

    /**
     * 删除测试记录列表
     *
     * @param testRecordListId 测试记录列表id
     * @return boolean
     */
    boolean deleteTestRecordList(String testRecordListId);
}
