package com.njustc.onlinebiz.test.service.testcase;

import com.njustc.onlinebiz.common.model.EntrustDto;
import com.njustc.onlinebiz.common.model.test.project.ProjectBaseInfo;
import com.njustc.onlinebiz.test.dao.testcase.TestcaseDAO;
import com.njustc.onlinebiz.test.exception.project.ProjectDAOFailureException;
import com.njustc.onlinebiz.test.service.project.ProjectService;
import org.junit.jupiter.api.Test;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.exception.testcase.TestcaseNotFoundException;
import com.njustc.onlinebiz.test.exception.testcase.TestcasePermissionDeniedException;
import com.njustc.onlinebiz.common.model.test.testcase.Testcase;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class MongoTestcaseServiceTest {

    @Autowired
    private TestcaseService testcaseservice;
    private ProjectService projectService;
    private TestcaseDAO testcaseDAO;
    private static final String ENTRUST_SERVICE_URL = "http://onlinebiz-entrust";
    private RestTemplate restTemplate;
    private static String TestCaseListId1 = null;
    private static String TestCaseListId2 = null;
    private static String TestCaseListId3 = null;
    private static Testcase TestCaseList1 = null;
    private static Testcase TestCaseList2 = null;
    private static Testcase TestCaseList3 = null;

    @Test
    void createTestcaseList() {
        //由客户（非合法人员）创建测试用例表
        try {
            testcaseservice.createTestcaseList("P001", "E001", null, 11111L, Role.CUSTOMER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Customer try to create a testcase table and cause a mistake.");
        }
        //由测试部员工（非合法人员）创建测试用例表
        try {
            testcaseservice.createTestcaseList("P001", "E001", null, 2001L, Role.TESTER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Tester try to create a testcase table and cause a mistake.");
        }
        //由测试部主管（非合法人员）创建测试用例表
        try {
            testcaseservice.createTestcaseList("P001", "E001", null, 2000L, Role.TESTING_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Testing supervisor try to create a testcase table and cause a mistake.");
        }
        //由质量部员工（非合法人员）创建测试用例表
        try {
            testcaseservice.createTestcaseList("P001", "E001", null, 3001L, Role.QA);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("QA try to create a testcase table and cause a mistake.");
        }
        //由质量部主管（非合法人员）创建测试用例表
        try {
            testcaseservice.createTestcaseList("P001", "E001", null, 3000L, Role.QA_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("QA supervisor try to create a testcase table and cause a mistake.");
        }
        //由指派的市场部员工/市场部主管（合法人员）创建测试用例表
        String url1 = ENTRUST_SERVICE_URL + "/api/entrust/" + "E001" + "/get_dto";
        ResponseEntity<EntrustDto> responseEntity1 = restTemplate.getForEntity(url1, EntrustDto.class);
        EntrustDto entrustDto1 = responseEntity1.getBody();
        TestCaseListId1 = testcaseservice.createTestcaseList("P001", "E001", null, entrustDto1.getMarketerId(), Role.MARKETER);
        String url2 = ENTRUST_SERVICE_URL + "/api/entrust/" + "E002" + "/get_dto";
        ResponseEntity<EntrustDto> responseEntity2 = restTemplate.getForEntity(url2, EntrustDto.class);
        EntrustDto entrustDto2 = responseEntity2.getBody();
        TestCaseListId2 = testcaseservice.createTestcaseList("P002", "E002", null, entrustDto2.getMarketerId(), Role.MARKETER);
        TestCaseListId3 = testcaseservice.createTestcaseList("P003", "E003", null, 1000L, Role.MARKETING_SUPERVISOR);

        /*若非被指派的市场部员工想创建，则会在创建项目时识别出并报错，此处无需再进行检测和处理*/
    }

    @Test
    void findTestcaseList() {
        //由客户（非合法人员）查找测试用例表
        try {
            testcaseservice.findTestcaseList(TestCaseListId1, 11111L, Role.CUSTOMER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Customer try to check a testcase table and cause a mistake.");
        }
        //由市场部员工（非合法人员）查找测试用例表
        try {
            testcaseservice.findTestcaseList(TestCaseListId1, 1001L, Role.MARKETER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Marketer try to check a testcase table and cause a mistake.");
        }
        //由市场部主管（非合法人员）查找测试用例表
        try {
            testcaseservice.findTestcaseList(TestCaseListId1, 1000L, Role.MARKETING_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Marketing supervisor try to check a testcase table and cause a mistake.");
        }
        //由非指定的测试部员工（非合法人员）查找测试用例表
        try {
            testcaseservice.findTestcaseList(TestCaseListId1, 2066L, Role.TESTER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Another tester try to check a testcase table and cause a mistake.");
        }
        //由非指定的质量部员工（非合法人员）查找测试用例表
        try {
            testcaseservice.findTestcaseList(TestCaseListId1, 3666L, Role.QA);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Another qa try to check a testcase table and cause a mistake.");
        }
        //由指派的测试部员工/测试部主管（合法人员）查找测试用例表
        Testcase testcase1 = testcaseDAO.findTestcaseListById(TestCaseListId1);
        String projectId1 = testcase1.getProjectId();
        ProjectBaseInfo pbaseinfo1 = projectService.getProjectBaseInfo(projectId1);
        Long testerId1 = pbaseinfo1.getTesterId();
        TestCaseList1 = testcaseservice.findTestcaseList(TestCaseListId1, testerId1, Role.TESTER);
        assert (TestCaseList1.getProjectId().equals(projectId1));
        Testcase testcase2 = testcaseDAO.findTestcaseListById(TestCaseListId2);
        String projectId2 = testcase2.getProjectId();
        ProjectBaseInfo pbaseinfo2 = projectService.getProjectBaseInfo(projectId2);
        Long testerId2 = pbaseinfo2.getTesterId();
        TestCaseList2 = testcaseservice.findTestcaseList(TestCaseListId2, testerId2, Role.TESTER);
        assert (TestCaseList2.getProjectId().equals(projectId2));
        TestCaseList3 = testcaseservice.findTestcaseList(TestCaseListId3, 2000L, Role.TESTING_SUPERVISOR);
        Testcase testcase3 = testcaseDAO.findTestcaseListById(TestCaseListId3);
        String projectId3 = testcase3.getProjectId();
        assert (TestCaseList3.getProjectId().equals(projectId3));
        //由指派的质量部员工/质量部主管（合法人员）查找测试用例表
        Long qaId1 = pbaseinfo1.getQaId();
        TestCaseList1 = testcaseservice.findTestcaseList(TestCaseListId1, qaId1, Role.QA);
        assert (TestCaseList1.getProjectId().equals(projectId1));
        Long qaId2 = pbaseinfo2.getQaId();
        TestCaseList2 = testcaseservice.findTestcaseList(TestCaseListId2, qaId2, Role.QA);
        assert (TestCaseList2.getProjectId().equals(projectId2));
        TestCaseList3 = testcaseservice.findTestcaseList(TestCaseListId3, 3000L, Role.QA_SUPERVISOR);
        assert (TestCaseList3.getProjectId().equals(projectId3));
        //尝试寻找不存在的测试用例表
        try {
            testcaseservice.findTestcaseList("T1000", 2000L, Role.TESTING_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcaseNotFoundException.class));
            System.out.println("Testing supervisor try to update a testcase table non-existent and cause a mistake.");
        }
        try {
            testcaseservice.findTestcaseList("T10086", 2000L, Role.TESTING_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcaseNotFoundException.class));
            System.out.println("Testing supervisor try to update a testcase table non-existent and cause a mistake.");
        }
        try {
            testcaseservice.findTestcaseList("T10010", 2000L, Role.TESTING_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcaseNotFoundException.class));
            System.out.println("Testing supervisor try to update a testcase table non-existent and cause a mistake.");
        }
    }

    @Test
    void updateTestcaseList() {
        //由客户（非合法人员）修改测试用例表
        try {
            testcaseservice.updateTestcaseList(TestCaseListId1, null, 11111L, Role.CUSTOMER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Customer try to update a testcase table and cause a mistake.");
        }
        //由市场部员工（非合法人员）修改测试用例表
        try {
            testcaseservice.updateTestcaseList(TestCaseListId1, null, 1001L, Role.MARKETER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Marketer try to update a testcase table and cause a mistake.");
        }
        //由市场部主管（非合法人员）修改测试用例表
        try {
            testcaseservice.updateTestcaseList(TestCaseListId1, null, 1000L, Role.MARKETING_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Marketing supervisor try to update a testcase table and cause a mistake.");
        }
        //由质量部员工（非合法人员）修改测试用例表
        try {
            testcaseservice.updateTestcaseList(TestCaseListId1, null, 3001L, Role.QA);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("QA try to update a testcase table and cause a mistake.");
        }
        //由质量部主管（非合法人员）修改测试用例表
        try {
            testcaseservice.updateTestcaseList(TestCaseListId1, null, 3000L, Role.QA_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("QA supervisor try to update a testcase table and cause a mistake.");
        }
        //由非指派的测试部人员（非合法人员）修改测试用例表
        Testcase testcase1 = testcaseDAO.findTestcaseListById(TestCaseListId1);
        String projectId1 = testcase1.getProjectId();
        ProjectBaseInfo pbaseinfo1 = projectService.getProjectBaseInfo(projectId1);
        Long testerId1 = pbaseinfo1.getTesterId();
        if(testerId1.equals(2066L)) {
            testerId1 = 2065L;
        }
        else {
            testerId1 = 2066L;
        }
        try {
            testcaseservice.updateTestcaseList(TestCaseListId1, null, testerId1, Role.TESTER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Another tester try to update a testcase table and cause a mistake.");
        }
        //由指派的测试部人员/测试部主管（合法人员）修改测试用例
        testcaseservice.updateTestcaseList(TestCaseListId1, null, testerId1, Role.TESTER);
        testcaseservice.updateTestcaseList(TestCaseListId1, null, 2000L, Role.TESTING_SUPERVISOR);
        List<Testcase.TestcaseList> NewTestcases = null;
        Testcase.TestcaseList get = new Testcase.TestcaseList("category", "testcaseId", "designInstruction", "statute", "expectedResult", "designer", "time");
        NewTestcases.add(get);
        testcaseservice.updateTestcaseList(TestCaseListId1, NewTestcases, testerId1, Role.TESTER);
        //尝试修改不存在的测试用例表
        //传参为数据库中不存在的testcase编号
        try {
            testcaseservice.updateTestcaseList("T1000", null, 2000L, Role.TESTING_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcaseNotFoundException.class));
            System.out.println("Testing supervisor try to update a testcase table non-existent and cause a mistake.");
        }
        try {
            testcaseservice.updateTestcaseList("T10086", null, 2000L, Role.TESTING_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcaseNotFoundException.class));
            System.out.println("Testing supervisor try to update a testcase table non-existent and cause a mistake.");
        }
        try {
            testcaseservice.updateTestcaseList("T10010", null, 2000L, Role.TESTING_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcaseNotFoundException.class));
            System.out.println("Testing supervisor try to update a testcase table non-existent and cause a mistake.");
        }
    }

    @Test
    void removeTestcaseList() {
        //由客户（非合法人员）删除测试用例表
        try {
            testcaseservice.removeTestcaseList(TestCaseListId2, 11111L, Role.CUSTOMER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Customer try to delete a testcase table and cause a mistake.");
        }
        //由市场部员工（非合法人员）删除测试用例表
        try {
            testcaseservice.removeTestcaseList(TestCaseListId2, 1001L, Role.MARKETER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Marketer try to delete a testcase table and cause a mistake.");
        }
        //由市场部主管（非合法人员）删除测试用例表
        try {
            testcaseservice.removeTestcaseList(TestCaseListId2, 1000L, Role.MARKETING_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Marketing supervisor try to delete a testcase table and cause a mistake.");
        }
        //由测试部员工（非合法人员）删除测试用例表
        try {
            testcaseservice.removeTestcaseList(TestCaseListId2, 2001L, Role.TESTER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Tester try to delete a testcase table and cause a mistake.");
        }
        //由测试部主管（非合法人员）删除测试用例表
        try {
            testcaseservice.removeTestcaseList(TestCaseListId2, 2000L, Role.TESTING_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Testing supervisor try to delete a testcase table and cause a mistake.");
        }
        //由质量部员工（非合法人员）删除测试用例表
        try {
            testcaseservice.removeTestcaseList(TestCaseListId2, 3001L, Role.QA);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("QA try to delete a testcase table and cause a mistake.");
        }
        //由质量部主管（非合法人员）删除测试用例表
        try {
            testcaseservice.removeTestcaseList(TestCaseListId2, 3000L, Role.QA_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("QA supervisor try to delete a testcase table and cause a mistake.");
        }
        //由超级管理员admin（合法人员）删除测试用表
        testcaseservice.removeTestcaseList(TestCaseListId2, 0L, Role.ADMIN);
        //尝试删除不存在的测试用例表
        try {
            testcaseservice.removeTestcaseList("T1000", 0L, Role.ADMIN);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcaseNotFoundException.class));
            System.out.println("Admin try to delete a testcase table non-existent and cause a mistake.");
        }
        try {
            testcaseservice.removeTestcaseList("T10086", 0L, Role.ADMIN);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcaseNotFoundException.class));
            System.out.println("Admin try to delete a testcase table non-existent and cause a mistake.");
        }
        try {
            testcaseservice.removeTestcaseList("T10010", 0L, Role.ADMIN);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcaseNotFoundException.class));
            System.out.println("Admin try to delete a testcase table non-existent and cause a mistake.");
        }
    }
}