package com.njustc.onlinebiz.test.service.testissue;

import org.junit.jupiter.api.Test;
import com.njustc.onlinebiz.common.model.EntrustDto;
import com.njustc.onlinebiz.test.dao.testissue.TestIssueDAO;
import com.njustc.onlinebiz.test.service.project.ProjectService;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.exception.testissue.TestIssuePermissionDeniedException;
import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class MongoTestIssueServiceTest {

    private TestIssueService testissueservice;
    private ProjectService projectService;
    private TestIssueDAO testissueDAO;
    private static final String ENTRUST_SERVICE_URL = "http://onlinebiz-entrust";
    private RestTemplate restTemplate;
    private static String TestIssueListId1 = null;
    private static String TestIssueListId2 = null;
    private static String TestIssueListId3 = null;
    private static TestIssueList TestIssueList1 = null;
    private static TestIssueList TestIssueList2 = null;
    private static TestIssueList TestIssueList3 = null;

    @Test
    void createTestIssueList() {

        //由客户（非合法人员）创建测试问题清单
        try {
            testissueservice.createTestIssueList("P001", "E001", null, 11111L, Role.CUSTOMER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestIssuePermissionDeniedException.class));
            System.out.println("Customer try to create a test-issue list and cause a mistake.");
        }
        //由测试部员工（非合法人员）创建测试问题清单
        try {
            testissueservice.createTestIssueList("P001", "E001", null, 2001L, Role.TESTER);
        } catch (Exception e) {
            assert (e.getClass().equals(TestIssuePermissionDeniedException.class));
            System.out.println("Tester try to create a test-issue list and cause a mistake.");
        }
        //由测试部主管（非合法人员）创建测试问题清单
        try {
            testissueservice.createTestIssueList("P001", "E001", null, 2000L, Role.TESTING_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestIssuePermissionDeniedException.class));
            System.out.println("Testing supervisor try to create a test-issue list and cause a mistake.");
        }
        //由质量部员工（非合法人员）创建测试问题清单
        try {
            testissueservice.createTestIssueList("P001", "E001", null, 3001L, Role.QA);
        } catch (Exception e) {
            assert (e.getClass().equals(TestIssuePermissionDeniedException.class));
            System.out.println("QA try to create a test-issue list and cause a mistake.");
        }
        //由质量部主管（非合法人员）创建测试问题清单
        try {
            testissueservice.createTestIssueList("P001", "E001", null, 3000L, Role.QA_SUPERVISOR);
        } catch (Exception e) {
            assert (e.getClass().equals(TestIssuePermissionDeniedException.class));
            System.out.println("QA supervisor try to create a test-issue list and cause a mistake.");
        }
        //由指派的市场部员工/市场部主管（合法人员）创建测试用例表
        String url1 = ENTRUST_SERVICE_URL + "/api/entrust/" + "E001" + "/get_dto";
        ResponseEntity<EntrustDto> responseEntity1 = restTemplate.getForEntity(url1, EntrustDto.class);
        EntrustDto entrustDto1 = responseEntity1.getBody();
        TestIssueListId1 = testissueservice.createTestIssueList("P001", "E001", null, entrustDto1.getMarketerId(), Role.MARKETER);
        String url2 = ENTRUST_SERVICE_URL + "/api/entrust/" + "E002" + "/get_dto";
        ResponseEntity<EntrustDto> responseEntity2 = restTemplate.getForEntity(url2, EntrustDto.class);
        EntrustDto entrustDto2 = responseEntity2.getBody();
        TestIssueListId2 = testissueservice.createTestIssueList("P002", "E002", null, entrustDto2.getMarketerId(), Role.MARKETER);
        TestIssueListId3 = testissueservice.createTestIssueList("P003", "E003", null, 1000L, Role.MARKETING_SUPERVISOR);

        /*若非被指派的市场部员工想创建，则会在创建项目时识别出并报错，此处无需再进行检测和处理*/
    }

    @Test
    void findTestIssueList() {
    }

    @Test
    void updateTestIssueList() {
    }

    @Test
    void removeTestIssueList() {
    }
}