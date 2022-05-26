package com.njustc.onlinebiz.test.dao.testrecord;

import com.njustc.onlinebiz.test.model.testrecord.TestRecordList;
import com.njustc.onlinebiz.test.model.testrecord.TestRecordStatus;

import java.util.List;

public interface TestRecordDAO {
    TestRecordList insertTestRecordList(TestRecordList testRecordList);

    TestRecordList findTestRecordListById(String testRecordListId);

    boolean updateContent(String testRecordListId, List<TestRecordList.TestRecord> content);

    boolean updateStatus(String testRecordListId, TestRecordStatus status);

    boolean deleteTestRecordList(String testRecordListId);
}