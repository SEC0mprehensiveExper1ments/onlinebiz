package com.njustc.onlinebiz.test.service.project;

import com.njustc.onlinebiz.common.model.EntrustDto;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.ProjectBaseInfo;
import com.njustc.onlinebiz.test.exception.project.ProjectDAOFailureException;
import com.njustc.onlinebiz.test.exception.project.ProjectNotFoundException;
import com.njustc.onlinebiz.test.exception.project.ProjectPermissionDeniedException;
import com.njustc.onlinebiz.common.model.test.project.Project;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@RunWith(SpringRunner.class)
class MongoProjectServiceTest {

    @Autowired
    private ProjectService projectService;
    private static final String ENTRUST_SERVICE_URL = "http://onlinebiz-entrust";
    private RestTemplate restTemplate;
    private static String projectId1 = null;
    private static String projectId2 = null;
    private static String projectId3 = null;

    @Test
    void createTestProject() {
        //由客户（非合法人员）创建项目
        try {
            projectService.createTestProject(11111L, Role.CUSTOMER, "E001");
        } catch (Exception e) {
            assert (e.getClass().equals(ProjectPermissionDeniedException.class));
            System.out.println("Customer try to create a project and cause a mistake.");
        }
        //由质量部员工（非合法人员）创建项目
        try {
            projectService.createTestProject(3001L, Role.QA, "E001");
        } catch (Exception e) {
            assert (e.getClass().equals(ProjectPermissionDeniedException.class));
            System.out.println("QA try to create a project and cause a mistake.");
        }
        //由质量部主管（非合法人员）创建项目
        try {
            projectService.createTestProject(3000L, Role.QA_SUPERVISOR, "E001");
        } catch (Exception e) {
            assert (e.getClass().equals(ProjectPermissionDeniedException.class));
            System.out.println("QA supervisor try to create a project and cause a mistake.");
        }
        //由测试部员工（非合法人员）创建项目
        try {
            projectService.createTestProject(2001L, Role.TESTER, "E001");
        } catch (Exception e) {
            assert (e.getClass().equals(ProjectPermissionDeniedException.class));
            System.out.println("Tester try to create a project and cause a mistake.");
        }
        //由测试部主管（非合法人员）创建项目
        try {
            projectService.createTestProject(2000L, Role.TESTING_SUPERVISOR, "E001");
        } catch (Exception e) {
            assert (e.getClass().equals(ProjectPermissionDeniedException.class));
            System.out.println("Testing supervisor try to create a project and cause a mistake.");
        }
        //由指定的市场部员工/市场部主管（合法人员）创建项目
        String url1 = ENTRUST_SERVICE_URL + "/api/entrust/" + "E001" + "/get_dto";
        ResponseEntity<EntrustDto> responseEntity1 = restTemplate.getForEntity(url1, EntrustDto.class);
        EntrustDto entrustDto1 = responseEntity1.getBody();
        if (entrustDto1 == null) {
            throw new ProjectDAOFailureException("entrustDto为空");
        }
        projectId1 = projectService.createTestProject(entrustDto1.getMarketerId(), Role.MARKETER, "E001");
        String url2 = ENTRUST_SERVICE_URL + "/api/entrust/" + "E002" + "/get_dto";
        ResponseEntity<EntrustDto> responseEntity2 = restTemplate.getForEntity(url2, EntrustDto.class);
        EntrustDto entrustDto2 = responseEntity2.getBody();
        if (entrustDto2 == null) {
            throw new ProjectDAOFailureException("entrustDto为空");
        }
        projectId2 = projectService.createTestProject(entrustDto2.getMarketerId(), Role.MARKETER, "E002");
        projectId3 = projectService.createTestProject(2000L, Role.MARKETING_SUPERVISOR, "E003");
        //由非该委托被指派的市场部员工（非合法人员）创建项目
        Long marketerId = entrustDto1.getMarketerId();
        if(marketerId.equals(1666L)) {
            marketerId = 1665L;
        }
        else {
            marketerId = 1666L;
        }
        try {
            projectService.createTestProject(marketerId, Role.MARKETER, "E001");
        } catch (Exception e) {
            assert (e.getClass().equals(ProjectPermissionDeniedException.class));
            System.out.println("Another marketer try to create a project and cause a mistake.");
        }
    }

