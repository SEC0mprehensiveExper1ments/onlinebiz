package com.njustc.onlinebiz.test.service.testcase;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.testcase.Testcase;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.dao.testcase.TestcaseDAO;
import com.njustc.onlinebiz.test.exception.testcase.TestcaseDAOFailureException;
import com.njustc.onlinebiz.test.exception.testcase.TestcaseInvalidStageException;
import com.njustc.onlinebiz.test.exception.testcase.TestcaseNotFoundException;
import com.njustc.onlinebiz.test.exception.testcase.TestcasePermissionDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MongoTestcaseService implements TestcaseService {
    private final TestcaseDAO testcaseDAO;
    private final ProjectDAO projectDAO;
    public MongoTestcaseService(TestcaseDAO testcaseDAO, ProjectDAO projectDAO) {
        this.testcaseDAO = testcaseDAO;
        this.projectDAO = projectDAO;
    }

    @Override
    public String createTestcaseList(String projectId, String entrustId, List<Testcase.TestcaseList> testcases, Long userId, Role userRole) {
        if (!hasAuthorityToCreate(userRole)) {
            throw new TestcasePermissionDeniedException("无权新建测试用例表");
        }
        Testcase testcase = new Testcase();
        testcase.setProjectId(projectId);
        testcase.setEntrustId(entrustId);
        testcase.setTestcases(testcases);
        String testcaseId = testcaseDAO.insertTestcaseList(testcase).getId();
        return testcaseId;
    }

    @Override
    public Testcase findTestcaseList(String testcaseListId, Long userId, Role userRole) {
        Testcase testcase = testcaseDAO.findTestcaseListById(testcaseListId);
        if (testcase == null) {
            throw new TestcaseNotFoundException("该测试用例表不存在");
        }
        if (!hasAuthorityToCheck(userId, userRole, testcase)) {
            throw new TestcasePermissionDeniedException("无权查看该测试用例表");
        }

        ProjectStage projectStage = projectDAO.findProjectById(testcase.getProjectId()).getStatus().getStage();
        if (projectStage == ProjectStage.WAIT_FOR_QA || projectStage == ProjectStage.SCHEME_UNFILLED ||
                projectStage == ProjectStage.SCHEME_AUDITING || projectStage == ProjectStage.SCHEME_AUDITING_DENIED) {
            throw new TestcaseInvalidStageException("此阶段不可查看测试用例表");
        }

        return testcase;
    }

    @Override
    public void updateTestcaseList(String testcaseListId, List<Testcase.TestcaseList> testcases, Long userId, Role userRole) {
        Testcase testcase = testcaseDAO.findTestcaseListById(testcaseListId);
        if (testcase == null) {
            throw new TestcaseNotFoundException("该测试用例表不存在");
        }
        if (!hasAuthorityToFill(userId, userRole, testcase)) {
            throw new TestcasePermissionDeniedException("无权查看该测试用例表");
        }

        ProjectStage projectStage = projectDAO.findProjectById(testcase.getProjectId()).getStatus().getStage();
        if (!(projectStage == ProjectStage.SCHEME_REVIEW_UPLOADED || projectStage == ProjectStage.REPORT_QA_DENIED ||
                projectStage == ProjectStage.REPORT_CUSTOMER_REJECT || projectStage == ProjectStage.QA_ALL_REJECTED)) {
            throw new TestcaseInvalidStageException("此阶段不可修改测试用例表");
        }

        if (!testcaseDAO.updateContent(testcaseListId, testcases)) {
            throw new TestcaseDAOFailureException("更新测试用例表失败");
        }
    }

    @Override
    public void removeTestcaseList(String testcaseListId, Long userId, Role userRole) {
        if (userRole != Role.ADMIN) {
            throw new TestcasePermissionDeniedException("无权删除测试用例表");
        }
        Testcase testcase = findTestcaseList(testcaseListId, userId, userRole);
        if (!testcaseDAO.deleteTestcaseList(testcase.getId())) {
            throw new TestcaseDAOFailureException("删除测试用例表失败");
        }
    }

    private boolean hasAuthorityToCreate(Role userRole) {
        /*根据调研情况，只有市场部人员、市场部主管具有新建测试用例表权限*/
        return userRole == Role.ADMIN || userRole == Role.MARKETER || userRole == Role.MARKETING_SUPERVISOR;
    }

    private boolean hasAuthorityToCheck(Long userId, Role userRole, Testcase testcase) {
        /*根据调研情况，分配的测试部人员、测试部主管、分配的质量部人员、质量部主管均有权限查阅*/
        if (userId == null || userRole == null) {
            return false;
        }
        else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.QA_SUPERVISOR || userRole == Role.TESTING_SUPERVISOR) {
            return true;
        }
        Long testerId = projectDAO.findProjectById(testcase.getProjectId()).getProjectBaseInfo().getTesterId();
        Long qaId = projectDAO.findProjectById(testcase.getProjectId()).getProjectBaseInfo().getQaId();
        if (userRole == Role.TESTER && userId.equals(testerId)) return true;
        if (userRole == Role.QA && userId.equals(qaId)) return true;
        return false;
    }

    private boolean hasAuthorityToFill(Long userId, Role userRole, Testcase testcase) {
        /*根据调研情况，超级管理员、分配的测试部人员、测试部主管有权限修改*/
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.TESTING_SUPERVISOR) {
            return true;
        }
        Long testerId = projectDAO.findProjectById(testcase.getProjectId()).getProjectBaseInfo().getTesterId();
        if (userRole == Role.TESTER && userId.equals(testerId)) return true;
        return false;
    }

}
