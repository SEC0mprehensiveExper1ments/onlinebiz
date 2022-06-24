package com.njustc.onlinebiz.test.service.testissue;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.dao.testissue.TestIssueDAO;
import com.njustc.onlinebiz.test.exception.testissue.TestIssueDAOFailureException;
import com.njustc.onlinebiz.test.exception.testissue.TestIssueInvalidStageException;
import com.njustc.onlinebiz.test.exception.testissue.TestIssueNotFoundException;
import com.njustc.onlinebiz.test.exception.testissue.TestIssuePermissionDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MongoTestIssueService implements TestIssueService {
    private final TestIssueDAO testIssueDAO;
    private final ProjectDAO projectDAO;

    public MongoTestIssueService(TestIssueDAO testIssueDAO, ProjectDAO projectDAO) {
        this.testIssueDAO = testIssueDAO;
        this.projectDAO = projectDAO;
    }

    @Override
    public String createTestIssueList(String projectId, String entrustId, List<TestIssueList.TestIssue> testIssues, Long userId, Role userRole) {
        if (!hasAuthorityToCreate(userRole)) {
            throw new TestIssuePermissionDeniedException("无权新建测试问题清单");
        }
        TestIssueList testIssueList = new TestIssueList();
        testIssueList.setProjectId(projectId);
        testIssueList.setEntrustId(entrustId);
        testIssueList.setTestIssues(testIssues);
        String testIssueListId = testIssueDAO.insertTestIssueList(testIssueList).getId();
        return testIssueListId;
    }

    @Override
    public TestIssueList findTestIssueList(String testIssueListId, Long userId, Role userRole) {
        TestIssueList testIssueList = testIssueDAO.findTestIssueListById(testIssueListId);
        if (testIssueList == null) {
            throw new TestIssueNotFoundException("该测试问题清单不存在");
        }
        if (!hasAuthorityToCheck(userId, userRole, testIssueList)) {
            throw new TestIssuePermissionDeniedException("无权查看该测试问题清单");
        }

        ProjectStage projectStage = projectDAO.findProjectById(testIssueList.getProjectId()).getStatus().getStage();
        if (projectStage == ProjectStage.WAIT_FOR_QA || projectStage == ProjectStage.SCHEME_UNFILLED ||
                projectStage == ProjectStage.SCHEME_AUDITING || projectStage == ProjectStage.SCHEME_AUDITING_DENIED) {
            throw new TestIssueInvalidStageException("此阶段不可查看测试问题清单");
        }

        return testIssueList;
    }

    @Override
    public void updateTestIssueList(String testIssueListId, List<TestIssueList.TestIssue> testIssues, Long userId, Role userRole) {
        TestIssueList testIssueList = testIssueDAO.findTestIssueListById(testIssueListId);
        if (testIssueList == null) {
            throw new TestIssueNotFoundException("该测试问题清单不存在");
        }
        if (!hasAuthorityToFill(userId, userRole, testIssueList)) {
            throw new TestIssuePermissionDeniedException("无权查看该测试问题清单");
        }

        ProjectStage projectStage = projectDAO.findProjectById(testIssueList.getProjectId()).getStatus().getStage();
        if (!(projectStage == ProjectStage.SCHEME_REVIEW_UPLOADED || projectStage == ProjectStage.REPORT_QA_DENIED ||
                projectStage == ProjectStage.REPORT_CUSTOMER_REJECT || projectStage == ProjectStage.QA_ALL_REJECTED)) {
            throw new TestIssueInvalidStageException("此阶段不可修改测试问题清单");
        }

        if (!testIssueDAO.updateContent(testIssueListId, testIssues)) {
            throw new TestIssueDAOFailureException("更新测试问题清单失败");
        }
    }

    @Override
    public void removeTestIssueList(String testIssueListId, Long userId, Role userRole) {
        if (userRole != Role.ADMIN) {
            throw new TestIssuePermissionDeniedException("无权删除测试问题清单");
        }
        TestIssueList testIssueList = findTestIssueList(testIssueListId, userId, userRole);
        if (!testIssueDAO.deleteTestIssueList(testIssueList.getId())) {
            throw new TestIssueDAOFailureException("删除测试问题清单失败");
        }
    }

    private boolean hasAuthorityToCreate(Role userRole) {
        /*根据调研情况，只有市场部人员、市场部主管具有新建测试问题清单权限*/
        return userRole == Role.ADMIN || userRole == Role.MARKETER || userRole == Role.MARKETING_SUPERVISOR;
    }

    private boolean hasAuthorityToCheck(Long userId, Role userRole, TestIssueList testIssueList) {
        /*根据调研情况，分配的测试部人员、测试部主管、分配的质量部人员、质量部主管均有权限查阅*/
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.QA_SUPERVISOR || userRole == Role.TESTING_SUPERVISOR) {
            return true;
        }
        Long testerId = projectDAO.findProjectById(testIssueList.getProjectId()).getProjectBaseInfo().getTesterId();
        Long qaId = projectDAO.findProjectById(testIssueList.getProjectId()).getProjectBaseInfo().getQaId();
        if (userRole == Role.TESTER && userId.equals(testerId)) return true;
        if (userRole == Role.QA && userId.equals(qaId)) return true;
        return false;
    }

    private boolean hasAuthorityToFill(Long userId, Role userRole, TestIssueList testIssueList) {
        /*根据调研情况，超级管理员、分配的测试部人员、测试部主管有权限修改*/
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.TESTING_SUPERVISOR) {
            return true;
        }
        Long testerId = projectDAO.findProjectById(testIssueList.getProjectId()).getProjectBaseInfo().getTesterId();
        if (userRole == Role.TESTER && userId.equals(testerId)) return true;
        return false;
    }
}