    @Test
    void findProject() {
        Project project = null;
        //由客户（非合法人员）查看项目
        try {
            project = projectService.findProject(projectId1, 11111L, Role.CUSTOMER);
        } catch (Exception e) {
            assert (e.getClass().equals(ProjectPermissionDeniedException.class));
            System.out.println("Customer try to find a project and cause a mistake.");
        }
        //由质量部员工（合法人员）查看项目
        project = projectService.findProject(projectId1, 3001L, Role.QA);
        ProjectBaseInfo pbaseinfo1 = projectService.getProjectBaseInfo(projectId1);
        assert (project.getProjectBaseInfo().getMarketerId().equals(pbaseinfo1.getMarketerId()));
        ProjectBaseInfo pbaseinfo2 = projectService.getProjectBaseInfo(projectId2);
        project = projectService.findProject(projectId2, 3001L, Role.QA);
        assert (project.getProjectBaseInfo().getMarketerId().equals(pbaseinfo2.getMarketerId()));
        project = projectService.findProject(projectId3, 3001L, Role.QA);
        assert (project.getProjectBaseInfo().getMarketerId().equals(2000L));
        //由质量部主管（合法人员）查看项目
        project = projectService.findProject(projectId1, 3000L, Role.QA_SUPERVISOR);
        assert (project.getProjectBaseInfo().getMarketerId().equals(pbaseinfo1.getMarketerId()));
        project = projectService.findProject(projectId2, 3000L, Role.QA_SUPERVISOR);
        assert (project.getProjectBaseInfo().getMarketerId().equals(pbaseinfo2.getMarketerId()));
        project = projectService.findProject(projectId3, 3000L, Role.QA_SUPERVISOR);
        assert (project.getProjectBaseInfo().getMarketerId().equals(2000L));
        //由市场部员工（合法人员）查看项目
        project = projectService.findProject(projectId1, 1001L, Role.MARKETER);
        assert (project.getProjectBaseInfo().getMarketerId().equals(pbaseinfo1.getMarketerId()));
        project = projectService.findProject(projectId2, 1001L, Role.MARKETER);
        assert (project.getProjectBaseInfo().getMarketerId().equals(pbaseinfo2.getMarketerId()));
        project = projectService.findProject(projectId3, 1001L, Role.MARKETER);
        assert (project.getProjectBaseInfo().getMarketerId().equals(2000L));
        //由市场部主管（合法人员）查看项目
        project = projectService.findProject(projectId1, 1000L, Role.MARKETING_SUPERVISOR);
        assert (project.getProjectBaseInfo().getMarketerId().equals(pbaseinfo1.getMarketerId()));
        project = projectService.findProject(projectId2, 1000L, Role.MARKETING_SUPERVISOR);
        assert (project.getProjectBaseInfo().getMarketerId().equals(pbaseinfo2.getMarketerId()));
        project = projectService.findProject(projectId3, 1000L, Role.MARKETING_SUPERVISOR);
        assert (project.getProjectBaseInfo().getMarketerId().equals(2000L));
        //由测试部员工（合法人员）查看项目
        project = projectService.findProject(projectId1, 2001L, Role.TESTER);
        assert (project.getProjectBaseInfo().getMarketerId().equals(pbaseinfo1.getMarketerId()));
        project = projectService.findProject(projectId2, 2001L, Role.TESTER);
        assert (project.getProjectBaseInfo().getMarketerId().equals(pbaseinfo2.getMarketerId()));
        project = projectService.findProject(projectId3, 2001L, Role.TESTER);
        assert (project.getProjectBaseInfo().getMarketerId().equals(2000L));
        //由测试部主管（合法人员）查看项目
        project = projectService.findProject(projectId1, 2000L, Role.TESTING_SUPERVISOR);
        assert (project.getProjectBaseInfo().getMarketerId().equals(pbaseinfo1.getMarketerId()));
        project = projectService.findProject(projectId2, 2000L, Role.TESTING_SUPERVISOR);
        assert (project.getProjectBaseInfo().getMarketerId().equals(pbaseinfo2.getMarketerId()));
        project = projectService.findProject(projectId3, 2000L, Role.TESTING_SUPERVISOR);
        assert (project.getProjectBaseInfo().getMarketerId().equals(2000L));
        //查看一个不存在的项目id
        try {
            project = projectService.findProject("projectId4", 3001L, Role.QA);
        } catch (Exception e) {
            assert (e.getClass().equals(ProjectNotFoundException.class));
            System.out.println("Try to find a project non-existent and cause a mistake.");
        }
    }
}
