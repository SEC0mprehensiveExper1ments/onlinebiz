package com.njustc.onlinebiz.test.service.testcase;

import com.njustc.onlinebiz.common.model.test.project.Project;
import com.njustc.onlinebiz.common.model.test.project.ProjectBaseInfo;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.project.ProjectStatus;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.dao.testcase.TestcaseDAO;
import com.njustc.onlinebiz.test.service.project.ProjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.exception.testcase.TestcaseNotFoundException;
import com.njustc.onlinebiz.test.exception.testcase.TestcasePermissionDeniedException;
import com.njustc.onlinebiz.common.model.test.testcase.Testcase;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class MongoTestcaseServiceTest {

    private final ProjectDAO projectDAO = mock(ProjectDAO.class);
    private final TestcaseDAO testcaseDAO = mock(TestcaseDAO.class);
    private final MongoTestcaseService mongotestcaseservice = new MongoTestcaseService(testcaseDAO, projectDAO);
    private final ProjectService projectService = mock(ProjectService.class);
    private static final String ENTRUST_SERVICE_URL = "http://onlinebiz-entrust";
    private static String TestCaseListId1 = null;
    private static String TestCaseListId2 = null;
    private static String TestCaseListId3 = null;
    private static Testcase TestCaseList1 = null;
    private static Testcase TestCaseList2 = null;
    private static Testcase TestCaseList3 = null;

    @Test
    void createTestcaseList() {

        //由客户（非合法人员）创建测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.createTestcaseList("P001", "E001", null, 11111L, Role.CUSTOMER));
        //由测试部员工（非合法人员）创建测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.createTestcaseList("P001", "E001", null, 2001L, Role.TESTER));
        //由测试部主管（非合法人员）创建测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.createTestcaseList("P001", "E001", null, 2001L, Role.TESTING_SUPERVISOR));
        //由质量部员工（非合法人员）创建测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.createTestcaseList("P001", "E001", null, 3001L, Role.QA));
        //由质量部主管（非合法人员）创建测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.createTestcaseList("P001", "E001", null, 3000L, Role.QA_SUPERVISOR));

        //由指派的市场部员工/市场部主管（合法人员）创建测试用例表
        List<Testcase.TestcaseList> testcases = new ArrayList<>();
        Assertions.assertDoesNotThrow(() ->
                mongotestcaseservice.createTestcaseList("P001", "E001", null, 1001L, Role.MARKETER));
        Assertions.assertDoesNotThrow(() ->
                mongotestcaseservice.createTestcaseList("P001", "E001", null, 1000L, Role.MARKETING_SUPERVISOR));
        /*若非被指派的市场部员工想创建，则会在创建项目时识别出并报错，此处无需再进行检测和处理*/
    }

    @Test
    void findTestcaseList() {

        Testcase testcase = new Testcase();
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        when(testcaseDAO.findTestcaseListById(any())).thenReturn(testcase);

        //由客户（非合法人员）查找测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.findTestcaseList("TestcaseListId", 11111L, Role.QA));
        //由市场部员工（非合法人员）查找测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.findTestcaseList("TestcaseListId", 1001L, Role.MARKETER));
        //由市场部主管（非合法人员）查找测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.findTestcaseList("TestcaseListId", 1000L, Role.MARKETING_SUPERVISOR));
        //由非指定的测试部员工（非合法人员）查找测试用例表
        when(projectService.getProjectBaseInfo(any()).getTesterId()).thenReturn(2001L);
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.findTestcaseList("TestcaseListId", 2066L, Role.TESTER));
        //由非指定的质量部员工（非合法人员）查找测试用例表
        when(projectService.getProjectBaseInfo(any()).getQaId()).thenReturn(3001L);
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.findTestcaseList("TestcaseListId", 3666L, Role.QA));

        //由指派的测试部员工/测试部主管（合法人员）查找测试用例表
        when(testcaseDAO.findTestcaseListById(any())).thenReturn(testcase);
        when(projectService.getProjectBaseInfo(any()).getTesterId()).thenReturn(2001L);
        Assertions.assertDoesNotThrow(() ->
                mongotestcaseservice.findTestcaseList("TestcaseListId", 2001L, Role.TESTER));

        Assertions.assertDoesNotThrow(() ->
                mongotestcaseservice.findTestcaseList("TestcaseListId", 2000L, Role.TESTING_SUPERVISOR));
        //由指派的质量部员工/质量部主管（合法人员）查找测试用例表
        when(testcaseDAO.findTestcaseListById(any())).thenReturn(testcase);
        when(projectService.getProjectBaseInfo(any()).getQaId()).thenReturn(3001L);
        Assertions.assertDoesNotThrow(() ->
                mongotestcaseservice.findTestcaseList("TestcaseListId", 3001L, Role.QA));

        Assertions.assertDoesNotThrow(() ->
                mongotestcaseservice.findTestcaseList("TestcaseListId", 3000L, Role.QA_SUPERVISOR));

        //尝试寻找不存在的测试用例表
        when(testcaseDAO.findTestcaseListById(any())).thenReturn(null);
        Assertions.assertThrows(
                TestcaseNotFoundException.class,
                () -> mongotestcaseservice.findTestcaseList("TestcaseListId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                TestcaseNotFoundException.class,
                () -> mongotestcaseservice.findTestcaseList("TestcaseListId", 2001L, Role.TESTER));
        Assertions.assertThrows(
                TestcaseNotFoundException.class,
                () -> mongotestcaseservice.findTestcaseList("TestcaseListId", 3000L, Role.QA_SUPERVISOR));
    }

    @Test
    void updateTestcaseList() {

        Testcase testcase = new Testcase();
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        when(testcaseDAO.findTestcaseListById(any())).thenReturn(testcase);

        //由客户（非合法人员）修改测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.updateTestcaseList("TestcaseListId", null, 11111L, Role.CUSTOMER));
        //由市场部员工（非合法人员）修改测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.updateTestcaseList("TestcaseListId", null, 1001L, Role.MARKETER));
        //由市场部主管（非合法人员）修改测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.updateTestcaseList("TestcaseListId", null, 1000L, Role.MARKETING_SUPERVISOR));
        //由质量部员工（非合法人员）修改测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.updateTestcaseList("TestcaseListId", null, 3001L, Role.QA));
        //由质量部主管（非合法人员）修改测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.updateTestcaseList("TestcaseListId", null, 3000L, Role.QA_SUPERVISOR));
        //由非指派的测试部人员（非合法人员）修改测试用例表
        when(projectService.getProjectBaseInfo(any()).getTesterId()).thenReturn(2001L);
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.findTestcaseList("TestcaseListId", 2066L, Role.TESTER));

        //由指派的测试部人员/测试部主管（合法人员）修改测试用例
        when(testcaseDAO.findTestcaseListById(any())).thenReturn(testcase);
        when(projectService.getProjectBaseInfo(any()).getTesterId()).thenReturn(2001L);
        Assertions.assertDoesNotThrow(() ->
                mongotestcaseservice.updateTestcaseList("TestcaseListId", null, 2001L, Role.TESTER));

        Assertions.assertDoesNotThrow(() ->
                mongotestcaseservice.updateTestcaseList("TestcaseListId", null, 2000L, Role.TESTING_SUPERVISOR));

        //尝试修改不存在的测试用例表
        when(testcaseDAO.findTestcaseListById(any())).thenReturn(null);
        Assertions.assertThrows(
                TestcaseNotFoundException.class,
                () -> mongotestcaseservice.updateTestcaseList("TestcaseListId", null, 2001L, Role.TESTER));
        Assertions.assertThrows(
                TestcaseNotFoundException.class,
                () -> mongotestcaseservice.updateTestcaseList("TestcaseListId", null, 2000L, Role.TESTING_SUPERVISOR));
    }

    @Test
    void removeTestcaseList() {

        Testcase testcase = new Testcase();
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        when(testcaseDAO.findTestcaseListById(any())).thenReturn(testcase);

        //由客户（非合法人员）删除测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.removeTestcaseList("TestcaseListId", 11111L, Role.CUSTOMER));
        //由市场部员工（非合法人员）删除测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.removeTestcaseList("TestcaseListId", 1001L, Role.MARKETER));
        //由市场部主管（非合法人员）删除测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.removeTestcaseList("TestcaseListId", 1000L, Role.MARKETING_SUPERVISOR));
        //由测试部员工（非合法人员）删除测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.removeTestcaseList("TestcaseListId", 2001L, Role.TESTER));
        //由测试部主管（非合法人员）删除测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.removeTestcaseList("TestcaseListId", 2000L, Role.TESTING_SUPERVISOR));
        //由质量部员工（非合法人员）删除测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.removeTestcaseList("TestcaseListId", 3001L, Role.QA));
        //由质量部主管（非合法人员）删除测试用例表
        Assertions.assertThrows(
                TestcasePermissionDeniedException.class,
                () -> mongotestcaseservice.removeTestcaseList("TestcaseListId", 3000L, Role.QA_SUPERVISOR));

        //由超级管理员admin（合法人员）删除测试用表
        when(testcaseDAO.deleteTestcaseList(any())).thenReturn(true);
        Assertions.assertDoesNotThrow(() ->
                mongotestcaseservice.removeTestcaseList("TestcaseListId", 0L, Role.ADMIN));

        //尝试删除不存在的测试用例表
        when(testcaseDAO.findTestcaseListById(any())).thenReturn(null);
        Assertions.assertThrows(
                TestcaseNotFoundException.class,
                () -> mongotestcaseservice.removeTestcaseList("TestcaseListId", 0L, Role.ADMIN));
    }
}