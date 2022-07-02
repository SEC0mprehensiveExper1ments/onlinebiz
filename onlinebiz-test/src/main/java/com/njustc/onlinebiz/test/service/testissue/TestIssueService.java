package com.njustc.onlinebiz.test.service.testissue;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;

import java.util.List;

/**
 * 测试问题服务
 *
 */
public interface TestIssueService {

    /**
     * 创建一份测试问题清单
     *
     * @param projectId 项目id
     * @param entrustId 委托id
     * @param testIssues 测试问题
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    String createTestIssueList(String projectId, String entrustId, List<TestIssueList.TestIssue> testIssues, Long userId, Role userRole);

    /**
     * 根据测试问题清单id查找相应测试问题清单
     *
     * @param testIssueListId 测试问题列表id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link TestIssueList}
     */
    TestIssueList findTestIssueList(String testIssueListId, Long userId, Role userRole);

    /**
     * 更新测试问题清单信息
     *
     * @param testIssueListId 测试问题列表id
     * @param testIssues 测试问题
     * @param userId 用户id
     * @param userRole 用户角色
     */
    void updateTestIssueList(String testIssueListId, List<TestIssueList.TestIssue> testIssues, Long userId, Role userRole);

    /**
     * 根据测试问题清单id删除测试问题清单
     *
     * @param testIssueListId 测试问题列表id
     * @param userId 用户id
     * @param userRole 用户角色
     */
    void removeTestIssueList(String testIssueListId, Long userId, Role userRole);
}
