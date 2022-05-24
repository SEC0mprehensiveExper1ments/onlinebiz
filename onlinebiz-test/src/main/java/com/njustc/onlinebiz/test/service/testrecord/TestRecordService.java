package com.njustc.onlinebiz.test.service.testrecord;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.testrecord.TestRecordList;

import java.util.List;

public interface TestRecordService {

    //创建一份测试记录表
    String createTestRecordList(String entrustId, List<TestRecordList.TestRecord> testRecords, Long userId, Role userRole);

    //根据测试记录表id查找相应测试记录表
    TestRecordList findTestRecordList(String testRecordListId, Long userId, Role userRole);

    //更新测试记录表信息
    void updateTestRecordList(String testRecordListId, List<TestRecordList.TestRecord> testRecords, Long userId, Role userRole);

    //根据测试记录表id删除测试记录表
    void removeTestRecordList(String testRecordListId, Long userId, Role userRole);
}
