package com.njustc.onlinebiz.test.service.testissue;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.dao.testissue.TestIssueDAO;
import com.njustc.onlinebiz.test.exception.testissue.TestIssueDAOFailureException;
import com.njustc.onlinebiz.test.exception.testissue.TestIssueNotFoundException;
import com.njustc.onlinebiz.test.exception.testissue.TestIssuePermissionDeniedException;
import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;
import com.njustc.onlinebiz.common.model.test.testissue.TestIssueStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class MongoTestIssueService implements TestIssueService {
    private static final String ENTRUST_SERVICE_URL = "http://onlinebiz-entrust";
    private final RestTemplate restTemplate;
    private final TestIssueDAO testIssueDAO;

    public MongoTestIssueService(RestTemplate restTemplate, MongoTemplate mongoTemplate, TestIssueDAO testIssueDAO) {
        this.restTemplate = restTemplate;
        this.testIssueDAO = testIssueDAO;
    }

    @Override
    public String createTestIssueList(String entrustId, List<TestIssueList.TestIssue> testIssues, Long userId, Role userRole) {
        if (!hasAuthorityToCreate(userRole)) {
            throw new TestIssuePermissionDeniedException("无权新建测试问题清单");
        }
        TestIssueList testIssueList = new TestIssueList();
        testIssueList.setEntrustId(entrustId);
        testIssueList.setTestIssues(testIssues);
        testIssueList.setStatus(new TestIssueStatus(true, "需修改"));
        return testIssueDAO.insertTestIssueList(testIssueList).getId();
    }

    @Override
    public TestIssueList findTestIssueList(String testIssueListId, Long userId, Role userRole) {
        TestIssueList testIssueList = testIssueDAO.findTestIssueListById(testIssueListId);
        if (testIssueList == null) {
            throw new TestIssueNotFoundException("该测试问题清单不存在");
        }
        if (!hasAuthorityToCheck(userId, userRole, testIssueList)) {
            throw new TestIssuePermissionDeniedException("无权查看该测试问题清单");
        }
        return testIssueList;
    }

    @Override
    public void updateTestIssueList(String testIssueListId, List<TestIssueList.TestIssue> testIssues, Long userId, Role userRole) {
        TestIssueList testIssueList = testIssueDAO.findTestIssueListById(testIssueListId);
        if (testIssueList == null) {
            throw new TestIssueNotFoundException("该测试问题清单不存在");
        }
        if (!hasAuthorityToFill(userId, userRole, testIssueList)) {
            throw new TestIssuePermissionDeniedException("无权查看该测试问题清单");
        }
        if (!testIssueDAO.updateContent(testIssueListId, testIssues) || !testIssueDAO.updateStatus(testIssueListId, new TestIssueStatus(true, null))) {
            throw new TestIssueDAOFailureException("更新测试问题清单失败");
        }
    }

    @Override
    public void removeTestIssueList(String testIssueListId, Long userId, Role userRole) {
        if (userRole != Role.ADMIN) {
            throw new TestIssuePermissionDeniedException("无权删除测试问题清单");
        }
        TestIssueList testIssueList = findTestIssueList(testIssueListId, userId, userRole);
        if (!testIssueDAO.deleteTestIssueList(testIssueList.getId())) {
            throw new TestIssueDAOFailureException("删除测试问题清单失败");
        }
    }

    private boolean hasAuthorityToCreate(Role userRole) {
        /*根据调研情况，只有市场部人员、市场部主管具有新建测试问题清单权限*/
        return userRole == Role.ADMIN || userRole == Role.MARKETER || userRole == Role.MARKETING_SUPERVISOR;
    }

    private boolean hasAuthorityToCheck(Long userId, Role userRole, TestIssueList testIssueList) {
        String entrustId = testIssueList.getEntrustId();
        ResponseEntity<Long> responseEntity = restTemplate.getForEntity(ENTRUST_SERVICE_URL + "/api/entrust/{entrustId}/get_testerId", Long.class, entrustId);
        if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
            Long testerId = responseEntity.getBody();
            /*根据调研情况，分配的测试部人员、测试部主管、所有质量部人员、质量部主管均有权限查阅*/
            return userRole == Role.ADMIN || (userRole == Role.TESTER && userId.equals(testerId)) || userRole == Role.TESTING_SUPERVISOR || userRole == Role.QA || userRole == Role.QA_SUPERVISOR;
        }
        return false;
    }

    private boolean hasAuthorityToFill(Long userId, Role userRole, TestIssueList testIssueList) {
        String entrustId = testIssueList.getEntrustId();
        ResponseEntity<Long> responseEntity = restTemplate.getForEntity(ENTRUST_SERVICE_URL + "/api/entrust/{entrustId}/get_testerId", Long.class, entrustId);
        if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
            Long testerId = responseEntity.getBody();
            /*根据调研情况，分配的测试部人员、测试部主管有权限修改*/
            return userRole == Role.ADMIN || (userRole == Role.TESTER && userId.equals(testerId)) || userRole == Role.TESTING_SUPERVISOR;
        }
        return false;
    }
}
