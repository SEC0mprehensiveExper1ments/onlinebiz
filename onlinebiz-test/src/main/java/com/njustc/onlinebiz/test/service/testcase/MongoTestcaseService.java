package com.njustc.onlinebiz.test.service.testcase;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.dao.testcase.TestcaseDAO;
import com.njustc.onlinebiz.test.exception.scheme.SchemeDAOFailureException;
import com.njustc.onlinebiz.test.exception.scheme.SchemePermissionDeniedException;
import com.njustc.onlinebiz.test.exception.testcase.TestcaseDAOFailureException;
import com.njustc.onlinebiz.test.exception.testcase.TestcaseNotFoundException;
import com.njustc.onlinebiz.test.exception.testcase.TestcasePermissionDeniedException;
import com.njustc.onlinebiz.test.model.testcase.Testcase;
import com.njustc.onlinebiz.test.model.testcase.TestcaseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class MongoTestcaseService implements TestcaseService {
    private static final String ENTRUST_SERVICE_URL = "http://onlinebiz-entrust";
    private final RestTemplate restTemplate;
    private final TestcaseDAO testcaseDAO;

    public MongoTestcaseService(RestTemplate restTemplate, MongoTemplate mongoTemplate, TestcaseDAO testcaseDAO) {
        this.restTemplate = restTemplate;
        this.testcaseDAO = testcaseDAO;
    }

    @Override
    public String createTestcaseList(String entrustId, List<Testcase.TestcaseList> testcases, Long userId, Role userRole){
        if (!hasAuthorityToCreate(userRole)) {
            throw new TestcasePermissionDeniedException("无权新建测试用例表");
        }
        Testcase testcase = new Testcase();
        testcase.setEntrustId(entrustId);
        testcase.setTestcases(testcases);
        testcase.setStatus(new TestcaseStatus(true, "需修改"));
        return testcaseDAO.insertTestcaseList(testcase).getId();
    }

    @Override
    public Testcase findTestcaseList(String testcaseListId, Long userId, Role userRole){
        Testcase testcase = testcaseDAO.findTestcaseListById(testcaseListId);
        if (testcase == null) {
            throw new TestcaseNotFoundException("该测试用例表不存在");
        }
        if (!hasAuthorityToCheck(userId, userRole, testcase)) {
            throw new TestcasePermissionDeniedException("无权查看该测试用例表");
        }
        return testcase;
    }

    @Override
    public void updateTestcaseList(String testcaseListId, List<Testcase.TestcaseList> testcases, Long userId, Role userRole){
        Testcase testcase = testcaseDAO.findTestcaseListById(testcaseListId);
        if (testcase == null) {
            throw new TestcaseNotFoundException("该测试用例表不存在");
        }
        if (!hasAuthorityToFill(userId, userRole, testcase)) {
            throw new TestcasePermissionDeniedException("无权查看该测试用例表");
        }
        if (!testcaseDAO.updateContent(testcaseListId, testcases) || !testcaseDAO.updateStatus(testcaseListId, new TestcaseStatus(true, null))) {
            throw new TestcaseDAOFailureException("更新测试用例表失败");
        }
    }

    @Override
    public void removeTestcaseList(String testcaseListId, Long userId, Role userRole){
        if (userRole != Role.ADMIN) {
            throw new SchemePermissionDeniedException("无权删除测试用例表");
        }
        Testcase testcase = findTestcaseList(testcaseListId, userId, userRole);
        if (!testcaseDAO.deleteTestcaseList(testcase.getId())) {
            throw new SchemeDAOFailureException("删除测试用例表失败");
        }
    }

    private boolean hasAuthorityToCreate(Role userRole) {
        /*根据调研情况，只有市场部人员、市场部主管具有新建测试用例表权限*/
        return userRole == Role.ADMIN || userRole == Role.MARKETER || userRole == Role.MARKETING_SUPERVISOR;
    }

    private boolean hasAuthorityToCheck(Long userId, Role userRole, Testcase testcase) {
        String entrustId = testcase.getEntrustId();
        ResponseEntity<Long> responseEntity = restTemplate.getForEntity(ENTRUST_SERVICE_URL + "/api/entrust/{entrustId}/get_testerId", Long.class, entrustId);
        if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
            Long testerId = responseEntity.getBody();
            /*根据调研情况，分配的测试部人员、测试部主管、所有质量部人员、质量部主管均有权限查阅*/
            return userRole == Role.ADMIN || (userRole == Role.TESTER && userId.equals(testerId)) || userRole == Role.TESTING_SUPERVISOR || userRole == Role.QA || userRole == Role.QA_SUPERVISOR;
        }
        return false;
    }

    private boolean hasAuthorityToFill(Long userId, Role userRole, Testcase testcase) {
        String entrustId = testcase.getEntrustId();
        ResponseEntity<Long> responseEntity = restTemplate.getForEntity(ENTRUST_SERVICE_URL + "/api/entrust/{entrustId}/get_testerId", Long.class, entrustId);
        if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
            Long testerId = responseEntity.getBody();
            /*根据调研情况，分配的测试部人员、测试部主管有权限修改*/
            return userRole == Role.ADMIN || (userRole == Role.TESTER && userId.equals(testerId)) || userRole == Role.TESTING_SUPERVISOR;
        }
        return false;
    }

}
