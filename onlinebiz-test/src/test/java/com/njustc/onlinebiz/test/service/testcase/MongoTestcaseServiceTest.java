//package com.njustc.onlinebiz.test.service.testcase;
//
//import org.junit.jupiter.api.Test;
//import com.njustc.onlinebiz.common.model.Role;
//import com.njustc.onlinebiz.test.exception.testcase.TestcaseNotFoundException;
//import com.njustc.onlinebiz.test.exception.testcase.TestcasePermissionDeniedException;
//import com.njustc.onlinebiz.common.model.test.testcase.Testcase;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//class MongoTestcaseServiceTest {
//
//    @Autowired
//    private TestcaseService testcaseservice;
//    private static String TestCaseListId1 = null;
//    private static String TestCaseListId2 = null;
//    private static String TestCaseListId3 = null;
//    private static Testcase TestCaseList1 = null;
//    private static Testcase TestCaseList2 = null;
//    private static Testcase TestCaseList3 = null;
//
//    @Test
//    void createTestcaseList() {
//        //由客户（非合法人员）创建测试用例表
//        try {
//            testcaseservice.createTestcaseList("E001", null, 11111L, Role.CUSTOMER);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Customer try to create a testcase table and cause a mistake.");
//        }
//        //由测试部员工（非合法人员）创建测试用例表
//        try {
//            testcaseservice.createTestcaseList("E001", null, 2001L, Role.TESTER);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Tester try to create a testcase table and cause a mistake.");
//        }
//        //由测试部主管（非合法人员）创建测试用例表
//        try {
//            testcaseservice.createTestcaseList("E001", null, 2000L, Role.TESTING_SUPERVISOR);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Testing supervisor try to create a testcase table and cause a mistake.");
//        }
//        //由质量部员工（非合法人员）创建测试用例表
//        try {
//            testcaseservice.createTestcaseList("E001", null, 3001L, Role.QA);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("QA try to create a testcase table and cause a mistake.");
//        }
//        //由质量部主管（非合法人员）创建测试用例表
//        try {
//            testcaseservice.createTestcaseList("E001", null, 3000L, Role.QA_SUPERVISOR);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("QA supervisor try to create a testcase table and cause a mistake.");
//        }
//        //由指派的市场部员工/市场部主管（合法人员）创建测试用例表
//        TestCaseListId1 = testcaseservice.createTestcaseList("E001", null, 1001L, Role.MARKETER);
//        TestCaseListId2 = testcaseservice.createTestcaseList("E002", null, 1001L, Role.MARKETER);
//        TestCaseListId3 = testcaseservice.createTestcaseList("E003", null, 1000L, Role.MARKETING_SUPERVISOR);
//
//        /*若非被指派的市场部员工想创建，则会在创建项目时识别出并报错，此处无需再进行检测和处理*/
//    }
//
//    @Test
//    void findTestcaseList() {
//        //由客户（非合法人员）查找测试用例表
//        try {
//            testcaseservice.findTestcaseList("T001", 11111L, Role.CUSTOMER);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Customer try to check a testcase table and cause a mistake.");
//        }
//        //由市场部员工（非合法人员）查找测试用例表
//        try {
//            testcaseservice.findTestcaseList("T001", 1001L, Role.MARKETER);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Marketer try to check a testcase table and cause a mistake.");
//        }
//        //由市场部主管（非合法人员）查找测试用例表
//        try {
//            testcaseservice.findTestcaseList("T001", 1000L, Role.MARKETING_SUPERVISOR);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Marketing supervisor try to check a testcase table and cause a mistake.");
//        }
//        //由非指定的测试部员工（非合法人员）查找测试用例表
//        try {
//            testcaseservice.findTestcaseList("T001", 2066L, Role.TESTER);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Another tester try to check a testcase table and cause a mistake.");
//        }
//        //由非指定的质量部员工（非合法人员）查找测试用例表
//        /*TODO:测试时在数据库中注入特定的数据，保证testcaseId对应的qaId和这里的userId参数是不同的，测试时挂着委托服务测*/
//        try {
//            testcaseservice.findTestcaseList("T001", 3666L, Role.QA);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Another qa try to check a testcase table and cause a mistake.");
//        }
//        //由指派的测试部员工/测试部主管（合法人员）查找测试用例表
//        /*TODO:测试时在数据库中注入特定的数据，保证entrustId对应的testerId和这里的userId参数是一致的，测试时挂着委托服务测*/
//        TestCaseList1 = testcaseservice.findTestcaseList("T001", 2001L, Role.TESTER);
//        TestCaseList2 = testcaseservice.findTestcaseList("T002", 2001L, Role.TESTER);
//        TestCaseList3 = testcaseservice.findTestcaseList("T003", 2000L, Role.TESTING_SUPERVISOR);
//        //由指派的质量部员工/质量部主管（合法人员）查找测试用例表
//        /*TODO:测试时在数据库中注入特定的数据，保证entrustId对应的qaId和这里的userId参数是一致的，测试时挂着委托服务测*/
//        TestCaseList1 = testcaseservice.findTestcaseList("T001", 3001L, Role.QA);
//        TestCaseList2 = testcaseservice.findTestcaseList("T002", 3001L, Role.QA);
//        TestCaseList3 = testcaseservice.findTestcaseList("T003", 3000L, Role.QA_SUPERVISOR);
//        //尝试寻找不存在的测试用例表
//        try {
//            testcaseservice.findTestcaseList("T1000", 2000L, Role.TESTING_SUPERVISOR);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcaseNotFoundException.class));
//            System.out.println("Testing supervisor try to update a testcase table non-existent and cause a mistake.");
//        }
//        try {
//            testcaseservice.findTestcaseList("T10086", 2000L, Role.TESTING_SUPERVISOR);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcaseNotFoundException.class));
//            System.out.println("Testing supervisor try to update a testcase table non-existent and cause a mistake.");
//        }
//        try {
//            testcaseservice.findTestcaseList("T10010", 2000L, Role.TESTING_SUPERVISOR);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcaseNotFoundException.class));
//            System.out.println("Testing supervisor try to update a testcase table non-existent and cause a mistake.");
//        }
//    }
//
//    @Test
//    void updateTestcaseList() {
//        //由客户（非合法人员）修改测试用例表
//        try {
//            testcaseservice.updateTestcaseList("T001", null, 11111L, Role.CUSTOMER);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Customer try to update a testcase table and cause a mistake.");
//        }
//        //由市场部员工（非合法人员）修改测试用例表
//        try {
//            testcaseservice.updateTestcaseList("T001", null, 1001L, Role.MARKETER);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Marketer try to update a testcase table and cause a mistake.");
//        }
//        //由市场部主管（非合法人员）修改测试用例表
//        try {
//            testcaseservice.updateTestcaseList("T001", null, 1000L, Role.MARKETING_SUPERVISOR);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Marketing supervisor try to update a testcase table and cause a mistake.");
//        }
//        //由质量部员工（非合法人员）修改测试用例表
//        try {
//            testcaseservice.updateTestcaseList("T001", null, 3001L, Role.QA);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("QA try to update a testcase table and cause a mistake.");
//        }
//        //由质量部主管（非合法人员）修改测试用例表
//        try {
//            testcaseservice.updateTestcaseList("T001", null, 3000L, Role.QA_SUPERVISOR);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("QA supervisor try to update a testcase table and cause a mistake.");
//        }
//        //由非指派的测试部人员（非合法人员）修改测试用例表
//        /*TODO:测试时在数据库中注入特定的数据，保证testcaseListId对应的entrustId对应的testerId和这里的userId参数是不同的，测试时挂着委托服务测*/
//        try {
//            testcaseservice.updateTestcaseList("T001", null, 2066L, Role.TESTER);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Another tester try to update a testcase table and cause a mistake.");
//        }
//        //由指派的测试部人员/测试部主管（合法人员）修改测试用例表
//        /*TODO:测试时在数据库中注入特定的数据，保证testcaseListId对应的entrustId对应的testerId和这里的userId参数是不同的，测试时挂着委托服务测*/
//        testcaseservice.updateTestcaseList("T001", null, 2001L, Role.TESTER);
//        testcaseservice.updateTestcaseList("T001", null, 2000L, Role.TESTING_SUPERVISOR);
//        List<Testcase.TestcaseList> NewTestcases = null;
//        Testcase.TestcaseList get = new Testcase.TestcaseList("category", "testcaseId", "designInstruction", "statute", "expectedResult", "designer", "time");
//        NewTestcases.add(get);
//        testcaseservice.updateTestcaseList("T001", NewTestcases, 2001L, Role.TESTER);
//        //尝试修改不存在的测试用例表
//        try {
//            testcaseservice.updateTestcaseList("T1000", null, 2000L, Role.TESTING_SUPERVISOR);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcaseNotFoundException.class));
//            System.out.println("Testing supervisor try to update a testcase table non-existent and cause a mistake.");
//        }
//        try {
//            testcaseservice.updateTestcaseList("T10086", null, 2000L, Role.TESTING_SUPERVISOR);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcaseNotFoundException.class));
//            System.out.println("Testing supervisor try to update a testcase table non-existent and cause a mistake.");
//        }
//        try {
//            testcaseservice.updateTestcaseList("T10010", null, 2000L, Role.TESTING_SUPERVISOR);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcaseNotFoundException.class));
//            System.out.println("Testing supervisor try to update a testcase table non-existent and cause a mistake.");
//        }
//    }
//
//    @Test
//    void removeTestcaseList() {
//        //由客户（非合法人员）删除测试用例表
//        try {
//            testcaseservice.removeTestcaseList("T001", 11111L, Role.CUSTOMER);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Customer try to delete a testcase table and cause a mistake.");
//        }
//        //由市场部员工（非合法人员）删除测试用例表
//        try {
//            testcaseservice.removeTestcaseList("T001", 1001L, Role.MARKETER);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Marketer try to delete a testcase table and cause a mistake.");
//        }
//        //由市场部主管（非合法人员）删除测试用例表
//        try {
//            testcaseservice.removeTestcaseList("T001", 1000L, Role.MARKETING_SUPERVISOR);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Marketing supervisor try to delete a testcase table and cause a mistake.");
//        }
//        //由测试部员工（非合法人员）删除测试用例表
//        try {
//            testcaseservice.removeTestcaseList("T001", 2001L, Role.TESTER);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Tester try to delete a testcase table and cause a mistake.");
//        }
//        //由测试部主管（非合法人员）删除测试用例表
//        try {
//            testcaseservice.removeTestcaseList("T001", 2000L, Role.TESTING_SUPERVISOR);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("Testing supervisor try to delete a testcase table and cause a mistake.");
//        }
//        //由质量部员工（非合法人员）删除测试用例表
//        try {
//            testcaseservice.removeTestcaseList("T001", 3001L, Role.QA);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("QA try to delete a testcase table and cause a mistake.");
//        }
//        //由质量部主管（非合法人员）删除测试用例表
//        try {
//            testcaseservice.removeTestcaseList("T001", 3000L, Role.QA_SUPERVISOR);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
//            System.out.println("QA supervisor try to delete a testcase table and cause a mistake.");
//        }
//        //由超级管理员admin（合法人员）删除测试用表
//        testcaseservice.removeTestcaseList("T001", 0L, Role.ADMIN);
//        //尝试删除不存在的测试用例表
//        try {
//            testcaseservice.removeTestcaseList("T1000", 0L, Role.ADMIN);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcaseNotFoundException.class));
//            System.out.println("Admin try to delete a testcase table non-existent and cause a mistake.");
//        }
//        try {
//            testcaseservice.removeTestcaseList("T10086", 0L, Role.ADMIN);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcaseNotFoundException.class));
//            System.out.println("Admin try to delete a testcase table non-existent and cause a mistake.");
//        }
//        try {
//            testcaseservice.removeTestcaseList("T10010", 0L, Role.ADMIN);
//        } catch (Exception e) {
//            assert (e.getClass().equals(TestcaseNotFoundException.class));
//            System.out.println("Admin try to delete a testcase table non-existent and cause a mistake.");
//        }
//    }
//}