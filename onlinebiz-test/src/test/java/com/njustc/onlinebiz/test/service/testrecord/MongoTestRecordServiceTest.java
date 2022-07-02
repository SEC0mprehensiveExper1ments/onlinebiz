package com.njustc.onlinebiz.test.service.testrecord;

import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList;
import com.njustc.onlinebiz.test.exception.testrecord.TestRecordDAOFailureException;
import com.njustc.onlinebiz.test.exception.testrecord.TestRecordDAOFailureException;
import com.njustc.onlinebiz.test.exception.testrecord.TestRecordInvalidStageException;
import com.njustc.onlinebiz.test.exception.testrecord.TestRecordNotFoundException;
import com.njustc.onlinebiz.test.exception.testrecord.TestRecordPermissionDeniedException;
import org.junit.jupiter.api.Test;
import com.njustc.onlinebiz.common.model.test.project.Project;
import com.njustc.onlinebiz.common.model.test.project.ProjectBaseInfo;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.project.ProjectStatus;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import org.junit.jupiter.api.Assertions;
import com.njustc.onlinebiz.common.model.EntrustDto;
import com.njustc.onlinebiz.test.dao.testrecord.TestRecordDAO;
import com.njustc.onlinebiz.test.service.project.ProjectService;
import com.njustc.onlinebiz.common.model.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

class MongoTestRecordServiceTest {

    private final ProjectDAO projectDAO = mock(ProjectDAO.class);
    private final TestRecordDAO testrecordDAO = mock(TestRecordDAO.class);
    private final MongoTestRecordService testrecordservice = new MongoTestRecordService(testrecordDAO, projectDAO);
    private final ProjectBaseInfo projectbaseinfo = mock(ProjectBaseInfo.class);
    private final ProjectService projectService = mock(ProjectService.class);
    private static final String ENTRUST_SERVICE_URL = "http://onlinebiz-entrust";
    @Test
    void createTestRecordList() {

        TestRecordList TestrecordList = new TestRecordList();
        when(testrecordDAO.insertTestRecordList(any())).thenReturn(TestrecordList);
        //由客户（非合法人员）创建测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.createTestRecordList("P001", "E001", null, 11111L, Role.CUSTOMER));
        //由测试部员工（非合法人员）创建测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.createTestRecordList("P001", "E001", null, 2001L, Role.TESTER));
        //由测试部主管（非合法人员）创建测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.createTestRecordList("P001", "E001", null, 2000L, Role.TESTING_SUPERVISOR));
        //由质量部员工（非合法人员）创建测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.createTestRecordList("P001", "E001", null, 3001L, Role.QA));
        //由质量部主管（非合法人员）创建测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.createTestRecordList("P001", "E001", null, 3000L, Role.QA_SUPERVISOR));
        //由指派的市场部员工/市场部主管（合法人员）创建测试记录表
        Assertions.assertDoesNotThrow(() ->
                testrecordservice.createTestRecordList("P001", "E001", null, 1001L, Role.MARKETER));
        Assertions.assertDoesNotThrow(() ->
                testrecordservice.createTestRecordList("P002", "E002", null, 1002L, Role.MARKETER));
        Assertions.assertDoesNotThrow(() ->
                testrecordservice.createTestRecordList("P001", "E001", null, 1000L, Role.MARKETING_SUPERVISOR));

        /*若非被指派的市场部员工想创建，则会在创建项目时识别出并报错，此处无需再进行检测和处理*/
    }

    @Test
    void findTestRecordList() {

        TestRecordList TestrecordList = new TestRecordList();
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(TestrecordList);
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setMarketerId(1001L);
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());

        //开始测试
        //在SCHEME_REVIEW_UPLOADED阶段（合法阶段）进行查找测试记录表
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        //由客户（非合法人员）查找测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 11111L, Role.CUSTOMER));
        //由市场部员工（非合法人员）查找测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 1001L, Role.MARKETER));
        //由市场部主管（非合法人员）查找测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 1000L, Role.MARKETING_SUPERVISOR));
        //由非指定的测试部员工（非合法人员）查找测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 2066L, Role.TESTER));
        //由非指定的质量部员工（非合法人员）查找测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 3066L, Role.QA));
        //由指派的测试部员工/测试部主管（合法人员）查找测试记录表
        Assertions.assertDoesNotThrow(() ->
                testrecordservice.findTestRecordList("TestRecordListId", 2001L, Role.TESTER));
        Assertions.assertDoesNotThrow(() ->
                testrecordservice.findTestRecordList("TestRecordListId", 2000L, Role.TESTING_SUPERVISOR));
        //由指派的质量部员工（合法人员）查找测试记录表
        Assertions.assertDoesNotThrow(() ->
                testrecordservice.findTestRecordList("TestRecordListId", 3001L, Role.QA));
        Assertions.assertDoesNotThrow(() ->
                testrecordservice.findTestRecordList("TestRecordListId", 3000L, Role.QA_SUPERVISOR));

