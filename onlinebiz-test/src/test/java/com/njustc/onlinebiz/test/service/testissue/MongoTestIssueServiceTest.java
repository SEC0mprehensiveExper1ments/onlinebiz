package com.njustc.onlinebiz.test.service.testissue;

import com.njustc.onlinebiz.common.model.test.project.Project;
import com.njustc.onlinebiz.common.model.test.project.ProjectBaseInfo;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.project.ProjectStatus;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.exception.testissue.TestIssueDAOFailureException;
import com.njustc.onlinebiz.test.exception.testissue.TestIssueDAOFailureException;
import com.njustc.onlinebiz.test.exception.testissue.TestIssueInvalidStageException;
import com.njustc.onlinebiz.test.exception.testissue.TestIssueNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.njustc.onlinebiz.common.model.EntrustDto;
import com.njustc.onlinebiz.test.dao.testissue.TestIssueDAO;
import com.njustc.onlinebiz.test.service.project.ProjectService;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.exception.testissue.TestIssuePermissionDeniedException;
import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.mock;

class MongoTestIssueServiceTest {

    private final ProjectDAO projectDAO = mock(ProjectDAO.class);
    private final TestIssueDAO testissueDAO = mock(TestIssueDAO.class);
    private final MongoTestIssueService testissueservice = new MongoTestIssueService(testissueDAO, projectDAO);
    private final ProjectBaseInfo projectbaseinfo = mock(ProjectBaseInfo.class);
    private final ProjectService projectService = mock(ProjectService.class);
    private static final String ENTRUST_SERVICE_URL = "http://onlinebiz-entrust";

    @Test
    void createTestIssueList() {

        TestIssueList TestissueList = new TestIssueList();
        when(testissueDAO.insertTestIssueList(any())).thenReturn(TestissueList);

        //开始测试
        //由客户（非合法人员）创建测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.createTestIssueList("P001", "E001", null, 11111L, Role.CUSTOMER));
        //由测试部员工（非合法人员）创建测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.createTestIssueList("P001", "E001", null, 2001L, Role.TESTER));
        //由测试部主管（非合法人员）创建测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.createTestIssueList("P001", "E001", null, 2000L, Role.TESTING_SUPERVISOR));
        //由质量部员工（非合法人员）创建测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.createTestIssueList("P001", "E001", null, 3001L, Role.QA));
        //由质量部主管（非合法人员）创建测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.createTestIssueList("P001", "E001", null, 3000L, Role.QA_SUPERVISOR));
        //由指派的市场部员工/市场部主管（合法人员）创建测试问题清单
        Assertions.assertDoesNotThrow(() ->
                testissueservice.createTestIssueList("P001", "E001", null, 1001L, Role.MARKETER));
        Assertions.assertDoesNotThrow(() ->
                testissueservice.createTestIssueList("P002", "E002", null, 1002L, Role.MARKETER));
        Assertions.assertDoesNotThrow(() ->
                testissueservice.createTestIssueList("P001", "E001", null, 1000L, Role.MARKETING_SUPERVISOR));

        /*若非被指派的市场部员工想创建，则会在创建项目时识别出并报错，此处无需再进行检测和处理*/
    }

    @Test
    void findTestIssueList() {

        TestIssueList TestissueList = new TestIssueList();
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        when(testissueDAO.findTestIssueListById(any())).thenReturn(TestissueList);
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setMarketerId(1001L);
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());

        //开始测试
        //在SCHEME_REVIEW_UPLOADED阶段（合法阶段）查找测试问题清单
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        //由客户（非合法人员）查找测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 11111L, Role.CUSTOMER));
        //由市场部员工（非合法人员）查找测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 1001L, Role.MARKETER));
        //由市场部主管（非合法人员）查找测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 1000L, Role.MARKETING_SUPERVISOR));
        //由非指定的测试部员工（非合法人员）查找测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 2066L, Role.TESTER));
        //由非指定的质量部员工（非合法人员）查找测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 3066L, Role.QA));

        //由指派的测试部员工/测试部主管（合法人员）查找测试问题清单
        Assertions.assertDoesNotThrow(() ->
                testissueservice.findTestIssueList("TestIssueListId", 2001L, Role.TESTER));
        Assertions.assertDoesNotThrow(() ->
                testissueservice.findTestIssueList("TestIssueListId", 2000L, Role.TESTING_SUPERVISOR));
        //由指派的质量部员工（合法人员）查找测试问题清单
        Assertions.assertDoesNotThrow(() ->
                testissueservice.findTestIssueList("TestIssueListId", 3001L, Role.QA));
        Assertions.assertDoesNotThrow(() ->
                testissueservice.findTestIssueList("TestIssueListId", 3000L, Role.QA_SUPERVISOR));

