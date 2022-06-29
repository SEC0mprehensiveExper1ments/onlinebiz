package com.njustc.onlinebiz.test.dao.testissue;

import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;

import java.util.List;

/**
 * 测试问题dao层接口
 *
 */
public interface TestIssueDAO {
    /**
     * 插入测试问题列表
     *
     * @param testIssueList 测试问题列表
     * @return {@link TestIssueList}
     */
    TestIssueList insertTestIssueList(TestIssueList testIssueList);

    /**
     * 测试发现问题通过id列表
     *
     * @param testIssueListId 测试问题列表id
     * @return {@link TestIssueList}
     */
    TestIssueList findTestIssueListById(String testIssueListId);

    /**
     * 更新内容
     *
     * @param testIssueListId 测试问题列表id
     * @param content 内容
     * @return boolean
     */
    boolean updateContent(String testIssueListId, List<TestIssueList.TestIssue> content);

    /**
     *
     *
     * @param testIssueListId 测试问题列表id
     * @return boolean
     */
    boolean deleteTestIssueList(String testIssueListId);
}