        //尝试寻找不存在的测试记录表
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(null);
        Assertions.assertThrows(
                TestRecordNotFoundException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                TestRecordNotFoundException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 2001L, Role.TESTER));
        Assertions.assertThrows(
                TestRecordNotFoundException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 3000L, Role.QA_SUPERVISOR));
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(TestrecordList);

        //在REPORT_QA_DENIED阶段（合法阶段）进行查找测试记录表
        project.setStatus(new ProjectStatus(ProjectStage.REPORT_QA_DENIED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(TestrecordList);
        //由客户（非合法人员）查找测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 11111L, Role.CUSTOMER));
        //由市场部员工（非合法人员）查找测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 1001L, Role.MARKETER));
        //由市场部主管（非合法人员）查找测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 1000L, Role.MARKETING_SUPERVISOR));
        //由非指定的测试部员工（非合法人员）查找测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 2066L, Role.TESTER));
        //由非指定的质量部员工（非合法人员）查找测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 3066L, Role.QA));
        //由指派的测试部员工/测试部主管（合法人员）查找测试记录表
        Assertions.assertDoesNotThrow(() ->
                testrecordservice.findTestRecordList("TestRecordListId", 2001L, Role.TESTER));
        Assertions.assertDoesNotThrow(() ->
                testrecordservice.findTestRecordList("TestRecordListId", 2000L, Role.TESTING_SUPERVISOR));
        //由指派的质量部员工（合法人员）查找测试记录表
        Assertions.assertDoesNotThrow(() ->
                testrecordservice.findTestRecordList("TestRecordListId", 3001L, Role.QA));
        Assertions.assertDoesNotThrow(() ->
                testrecordservice.findTestRecordList("TestRecordListId", 3000L, Role.QA_SUPERVISOR));

        //尝试寻找不存在的测试记录表
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(null);
        Assertions.assertThrows(
                TestRecordNotFoundException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                TestRecordNotFoundException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 2001L, Role.TESTER));
        Assertions.assertThrows(
                TestRecordNotFoundException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 3000L, Role.QA_SUPERVISOR));
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(TestrecordList);

        //在WAIT_FOR_QA阶段（不合法阶段）尝试寻找问题清单
        project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(TestrecordList);
        Assertions.assertThrows(
                TestRecordInvalidStageException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 3001L, Role.QA));
        Assertions.assertThrows(
                TestRecordInvalidStageException.class,
                () -> testrecordservice.findTestRecordList("TestRecordListId", 3000L, Role.QA_SUPERVISOR));
    }

    @Test
    void updateTestRecordList() {

        TestRecordList TestrecordList = new TestRecordList();
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(TestrecordList);
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setMarketerId(1001L);
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());

        //开始测试
        //在SCHEME_REVIEW_UPLOADED阶段（合法阶段）尝试修改测试记录表
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        //由客户（非合法人员）修改测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 11111L, Role.CUSTOMER));
        //由市场部员工（非合法人员）修改测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 1001L, Role.MARKETER));
        //由市场部主管（非合法人员）修改测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 1000L, Role.MARKETING_SUPERVISOR));
        //由非指派的测试部员工（非合法人员）修改测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 2066L, Role.TESTER));
        //由质量部员工（非合法人员）修改测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 3001L, Role.QA));
        //由质量部主管（非合法人员）修改测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 3000L, Role.QA_SUPERVISOR));

        //由指派的测试部员工/测试部主管（合法人员）修改测试记录表
        when(testrecordDAO.updateContent(any(), any())).thenReturn(true);
        Assertions.assertDoesNotThrow(() ->
                testrecordservice.updateTestRecordList("TestRecordListId", null, 2001L, Role.TESTER));
        Assertions.assertDoesNotThrow(() ->
                testrecordservice.updateTestRecordList("TestRecordListId", null, 2000L, Role.TESTING_SUPERVISOR));
        when(testrecordDAO.updateContent(any(), any())).thenReturn(false);
        Assertions.assertThrows(
                TestRecordDAOFailureException.class,
                () -> testrecordservice.updateTestRecordList("TestIssueListId", null, 2001L, Role.TESTER));
        when(testrecordDAO.updateContent(any(), any())).thenReturn(false);
        Assertions.assertThrows(
                TestRecordDAOFailureException.class,
                () -> testrecordservice.updateTestRecordList("TestIssueListId", null, 2000L, Role.TESTING_SUPERVISOR));
        when(testrecordDAO.updateContent(any(), any())).thenReturn(true);

        //尝试修改不存在的测试记录表
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(null);
        Assertions.assertThrows(
                TestRecordNotFoundException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 2001L, Role.TESTER));
        Assertions.assertThrows(
                TestRecordNotFoundException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 2000L, Role.TESTING_SUPERVISOR));
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(TestrecordList);

        //在REPORT_QA_DENIED阶段（合法阶段）尝试修改测试记录表
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(TestrecordList);
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        //由客户（非合法人员）修改测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 11111L, Role.CUSTOMER));
        //由市场部员工（非合法人员）修改测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 1001L, Role.MARKETER));
        //由市场部主管（非合法人员）修改测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 1000L, Role.MARKETING_SUPERVISOR));
        //由非指派的测试部员工（非合法人员）修改测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 2066L, Role.TESTER));
        //由质量部员工（非合法人员）修改测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 3001L, Role.QA));
        //由质量部主管（非合法人员）修改测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 3000L, Role.QA_SUPERVISOR));

        //由指派的测试部员工/测试部主管（合法人员）修改测试记录表
        when(testrecordDAO.updateContent(any(), any())).thenReturn(true);
        Assertions.assertDoesNotThrow(() ->
                testrecordservice.updateTestRecordList("TestRecordListId", null, 2001L, Role.TESTER));
        Assertions.assertDoesNotThrow(() ->
                testrecordservice.updateTestRecordList("TestRecordListId", null, 2000L, Role.TESTING_SUPERVISOR));
        when(testrecordDAO.updateContent(any(), any())).thenReturn(false);
        Assertions.assertThrows(
                TestRecordDAOFailureException.class,
                () -> testrecordservice.updateTestRecordList("TestIssueListId", null, 2001L, Role.TESTER));
        when(testrecordDAO.updateContent(any(), any())).thenReturn(false);
        Assertions.assertThrows(
                TestRecordDAOFailureException.class,
                () -> testrecordservice.updateTestRecordList("TestIssueListId", null, 2000L, Role.TESTING_SUPERVISOR));
        when(testrecordDAO.updateContent(any(), any())).thenReturn(true);

        //尝试修改不存在的测试记录表
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(null);
        Assertions.assertThrows(
                TestRecordNotFoundException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 2001L, Role.TESTER));
        Assertions.assertThrows(
                TestRecordNotFoundException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 2000L, Role.TESTING_SUPERVISOR));
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(TestrecordList);
        
        //在REPORT_WAIT_SENT_TO_CUSTOMER阶段（非法阶段）尝试修改测试记录表
        project.setStatus(new ProjectStatus(ProjectStage.REPORT_WAIT_SENT_TO_CUSTOMER, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(TestrecordList);
        Assertions.assertThrows(
                TestRecordInvalidStageException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 2001L, Role.TESTER));
        Assertions.assertThrows(
                TestRecordInvalidStageException.class,
                () -> testrecordservice.updateTestRecordList("TestRecordListId", null, 2000L, Role.TESTING_SUPERVISOR));
    }

    @Test
    void removeTestRecordList() {

        TestRecordList TestrecordList = new TestRecordList();
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(TestrecordList);

        //开始测试
        //在SCHEME_REVIEW_UPLOADED阶段（合法阶段）尝试删除测试记录表
        //由客户（非合法人员）删除测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.removeTestRecordList("TestRecordListId", 11111L, Role.CUSTOMER));
        //由市场部员工（非合法人员）删除测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.removeTestRecordList("TestRecordListId", 1001L, Role.MARKETER));
        //由市场部主管（非合法人员）删除测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.removeTestRecordList("TestRecordListId", 1000L, Role.MARKETING_SUPERVISOR));
        //由测试部员工（非合法人员）删除测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.removeTestRecordList("TestRecordListId", 2001L, Role.TESTER));
        //由测试部主管（非合法人员）删除测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.removeTestRecordList("TestRecordListId", 2000L, Role.TESTING_SUPERVISOR));
        //由质量部员工（非合法人员）删除测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.removeTestRecordList("TestRecordListId", 3001L, Role.QA));
        //由质量部主管（非合法人员）删除测试记录表
        Assertions.assertThrows(
                TestRecordPermissionDeniedException.class,
                () -> testrecordservice.removeTestRecordList("TestRecordListId", 3000L, Role.QA_SUPERVISOR));

        //由超级管理员admin（合法人员）删除测试用表
        when(testrecordDAO.deleteTestRecordList(any())).thenReturn(true);
        Assertions.assertDoesNotThrow(() ->
                testrecordservice.removeTestRecordList("TestRecordListId", 0L, Role.ADMIN));
        when(testrecordDAO.deleteTestRecordList(any())).thenReturn(false);
        Assertions.assertThrows(
                TestRecordDAOFailureException.class,
                () -> testrecordservice.removeTestRecordList("TestRecordListId", 0L, Role.ADMIN));
        when(testrecordDAO.deleteTestRecordList(any())).thenReturn(true);

        //尝试删除不存在的测试记录表
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(null);
        Assertions.assertThrows(
                TestRecordNotFoundException.class,
                () -> testrecordservice.removeTestRecordList("TestRecordListId", 0L, Role.ADMIN));
        when(testrecordDAO.findTestRecordListById(any())).thenReturn(TestrecordList);
    }
}