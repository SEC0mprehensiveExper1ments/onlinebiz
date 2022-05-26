package com.njustc.onlinebiz.test.dao.testissue;

import com.njustc.onlinebiz.test.model.testissue.TestIssueList;
import com.njustc.onlinebiz.test.model.testissue.TestIssueStatus;

import java.util.List;

public interface TestIssueDAO {
    TestIssueList insertTestIssueList(TestIssueList testIssueList);

    TestIssueList findTestIssueListById(String testIssueListId);

    boolean updateContent(String testIssueListId, List<TestIssueList.TestIssue> content);

    boolean updateStatus(String testIssueListId, TestIssueStatus status);

    boolean deleteTestIssueList(String testIssueListId);
}
