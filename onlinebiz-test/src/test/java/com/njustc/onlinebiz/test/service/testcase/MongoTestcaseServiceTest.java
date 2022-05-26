package com.njustc.onlinebiz.test.service.testcase;

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


    @Test
    void createTestcaseList() {
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