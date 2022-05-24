package com.njustc.onlinebiz.test.service.testrecord;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.dao.testrecord.TestRecordDAO;
import com.njustc.onlinebiz.test.exception.testrecord.TestRecordDAOFailureException;
import com.njustc.onlinebiz.test.exception.testrecord.TestRecordNotFoundException;
import com.njustc.onlinebiz.test.exception.testrecord.TestRecordPermissionDeniedException;
import com.njustc.onlinebiz.test.model.testrecord.TestRecordList;
import com.njustc.onlinebiz.test.model.testrecord.TestRecordStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class MongoTestRecordService implements TestRecordService{
    private static final String ENTRUST_SERVICE_URL = "http://onlinebiz-entrust";
    private final RestTemplate restTemplate;
    private final TestRecordDAO testRecordDAO;

    public MongoTestRecordService(RestTemplate restTemplate, MongoTemplate mongoTemplate, TestRecordDAO testRecordDAO) {
        this.restTemplate = restTemplate;
        this.testRecordDAO = testRecordDAO;
    }

    @Override
    public String createTestRecordList(String entrustId, List<TestRecordList.TestRecord> testRecords, Long userId, Role userRole){
        if (!hasAuthorityToCreate(userRole)) {
            throw new TestRecordPermissionDeniedException("无权新建测试记录表");
        }
        TestRecordList testRecordList = new TestRecordList();
        testRecordList.setEntrustId(entrustId);
        testRecordList.setTestRecords(testRecords);
        testRecordList.setStatus(new TestRecordStatus(true, "需修改"));
        return testRecordDAO.insertTestRecordList(testRecordList).getId();
    }

    @Override
    public TestRecordList findTestRecordList(String testRecordListId, Long userId, Role userRole){
        TestRecordList testRecordList = testRecordDAO.findTestRecordListById(testRecordListId);
        if (testRecordList == null) {
            throw new TestRecordNotFoundException("该测试记录表不存在");
        }
        if (!hasAuthorityToCheck(userId, userRole, testRecordList)) {
            throw new TestRecordPermissionDeniedException("无权查看该测试记录表");
        }
        return testRecordList;
    }

    @Override
    public void updateTestRecordList(String testRecordListId, List<TestRecordList.TestRecord> testRecords, Long userId, Role userRole){
        TestRecordList testRecordList = testRecordDAO.findTestRecordListById(testRecordListId);
        if (testRecordList == null) {
            throw new TestRecordNotFoundException("该测试记录表不存在");
        }
        if (!hasAuthorityToFill(userId, userRole, testRecordList)) {
            throw new TestRecordPermissionDeniedException("无权查看该测试记录表");
        }
        if (!testRecordDAO.updateContent(testRecordListId, testRecords) || !testRecordDAO.updateStatus(testRecordListId, new TestRecordStatus(true, null))) {
            throw new TestRecordDAOFailureException("更新测试记录表失败");
        }
    }

    @Override
    public void removeTestRecordList(String testRecordListId, Long userId, Role userRole){
        if (userRole != Role.ADMIN) {
            throw new TestRecordPermissionDeniedException("无权删除测试记录表");
        }
        TestRecordList testRecordList = findTestRecordList(testRecordListId, userId, userRole);
        if (!testRecordDAO.deleteTestRecordList(testRecordList.getId())) {
            throw new TestRecordDAOFailureException("删除测试记录表失败");
        }
    }

    private boolean hasAuthorityToCreate(Role userRole) {
        /*根据调研情况，只有市场部人员、市场部主管具有新建测试记录表权限*/
        return userRole == Role.ADMIN || userRole == Role.MARKETER || userRole == Role.MARKETING_SUPERVISOR;
    }

    private boolean hasAuthorityToCheck(Long userId, Role userRole, TestRecordList testRecordList) {
        String entrustId = testRecordList.getEntrustId();
        ResponseEntity<Long> responseEntity = restTemplate.getForEntity(ENTRUST_SERVICE_URL + "/api/entrust/{entrustId}/get_testerId", Long.class, entrustId);
        if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
            Long testerId = responseEntity.getBody();
            /*根据调研情况，分配的测试部人员、测试部主管、所有质量部人员、质量部主管均有权限查阅*/
            return userRole == Role.ADMIN || (userRole == Role.TESTER && userId.equals(testerId)) || userRole == Role.TESTING_SUPERVISOR || userRole == Role.QA || userRole == Role.QA_SUPERVISOR;
        }
        return false;
    }

    private boolean hasAuthorityToFill(Long userId, Role userRole, TestRecordList testRecordList) {
        String entrustId = testRecordList.getEntrustId();
        ResponseEntity<Long> responseEntity = restTemplate.getForEntity(ENTRUST_SERVICE_URL + "/api/entrust/{entrustId}/get_testerId", Long.class, entrustId);
        if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
            Long testerId = responseEntity.getBody();
            /*根据调研情况，分配的测试部人员、测试部主管有权限修改*/
            return userRole == Role.ADMIN || (userRole == Role.TESTER && userId.equals(testerId)) || userRole == Role.TESTING_SUPERVISOR;
        }
        return false;
    }
}
