package com.njustc.onlinebiz.test.service.testcase;

import com.njustc.onlinebiz.test.exception.project.ProjectPermissionDeniedException;
import com.thoughtworks.xstream.mapper.Mapper;
import org.junit.jupiter.api.Test;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.exception.testcase.TestcaseDAOFailureException;
import com.njustc.onlinebiz.test.exception.testcase.TestcaseNotFoundException;
import com.njustc.onlinebiz.test.exception.testcase.TestcasePermissionDeniedException;
import com.njustc.onlinebiz.test.model.testcase.Testcase;
import com.njustc.onlinebiz.test.model.testcase.TestcaseStatus;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class MongoTestcaseServiceTest {

    @Autowired
    private TestcaseService testcaseservice;
    private static String TestCaseListId1 = null;
    private static String TestCaseListId2 = null;
    private static String TestCaseListId3 = null;

    @Test
    void createTestcaseList() {
        //由客户（非合法人员）创建测试用例表
        try {
            testcaseservice.createTestcaseList("E001", null, 1111L, Role.CUSTOMER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Customer try to create a testcase table and cause a mistake.");
        }
        //由测试部员工（非合法人员）创建测试用例表
        try {
            testcaseservice.createTestcaseList("E001", null, 11L, Role.TESTER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Tester try to create a testcase table and cause a mistake.");
        }
        //由测试部主管（非合法人员）创建测试用例表
        try {
            testcaseservice.createTestcaseList("E001", null, 10L, Role.TESTING_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Testing supervisor try to create a testcase table and cause a mistake.");
        }
        //由质量部员工（非合法人员）创建测试用例表
        try {
            testcaseservice.createTestcaseList("E001", null, 111L, Role.QA);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("QA try to create a testcase table and cause a mistake.");
        }
        //由质量部主管（非合法人员）创建测试用例表
        try {
            testcaseservice.createTestcaseList("E001", null, 100L, Role.QA_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("QA supervisor try to create a testcase table and cause a mistake.");
        }
        //由指派的市场部员工/市场部主管（合法人员）创建测试用例表
        TestCaseListId1 = testcaseservice.createTestcaseList("E001", null, 1L, Role.MARKETER);
        TestCaseListId2 = testcaseservice.createTestcaseList("E002", null, 1L, Role.MARKETER);
        TestCaseListId3 = testcaseservice.createTestcaseList("E003", null, 2L, Role.MARKETING_SUPERVISOR);

        /*若非被指派的市场部员工想创建，则会在创建项目时识别出并报错，此处无需再进行检测和处理*/
    }

    @Test
    void findTestcaseList() {
        //由客户（非合法人员）查找测试用例表
        try {
            testcaseservice.findTestcaseList("T001", 1111L, Role.CUSTOMER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Customer try to check a testcase table and cause a mistake.");
        }
        //由市场部员工（非合法人员）查找测试用例表
        try {
            testcaseservice.findTestcaseList("T001", 1L, Role.MARKETER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Marketer try to check a testcase table and cause a mistake.");
        }
        //由市场部主管（非合法人员）查找测试用例表
        try {
            testcaseservice.findTestcaseList("T001", 2L, Role.MARKETING_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Marketing supervisor try to check a testcase table and cause a mistake.");
        }
        //由非指定的测试部员工（非合法人员）查找测试用例表
        try {
            testcaseservice.findTestcaseList("T001", 66L, Role.TESTER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Another tester try to check a testcase table and cause a mistake.");
        }
        //由非指定的质量部员工（非合法人员）查找测试用例表
        /*TODO:测试时在数据库中注入特定的数据，保证testcaseId对应的qaId和这里的userId参数是不同的，测试时挂着委托服务测*/
        try {
            testcaseservice.findTestcaseList("T001", 666L, Role.TESTER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Another qa try to check a testcase table and cause a mistake.");
        }
        //由指派的测试部员工/测试部主管（合法人员）创建测试用例表
        /*TODO:测试时在数据库中注入特定的数据，保证entrustId对应的testerId和这里的userId参数是一致的，测试时挂着委托服务测*/
        TestCaseListId1 = testcaseservice.createTestcaseList("T001", null, 11L, Role.TESTER);
        TestCaseListId2 = testcaseservice.createTestcaseList("T002", null, 11L, Role.TESTER);
        TestCaseListId3 = testcaseservice.createTestcaseList("T003", null, 10L, Role.TESTING_SUPERVISOR);
        //由指派的质量部员工/质量部主管（合法人员）创建测试用例表
        /*TODO:测试时在数据库中注入特定的数据，保证entrustId对应的qaId和这里的userId参数是一致的，测试时挂着委托服务测*/
        TestCaseListId1 = testcaseservice.createTestcaseList("T001", null, 111L, Role.QA);
        TestCaseListId2 = testcaseservice.createTestcaseList("T002", null, 111L, Role.QA);
        TestCaseListId3 = testcaseservice.createTestcaseList("T003", null, 100L, Role.QA_SUPERVISOR);
    }

    @Test
    void updateTestcaseList() {
        //由客户（非合法人员）修改测试用例表
        try {
            testcaseservice.updateTestcaseList("T001", null, 1111L, Role.CUSTOMER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Customer try to update a testcase table and cause a mistake.");
        }
        //由市场部员工（非合法人员）修改测试用例表
        try {
            testcaseservice.updateTestcaseList("T001", null, 1L, Role.MARKETER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Marketer try to update a testcase table and cause a mistake.");
        }
        //由市场部主管（非合法人员）修改测试用例表
        try {
            testcaseservice.updateTestcaseList("T001", null, 2L, Role.MARKETING_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Marketing supervisor try to update a testcase table and cause a mistake.");
        }
        //由质量部员工（非合法人员）修改测试用例表
        try {
            testcaseservice.updateTestcaseList("T001", null, 111L, Role.QA);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("QA try to update a testcase table and cause a mistake.");
        }
        //由质量部主管（非合法人员）修改测试用例表
        try {
            testcaseservice.updateTestcaseList("T001", null, 1111L, Role.QA_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("QA supervisor try to update a testcase table and cause a mistake.");
        }
        //由非指派的测试部人员（非合法人员）修改测试用例表
        /*TODO:测试时在数据库中注入特定的数据，保证testcaseListId对应的entrustId对应的testerId和这里的userId参数是不同的，测试时挂着委托服务测*/
        try {
            testcaseservice.updateTestcaseList("T001", null, 66L, Role.TESTER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
            System.out.println("Another tester try to update a testcase table and cause a mistake.");
        }
        //由指派的测试部人员/测试部主管（合法人员）修改测试用例表
        /*TODO:测试时在数据库中注入特定的数据，保证testcaseListId对应的entrustId对应的testerId和这里的userId参数是不同的，测试时挂着委托服务测*/
        testcaseservice.updateTestcaseList("T001", null, 11L, Role.TESTER);
        testcaseservice.updateTestcaseList("T001", null, 10L, Role.TESTING_SUPERVISOR);
        List<Testcase.TestcaseList> NewTestcases = null;
        Testcase.TestcaseList get = new Testcase.TestcaseList("category", "testcaseId", "designInstruction", "statute", "expectedResult", "designer", "time");
        NewTestcases.add(get);
        testcaseservice.updateTestcaseList("T001", NewTestcases, 11L, Role.TESTER);
        //尝试修改不存在的测试用例表
        try {
            testcaseservice.updateTestcaseList("T1000", null, 11L, Role.TESTER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcaseNotFoundException.class));
            System.out.println("Try to update a testcase table non-existent and cause a mistake.");
        }
    }

    @Test
    void removeTestcaseList() {
    }
}