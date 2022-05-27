package com.njustc.onlinebiz.test.service.project;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.exception.project.ProjectNotFoundException;
import com.njustc.onlinebiz.test.exception.project.ProjectPermissionDeniedException;
import com.njustc.onlinebiz.test.model.project.Project;
import com.njustc.onlinebiz.test.model.project.ProjectStage;
import com.njustc.onlinebiz.test.model.project.ProjectStatus;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class MongoProjectServiceTest {

    @Autowired
    private ProjectService projectService;
    private static String projectId1 = null;
    private static String projectId2 = null;
    private static String projectId3 = null;

    @Test
    void createTestProject() {
        //由客户（非合法人员）创建项目
        try {
            projectService.createTestProject(123L, Role.CUSTOMER, "E001");
        } catch (Exception e) {
            assert (e.getClass().equals(ProjectPermissionDeniedException.class));
            System.out.println("Customer try to create a project and cause a mistake.");
        }
        //由质量部员工（非合法人员）创建项目
        try {
            projectService.createTestProject(3L, Role.QA, "E001");
        } catch (Exception e) {
            assert (e.getClass().equals(ProjectPermissionDeniedException.class));
            System.out.println("QA try to create a project and cause a mistake.");
        }
        //由质量部主管（非合法人员）创建项目
        try {
            projectService.createTestProject(123L, Role.QA_SUPERVISOR, "E001");
        } catch (Exception e) {
            assert (e.getClass().equals(ProjectPermissionDeniedException.class));
            System.out.println("QA supervisor try to create a project and cause a mistake.");
        }
        //由测试部员工（非合法人员）创建项目
        try {
            projectService.createTestProject(123L, Role.TESTER, "E001");
        } catch (Exception e) {
            assert (e.getClass().equals(ProjectPermissionDeniedException.class));
            System.out.println("Tester try to create a project and cause a mistake.");
        }
        //由测试部主管（非合法人员）创建项目
        try {
            projectService.createTestProject(123L, Role.TESTING_SUPERVISOR, "E001");
        } catch (Exception e) {
            assert (e.getClass().equals(ProjectPermissionDeniedException.class));
            System.out.println("Testing supervisor try to create a project and cause a mistake.");
        }
        //由市场部员工/主管（合法人员）创建项目
        projectId1 = projectService.createTestProject(1L, Role.MARKETER, "E001");
        projectId2 = projectService.createTestProject(1L, Role.MARKETER, "E002");
        projectId3 = projectService.createTestProject(2L, Role.MARKETING_SUPERVISOR, "E003");
    }

    @Test
    void findProject() {
        Project project = null;
        //由客户（非合法人员）查看项目
        try {
            project = projectService.findProject(projectId1, 4L, Role.CUSTOMER);
        } catch (Exception e) {
            assert (e.getClass().equals(ProjectPermissionDeniedException.class));
            System.out.println("Customer try to find a project and cause a mistake.");
        }
        //由质量部员工（合法人员）查看项目
        project = projectService.findProject(projectId1, 3L, Role.QA);
        assert (project.getMarketerId().equals(1L));
        project = projectService.findProject(projectId2, 3L, Role.QA);
        assert (project.getMarketerId().equals(1L));
        project = projectService.findProject(projectId3, 3L, Role.QA);
        assert (project.getMarketerId().equals(2L));
        //由质量部主管（合法人员）查看项目
        project = projectService.findProject(projectId1, 3L, Role.QA_SUPERVISOR);
        assert (project.getMarketerId().equals(1L));
        project = projectService.findProject(projectId2, 3L, Role.QA_SUPERVISOR);
        assert (project.getMarketerId().equals(1L));
        project = projectService.findProject(projectId3, 3L, Role.QA_SUPERVISOR);
        assert (project.getMarketerId().equals(2L));
        //由市场部员工（合法人员）查看项目
        project = projectService.findProject(projectId1, 3L, Role.MARKETER);
        assert (project.getMarketerId().equals(1L));
        project = projectService.findProject(projectId2, 3L, Role.MARKETER);
        assert (project.getMarketerId().equals(1L));
        project = projectService.findProject(projectId3, 3L, Role.MARKETER);
        assert (project.getMarketerId().equals(2L));
        //由市场部主管（合法人员）查看项目
        project = projectService.findProject(projectId1, 3L, Role.MARKETING_SUPERVISOR);
        assert (project.getMarketerId().equals(1L));
        project = projectService.findProject(projectId2, 3L, Role.MARKETING_SUPERVISOR);
        assert (project.getMarketerId().equals(1L));
        project = projectService.findProject(projectId3, 3L, Role.MARKETING_SUPERVISOR);
        assert (project.getMarketerId().equals(2L));
        //由测试部员工（合法人员）查看项目
        project = projectService.findProject(projectId1, 3L, Role.TESTER);
        assert (project.getMarketerId().equals(1L));
        project = projectService.findProject(projectId2, 3L, Role.TESTER);
        assert (project.getMarketerId().equals(1L));
        project = projectService.findProject(projectId3, 3L, Role.TESTER);
        assert (project.getMarketerId().equals(2L));
        //由测试部主管（合法人员）查看项目
        project = projectService.findProject(projectId1, 3L, Role.TESTING_SUPERVISOR);
        assert (project.getMarketerId().equals(1L));
        project = projectService.findProject(projectId2, 3L, Role.TESTING_SUPERVISOR);
        assert (project.getMarketerId().equals(1L));
        project = projectService.findProject(projectId3, 3L, Role.TESTING_SUPERVISOR);
        assert (project.getMarketerId().equals(2L));
        //查看一个不存在的项目id
        try {
            project = projectService.findProject("abc", 3L, Role.QA);
        } catch (Exception e) {
            assert (e.getClass().equals(ProjectNotFoundException.class));
            System.out.println("Try to find a project non-existent and cause a mistake.");
        }
    }
}