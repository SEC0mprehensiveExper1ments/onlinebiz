package com.njustc.onlinebiz.test.dao.testissue;

import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;

import java.util.List;

public interface TestIssueDAO {
    TestIssueList insertTestIssueList(TestIssueList testIssueList);

    TestIssueList findTestIssueListById(String testIssueListId);

    boolean updateContent(String testIssueListId, List<TestIssueList.TestIssue> content);

    boolean deleteTestIssueList(String testIssueListId);
}
