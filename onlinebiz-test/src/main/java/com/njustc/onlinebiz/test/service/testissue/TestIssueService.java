package com.njustc.onlinebiz.test.service.testissue;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.testissue.TestIssueList;

import java.util.List;

public interface TestIssueService {

    //创建一份测试问题清单
    String createTestIssueList(String entrustId, List<TestIssueList.TestIssue> testIssues, Long userId, Role userRole);

    //根据测试问题清单id查找相应测试问题清单
    TestIssueList findTestIssueList(String testIssueListId, Long userId, Role userRole);

    //更新测试问题清单信息
    void updateTestIssueList(String testIssueListId, List<TestIssueList.TestIssue> testIssues, Long userId, Role userRole);

    //根据测试问题清单id删除测试问题清单
    void removeTestIssueList(String testIssueListId, Long userId, Role userRole);
}
