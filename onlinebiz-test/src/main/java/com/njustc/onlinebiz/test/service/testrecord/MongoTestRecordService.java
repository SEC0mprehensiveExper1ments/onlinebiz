package com.njustc.onlinebiz.test.service.testrecord;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.dao.testrecord.TestRecordDAO;
import com.njustc.onlinebiz.test.exception.testrecord.TestRecordDAOFailureException;
import com.njustc.onlinebiz.test.exception.testrecord.TestRecordInvalidStageException;
import com.njustc.onlinebiz.test.exception.testrecord.TestRecordNotFoundException;
import com.njustc.onlinebiz.test.exception.testrecord.TestRecordPermissionDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MongoTestRecordService implements TestRecordService {
    private final TestRecordDAO testRecordDAO;
    private final ProjectDAO projectDAO;

    public MongoTestRecordService(TestRecordDAO testRecordDAO, ProjectDAO projectDAO) {
        this.testRecordDAO = testRecordDAO;
        this.projectDAO = projectDAO;
    }

    @Override
    public String createTestRecordList(String projectId, String entrustId, List<TestRecordList.TestRecord> testRecords, Long userId, Role userRole) {
        if (!hasAuthorityToCreate(userRole)) {
            throw new TestRecordPermissionDeniedException("无权新建测试记录表");
        }
        TestRecordList testRecordList = new TestRecordList();
        testRecordList.setProjectId(projectId);
        testRecordList.setEntrustId(entrustId);
        testRecordList.setTestRecords(testRecords);
        String testRecordListId = testRecordDAO.insertTestRecordList(testRecordList).getId();
        return testRecordListId;
    }

    @Override
    public TestRecordList findTestRecordList(String testRecordListId, Long userId, Role userRole) {
        TestRecordList testRecordList = testRecordDAO.findTestRecordListById(testRecordListId);
        if (testRecordList == null) {
            throw new TestRecordNotFoundException("该测试记录表不存在");
        }
        if (!hasAuthorityToCheck(userId, userRole, testRecordList)) {
            throw new TestRecordPermissionDeniedException("无权查看该测试记录表");
        }

        ProjectStage projectStage = projectDAO.findProjectById(testRecordList.getProjectId()).getStatus().getStage();
        if (projectStage == ProjectStage.WAIT_FOR_QA || projectStage == ProjectStage.SCHEME_UNFILLED ||
                projectStage == ProjectStage.SCHEME_AUDITING || projectStage == ProjectStage.SCHEME_AUDITING_DENIED) {
            throw new TestRecordInvalidStageException("此阶段不可查看测试记录表");
        }

        return testRecordList;
    }

    @Override
    public void updateTestRecordList(String testRecordListId, List<TestRecordList.TestRecord> testRecords, Long userId, Role userRole) {
        TestRecordList testRecordList = testRecordDAO.findTestRecordListById(testRecordListId);
        if (testRecordList == null) {
            throw new TestRecordNotFoundException("该测试记录表不存在");
        }
        if (!hasAuthorityToFill(userId, userRole, testRecordList)) {
            throw new TestRecordPermissionDeniedException("无权查看该测试记录表");
        }

        ProjectStage projectStage = projectDAO.findProjectById(testRecordList.getProjectId()).getStatus().getStage();
        if (!(projectStage == ProjectStage.SCHEME_REVIEW_UPLOADED || projectStage == ProjectStage.REPORT_QA_DENIED ||
                projectStage == ProjectStage.REPORT_CUSTOMER_REJECT || projectStage == ProjectStage.QA_ALL_REJECTED)) {
            throw new TestRecordInvalidStageException("此阶段不可修改测试记录表");
        }

        if (!testRecordDAO.updateContent(testRecordListId, testRecords)) {
            throw new TestRecordDAOFailureException("更新测试记录表失败");
        }
    }

    @Override
    public void removeTestRecordList(String testRecordListId, Long userId, Role userRole) {
        if (userRole != Role.ADMIN) {
            throw new TestRecordPermissionDeniedException("无权删除测试记录表");
        }
        TestRecordList testRecordList = findTestRecordList(testRecordListId, userId, userRole);
        if (!testRecordDAO.deleteTestRecordList(testRecordList.getId())) {
            throw new TestRecordDAOFailureException("删除测试记录表失败");
        }
    }

    private boolean hasAuthorityToCreate(Role userRole) {
        /*根据调研情况，只有市场部人员、市场部主管具有新建测试记录表权限*/
        return userRole == Role.ADMIN || userRole == Role.MARKETER || userRole == Role.MARKETING_SUPERVISOR;
    }

    private boolean hasAuthorityToCheck(Long userId, Role userRole, TestRecordList testRecordList) {
        /*根据调研情况，分配的测试部人员、测试部主管、分配的质量部人员、质量部主管均有权限查阅*/
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.QA_SUPERVISOR || userRole == Role.TESTING_SUPERVISOR) {
            return true;
        }
        Long testerId = projectDAO.findProjectById(testRecordList.getProjectId()).getProjectBaseInfo().getTesterId();
        Long qaId = projectDAO.findProjectById(testRecordList.getProjectId()).getProjectBaseInfo().getQaId();
        if (userRole == Role.TESTER && userId.equals(testerId)) return true;
        if (userRole == Role.QA && userId.equals(qaId)) return true;
        return false;
    }

    private boolean hasAuthorityToFill(Long userId, Role userRole, TestRecordList testRecordList) {
        /*根据调研情况，超级管理员、分配的测试部人员、测试部主管有权限修改*/
        if (userId == null || userRole == null) {
            return false;
        } else if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.TESTING_SUPERVISOR) {
            return true;
        }
        Long testerId = projectDAO.findProjectById(testRecordList.getProjectId()).getProjectBaseInfo().getTesterId();
        if (userRole == Role.TESTER && userId.equals(testerId)) return true;
        return false;
    }
}
