package com.njustc.onlinebiz.test.dao.testrecord;

import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList;

import java.util.List;

public interface TestRecordDAO {
    TestRecordList insertTestRecordList(TestRecordList testRecordList);

    TestRecordList findTestRecordListById(String testRecordListId);

    boolean updateContent(String testRecordListId, List<TestRecordList.TestRecord> content);

    boolean deleteTestRecordList(String testRecordListId);
}