        //尝试寻找不存在的测试问题清单
        when(testissueDAO.findTestIssueListById(any())).thenReturn(null);
        Assertions.assertThrows(
                TestIssueNotFoundException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                TestIssueNotFoundException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 2001L, Role.TESTER));
        Assertions.assertThrows(
                TestIssueNotFoundException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 3000L, Role.QA_SUPERVISOR));
        when(testissueDAO.findTestIssueListById(any())).thenReturn(TestissueList);

        //在REPORT_QA_DENIED阶段（合法阶段）查找测试问题清单
        when(testissueDAO.findTestIssueListById(any())).thenReturn(TestissueList);
        project.setStatus(new ProjectStatus(ProjectStage.REPORT_QA_DENIED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        //由客户（非合法人员）查找测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 11111L, Role.CUSTOMER));
        //由市场部员工（非合法人员）查找测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 1001L, Role.MARKETER));
        //由市场部主管（非合法人员）查找测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 1000L, Role.MARKETING_SUPERVISOR));
        //由非指定的测试部员工（非合法人员）查找测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 2066L, Role.TESTER));
        //由非指定的质量部员工（非合法人员）查找测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 3066L, Role.QA));

        //由指派的测试部员工/测试部主管（合法人员）查找测试问题清单
        Assertions.assertDoesNotThrow(() ->
                testissueservice.findTestIssueList("TestIssueListId", 2001L, Role.TESTER));
        Assertions.assertDoesNotThrow(() ->
                testissueservice.findTestIssueList("TestIssueListId", 2000L, Role.TESTING_SUPERVISOR));
        //由指派的质量部员工（合法人员）查找测试问题清单
        Assertions.assertDoesNotThrow(() ->
                testissueservice.findTestIssueList("TestIssueListId", 3001L, Role.QA));
        Assertions.assertDoesNotThrow(() ->
                testissueservice.findTestIssueList("TestIssueListId", 3000L, Role.QA_SUPERVISOR));

        //尝试寻找不存在的测试问题清单
        when(testissueDAO.findTestIssueListById(any())).thenReturn(null);
        Assertions.assertThrows(
                TestIssueNotFoundException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                TestIssueNotFoundException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 2001L, Role.TESTER));
        Assertions.assertThrows(
                TestIssueNotFoundException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 3000L, Role.QA_SUPERVISOR));
        when(testissueDAO.findTestIssueListById(any())).thenReturn(TestissueList);

        //在WAIT_FOR_QA（非法阶段）阶段尝试查找测试问题清单
        when(testissueDAO.findTestIssueListById(any())).thenReturn(TestissueList);
        project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        Assertions.assertThrows(
                TestIssueInvalidStageException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 2001L, Role.TESTER));
        Assertions.assertThrows(
                TestIssueInvalidStageException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                TestIssueInvalidStageException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 3001L, Role.QA));
        Assertions.assertThrows(
                TestIssueInvalidStageException.class,
                () -> testissueservice.findTestIssueList("TestIssueListId", 3000L, Role.QA_SUPERVISOR));
    }

    @Test
    void updateTestIssueList() {

        TestIssueList TestissueList = new TestIssueList();
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        when(testissueDAO.findTestIssueListById(any())).thenReturn(TestissueList);
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setMarketerId(1001L);
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());

        //开始测试
        //在SCHEME_REVIEW_UPLOADED阶段（合法阶段）尝试修改测试问题清单
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        //由客户（非合法人员）修改测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 11111L, Role.CUSTOMER));
        //由市场部员工（非合法人员）修改测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 1001L, Role.MARKETER));
        //由市场部主管（非合法人员）修改测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 1000L, Role.MARKETING_SUPERVISOR));
        //由非指派的测试部员工（非合法人员）修改测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 2066L, Role.TESTER));
        //由质量部员工（非合法人员）修改测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 3001L, Role.QA));
        //由质量部主管（非合法人员）修改测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 3000L, Role.QA_SUPERVISOR));

        //由指派的测试部员工/测试部主管（非合法人员）修改测试问题清单
        when(testissueDAO.updateContent(any(), any())).thenReturn(true);
        Assertions.assertDoesNotThrow(() ->
                testissueservice.updateTestIssueList("TestIssueListId", null, 2001L, Role.TESTER));
        Assertions.assertDoesNotThrow(() ->
                testissueservice.updateTestIssueList("TestIssueListId", null, 2000L, Role.TESTING_SUPERVISOR));
        when(testissueDAO.updateContent(any(), any())).thenReturn(false);
        Assertions.assertThrows(
                TestIssueDAOFailureException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 2001L, Role.TESTER));
        when(testissueDAO.updateContent(any(), any())).thenReturn(false);
        Assertions.assertThrows(
                TestIssueDAOFailureException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 2000L, Role.TESTING_SUPERVISOR));
        when(testissueDAO.updateContent(any(), any())).thenReturn(true);

        //尝试修改不存在的测试问题清单
        when(testissueDAO.findTestIssueListById(any())).thenReturn(null);
        Assertions.assertThrows(
                TestIssueNotFoundException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 2001L, Role.TESTER));
        Assertions.assertThrows(
                TestIssueNotFoundException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 2000L, Role.TESTING_SUPERVISOR));
        when(testissueDAO.findTestIssueListById(any())).thenReturn(TestissueList);

        //在REPORT_QA_DENIED阶段（合法阶段）尝试修改测试问题清单
        when(testissueDAO.findTestIssueListById(any())).thenReturn(TestissueList);
        project.setStatus(new ProjectStatus(ProjectStage.REPORT_QA_DENIED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        //由客户（非合法人员）修改测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 11111L, Role.CUSTOMER));
        //由市场部员工（非合法人员）修改测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 1001L, Role.MARKETER));
        //由市场部主管（非合法人员）修改测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 1000L, Role.MARKETING_SUPERVISOR));
        //由非指派的测试部员工（非合法人员）修改测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 2066L, Role.TESTER));
        //由质量部员工（非合法人员）修改测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 3001L, Role.QA));
        //由质量部主管（非合法人员）修改测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 3000L, Role.QA_SUPERVISOR));

        //由指派的测试部员工/测试部主管（合法人员）修改测试问题清单
        when(testissueDAO.updateContent(any(), any())).thenReturn(true);
        Assertions.assertDoesNotThrow(() ->
                testissueservice.updateTestIssueList("TestIssueListId", null, 2001L, Role.TESTER));
        Assertions.assertDoesNotThrow(() ->
                testissueservice.updateTestIssueList("TestIssueListId", null, 2000L, Role.TESTING_SUPERVISOR));
        when(testissueDAO.updateContent(any(), any())).thenReturn(false);
        Assertions.assertThrows(
                TestIssueDAOFailureException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 2001L, Role.TESTER));
        when(testissueDAO.updateContent(any(), any())).thenReturn(false);
        Assertions.assertThrows(
                TestIssueDAOFailureException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 2000L, Role.TESTING_SUPERVISOR));
        when(testissueDAO.updateContent(any(), any())).thenReturn(true);

        //尝试修改不存在的测试问题清单
        when(testissueDAO.findTestIssueListById(any())).thenReturn(null);
        Assertions.assertThrows(
                TestIssueNotFoundException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 2001L, Role.TESTER));
        Assertions.assertThrows(
                TestIssueNotFoundException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 2000L, Role.TESTING_SUPERVISOR));
        when(testissueDAO.findTestIssueListById(any())).thenReturn(TestissueList);

        //在WAIT_FOR_QA（非法阶段）尝试修改测试问题清单
        project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        when(testissueDAO.updateContent(any(), any())).thenReturn(true);
        when(testissueDAO.findTestIssueListById(any())).thenReturn(TestissueList);
        Assertions.assertThrows(
                TestIssueInvalidStageException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 2001L, Role.TESTER));
        Assertions.assertThrows(
                TestIssueInvalidStageException.class,
                () -> testissueservice.updateTestIssueList("TestIssueListId", null, 2000L, Role.TESTING_SUPERVISOR));
    }

    @Test
    void removeTestIssueList() {

        TestIssueList TestissueList = new TestIssueList();
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        when(testissueDAO.findTestIssueListById(any())).thenReturn(TestissueList);

        //由客户（非合法人员）删除测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.removeTestIssueList("TestIssueListId", 11111L, Role.CUSTOMER));
        //由市场部员工（非合法人员）删除测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.removeTestIssueList("TestIssueListId", 1001L, Role.MARKETER));
        //由市场部主管（非合法人员）删除测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.removeTestIssueList("TestIssueListId", 1000L, Role.MARKETING_SUPERVISOR));
        //由测试部员工（非合法人员）删除测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.removeTestIssueList("TestIssueListId", 2001L, Role.TESTER));
        //由测试部主管（非合法人员）删除测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.removeTestIssueList("TestIssueListId", 2000L, Role.TESTING_SUPERVISOR));
        //由质量部员工（非合法人员）删除测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.removeTestIssueList("TestIssueListId", 3001L, Role.QA));
        //由质量部主管（非合法人员）删除测试问题清单
        Assertions.assertThrows(
                TestIssuePermissionDeniedException.class,
                () -> testissueservice.removeTestIssueList("TestIssueListId", 3000L, Role.QA_SUPERVISOR));

        //由超级管理员admin（合法人员）删除测试用表
        when(testissueDAO.deleteTestIssueList(any())).thenReturn(true);
        Assertions.assertDoesNotThrow(() ->
                testissueservice.removeTestIssueList("TestIssueListId", 0L, Role.ADMIN));
        when(testissueDAO.deleteTestIssueList(any())).thenReturn(false);
        Assertions.assertThrows(
                TestIssueDAOFailureException.class,
                () -> testissueservice.removeTestIssueList("TestIssueListId", 0L, Role.ADMIN));
        when(testissueDAO.deleteTestIssueList(any())).thenReturn(true);

        //尝试删除不存在的测试问题清单
        when(testissueDAO.findTestIssueListById(any())).thenReturn(null);
        Assertions.assertThrows(
                TestIssueNotFoundException.class,
                () -> testissueservice.removeTestIssueList("TestIssueListId", 0L, Role.ADMIN));
        when(testissueDAO.findTestIssueListById(any())).thenReturn(TestissueList);
    }
}