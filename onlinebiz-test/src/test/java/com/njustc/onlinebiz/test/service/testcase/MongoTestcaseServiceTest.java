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
            testcaseservice.createTestcaseList("E001", null, 1L, Role.CUSTOMER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
        }
        //由市场部员工（非合法人员）创建测试用例表
        try {
            testcaseservice.createTestcaseList("E001", null, 1L, Role.MARKETER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
        }
        //由市场部主管（非合法人员）创建测试用例表
        try {
            testcaseservice.createTestcaseList("E001", null, 1L, Role.MARKETING_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestcasePermissionDeniedException.class));
        }
    }

    @Test
    void findTestcaseList() {
    }

    @Test
    void updateTestcaseList() {
    }

    @Test
    void removeTestcaseList() {
    }
}