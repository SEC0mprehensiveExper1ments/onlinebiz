package com.njustc.onlinebiz.test.service.testrecord;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList;

import java.util.List;

/**
 * 测试记录服务
 *
 */
public interface TestRecordService {

    /**
     * 创建一份测试记录表
     *
     * @param projectId 项目id
     * @param entrustId 委托id
     * @param testRecords 测试记录
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    String createTestRecordList(String projectId, String entrustId, List<TestRecordList.TestRecord> testRecords, Long userId, Role userRole);

    /**
     * 根据测试记录表id查找相应测试记录表
     *
     * @param testRecordListId 测试记录列表id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link TestRecordList}
     */
    TestRecordList findTestRecordList(String testRecordListId, Long userId, Role userRole);

    /**
     * 更新测试记录表信息
     *
     * @param testRecordListId 测试记录列表id
     * @param testRecords 测试记录
     * @param userId 用户id
     * @param userRole 用户角色
     */
    void updateTestRecordList(String testRecordListId, List<TestRecordList.TestRecord> testRecords, Long userId, Role userRole);

    /**
     * 根据测试记录表id删除测试记录表
     *
     * @param testRecordListId 测试记录列表id
     * @param userId 用户id
     * @param userRole 用户角色
     */
    void removeTestRecordList(String testRecordListId, Long userId, Role userRole);
}
