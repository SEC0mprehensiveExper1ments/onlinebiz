package com.njustc.onlinebiz.test.service.scheme;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.Project;
import com.njustc.onlinebiz.common.model.test.project.ProjectBaseInfo;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.project.ProjectStatus;
import com.njustc.onlinebiz.common.model.test.scheme.Scheme;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.dao.scheme.SchemeDAO;
import com.njustc.onlinebiz.test.exception.scheme.SchemeDAOFailureException;
import com.njustc.onlinebiz.test.exception.scheme.SchemeInvalidStageException;
import com.njustc.onlinebiz.test.exception.scheme.SchemeNotFoundException;
import com.njustc.onlinebiz.test.exception.scheme.SchemePermissionDeniedException;
import com.njustc.onlinebiz.common.model.test.scheme.Modification;
import com.njustc.onlinebiz.common.model.test.scheme.Schedule;
import com.njustc.onlinebiz.common.model.test.scheme.SchemeContent;
import com.njustc.onlinebiz.test.service.project.ProjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class MongoSchemeServiceTest {

    private final SchemeDAO schemeDAO = mock(SchemeDAO.class);
    private final ProjectDAO projectDAO = mock(ProjectDAO.class);
    private final MongoSchemeService schemeservice = new MongoSchemeService(schemeDAO, projectDAO);
    private final ProjectBaseInfo projectbaseinfo = mock(ProjectBaseInfo.class);
    private final ProjectService projectService = mock(ProjectService.class);
    private static final String ENTRUST_SERVICE_URL = "http://onlinebiz-entrust";

    @Test
    void updateScheme() {

        Scheme scheme = new Scheme();
        when(schemeDAO.findSchemeById(any())).thenReturn(scheme);
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_UNFILLED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setMarketerId(1001L);
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());
        SchemeContent schemecontent = new SchemeContent();
        when(schemeDAO.updateContent(any(), any())).thenReturn(false);

        //开始测试
        //在SCHEME_UNFILLED阶段尝试修改测试方案（合法阶段）
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_UNFILLED, ""));
        when(projectDAO.findProjectById(any())).thenReturn((project));
        //由客户（非法人员）更新测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 11111L, Role.CUSTOMER));
        //由市场部员工（非法人员）更新测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 1001L, Role.MARKETER));
        //由市场部主管（非法人员）更新测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 1000L, Role.MARKETING_SUPERVISOR));
        //由质量部员工（非法人员）更新测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 3001L, Role.QA));
        //由质量部主管（非法人员）更新测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 3000L, Role.QA_SUPERVISOR));
        //由非指派的测试部员工（非法人员）更新测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 2066L, Role.TESTER));

        //由指派的测试部员工/测试部主管（合法人员）更新测试方案
        when(schemeDAO.updateContent(any(),any())).thenReturn(true);
        Assertions.assertDoesNotThrow(() ->
                schemeservice.updateScheme("schemeId", schemecontent, 2001L, Role.TESTER));
        Assertions.assertDoesNotThrow(() ->
                schemeservice.updateScheme("schemeId", schemecontent, 2000L, Role.TESTING_SUPERVISOR));
        when(schemeDAO.updateContent(any(), any())).thenReturn(false);
        Assertions.assertThrows(
                SchemeDAOFailureException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                SchemeDAOFailureException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 2001L, Role.TESTER));

        //尝试更新不存在的测试方案
        when(schemeDAO.findSchemeById(any())).thenReturn(null);
        Assertions.assertThrows(
                SchemeNotFoundException.class,
                () -> schemeservice.updateScheme("schemeId1", schemecontent, 2001L, Role.TESTER));
        Assertions.assertThrows(
                SchemeNotFoundException.class,
                () -> schemeservice.updateScheme("schemeId2", schemecontent, 2001L, Role.TESTER));
        Assertions.assertThrows(
                SchemeNotFoundException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 2000L, Role.TESTING_SUPERVISOR));
        when(schemeDAO.findSchemeById(any())).thenReturn(scheme);

        //在SCHEME_AUDITING_DENIED阶段尝试修改测试方案（合法阶段）
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING_DENIED, ""));
        when(projectDAO.findProjectById(any())).thenReturn((project));
        when(schemeDAO.findSchemeById(any())).thenReturn(scheme);
        //由客户（非法人员）更新测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 11111L, Role.CUSTOMER));
        //由市场部员工（非法人员）更新测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 1001L, Role.MARKETER));
        //由市场部主管（非法人员）更新测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 1000L, Role.MARKETING_SUPERVISOR));
        //由质量部员工（非法人员）更新测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 3001L, Role.QA));
        //由质量部主管（非法人员）更新测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 3000L, Role.QA_SUPERVISOR));
        //由非指派的测试部员工（非法人员）更新测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 2066L, Role.TESTER));

        //由指派的测试部员工/测试部主管（合法人员）更新测试方案
        when(schemeDAO.updateContent(any(),any())).thenReturn(true);
        Assertions.assertDoesNotThrow(() ->
                schemeservice.updateScheme("schemeId", schemecontent, 2001L, Role.TESTER));
        Assertions.assertDoesNotThrow(() ->
                schemeservice.updateScheme("schemeId", schemecontent, 2000L, Role.TESTING_SUPERVISOR));
        when(schemeDAO.updateContent(any(), any())).thenReturn(false);
        Assertions.assertThrows(
                SchemeDAOFailureException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                SchemeDAOFailureException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 2001L, Role.TESTER));

        //尝试更新不存在的测试方案
        when(schemeDAO.findSchemeById(any())).thenReturn(null);
        Assertions.assertThrows(
                SchemeNotFoundException.class,
                () -> schemeservice.updateScheme("schemeId1", schemecontent, 2001L, Role.TESTER));
        Assertions.assertThrows(
                SchemeNotFoundException.class,
                () -> schemeservice.updateScheme("schemeId2", schemecontent, 2001L, Role.TESTER));
        Assertions.assertThrows(
                SchemeNotFoundException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 2000L, Role.TESTING_SUPERVISOR));
        when(schemeDAO.findSchemeById(any())).thenReturn(scheme);

        //尝试在不允许修改测试方案的阶段修改测试方案
        //在质量部审核测试方案阶段（SCHEME_AUDITING）尝试修改
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING, ""));
        when(projectDAO.findProjectById(any())).thenReturn((project));
        when(schemeDAO.findSchemeById(any())).thenReturn(scheme);
        when(schemeDAO.updateContent(any(),any())).thenReturn(true);
        Assertions.assertThrows(
                SchemeInvalidStageException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 2001L, Role.TESTER));
        Assertions.assertThrows(
                SchemeInvalidStageException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 2000L, Role.TESTING_SUPERVISOR));

        //在测试方案审核已通过（SCHEME_AUDITING_PASSED）尝试修改
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING_PASSED, ""));
        when(projectDAO.findProjectById(any())).thenReturn((project));
        when(schemeDAO.findSchemeById(any())).thenReturn(scheme);
        when(schemeDAO.updateContent(any(),any())).thenReturn(true);
        Assertions.assertThrows(
                SchemeInvalidStageException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 2001L, Role.TESTER));
        Assertions.assertThrows(
                SchemeInvalidStageException.class,
                () -> schemeservice.updateScheme("schemeId", schemecontent, 2000L, Role.TESTING_SUPERVISOR));
    }

    @Test
    void createScheme() {
        String SchemeId = new String();
        Scheme scheme = new Scheme();
        scheme.setId(SchemeId);
        when(schemeDAO.findSchemeById(any())).thenReturn(scheme);
        when(schemeDAO.insertScheme(any())).thenReturn(scheme);
        Project project = new Project();
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setMarketerId(1001L);
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());
        SchemeContent schemecontent = new SchemeContent();

        //开始测试
        //由客户（非合法人员）创建测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.createScheme("entrustId", schemecontent, 11111L, Role.CUSTOMER, "ProjectId"));
        //由测试部员工（非合法人员）创建测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.createScheme("entrustId", schemecontent, 2001L, Role.TESTER, "ProjectId"));
        //由测试部主管（非合法人员）创建测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.createScheme("entrustId", schemecontent, 2000L, Role.TESTING_SUPERVISOR, "ProjectId"));
        //由质量部员工（非合法人员）创建测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.createScheme("entrustId", schemecontent, 3001L, Role.QA, "ProjectId"));
        //由质量部主管（非合法人员）创建测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.createScheme("entrustId", schemecontent, 3000L, Role.QA_SUPERVISOR, "ProjectId"));
        //由非指派的测试部员工（非合法人员）创建测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.createScheme("entrustId", schemecontent, 2066L, Role.TESTER, "ProjectId"));

        //由ADMIN/指派的市场部员工/市场部主管（合法人员）创建测试方案
        Assertions.assertDoesNotThrow(
                () -> schemeservice.createScheme("entrustId", schemecontent, 1001L, Role.MARKETER, "ProjectId"));
        Assertions.assertDoesNotThrow(
                () -> schemeservice.createScheme("entrustId", schemecontent, 1000L, Role.MARKETING_SUPERVISOR, "ProjectId"));
        Assertions.assertDoesNotThrow(
                () -> schemeservice.createScheme("entrustId", schemecontent, 0L, Role.ADMIN, "ProjectId"));
    }

    @Test
    void removeScheme() {
        Scheme scheme = new Scheme();
        when(schemeDAO.findSchemeById(any())).thenReturn(scheme);
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setMarketerId(1001L);
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());
        SchemeContent schemecontent = new SchemeContent();
        when(schemeDAO.updateContent(any(), any())).thenReturn(false);

        //开始测试
        //由客户（非合法人员）删除测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.removeScheme("SchemeId", 11111L, Role.CUSTOMER));
        //由测试部员工（非合法人员）删除测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.removeScheme("SchemeId", 2001L, Role.TESTER));
        //由测试部主管（非合法人员）删除测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.removeScheme("SchemeId", 2000L, Role.TESTING_SUPERVISOR));
        //由质量部员工（非合法人员）删除测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.removeScheme("SchemeId", 3001L, Role.QA));
        //由质量部主管（非合法人员）删除测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.removeScheme("SchemeId", 3000L, Role.QA_SUPERVISOR));
        //由非指派的市场部员工（非法人员）删除测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.removeScheme("SchemeId", 1066L, Role.MARKETER));
        //由指派的市场部员工（非法人员）删除测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.removeScheme("SchemeId", 1001L, Role.MARKETER));
        //由市场部主管（非法人员）删除测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.removeScheme("SchemeId", 1000L, Role.MARKETING_SUPERVISOR));

        //由ADMIN（合法人员）删除测试方案
        when(schemeDAO.deleteScheme(any())).thenReturn(true);
        Assertions.assertDoesNotThrow(() ->
                schemeservice.removeScheme("SchemeId", 0L, Role.ADMIN));
        when(schemeDAO.deleteScheme(any())).thenReturn(false);
        Assertions.assertThrows(
                SchemeDAOFailureException.class,
                () -> schemeservice.removeScheme("SchemeId", 0L, Role.ADMIN));
    }

    @Test
    void findScheme() {
        Scheme scheme = new Scheme();
        when(schemeDAO.findSchemeById(any())).thenReturn(scheme);
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setMarketerId(1001L);
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());
        SchemeContent schemecontent = new SchemeContent();
        when(schemeDAO.updateContent(any(), any())).thenReturn(false);

        //开始测试
        //在SCHEME_AUDITING状态（合法状态）查找测试方案
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        //由客户（非法人员）查找测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.findScheme("SchemeId", 11111L, Role.CUSTOMER));
        //由市场部员工（非法人员）查找测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.findScheme("SchemeId", 1001L, Role.MARKETER));
        //由市场部员工（非法人员）查找测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.findScheme("SchemeId", 1066L, Role.MARKETER));
        //由市场部主管（非法人员）查找测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.findScheme("SchemeId", 1000L, Role.MARKETING_SUPERVISOR));
        //由非指派的测试部员工（非法人员）查找测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.findScheme("SchemeId", 2066L, Role.TESTER));
        //由非指派的质量部员工（非法人员）查找测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.findScheme("SchemeId", 3066L, Role.QA));

        //由指派的测试部员工（合法人员）查找测试方案
        Assertions.assertDoesNotThrow(
                () -> schemeservice.findScheme("SchemeId", 2001L, Role.TESTER));
        //由测试部主管（合法人员）查找测试方案
        Assertions.assertDoesNotThrow(
                () -> schemeservice.findScheme("SchemeId", 2000L, Role.TESTING_SUPERVISOR));
        //由分配的质量部员工（合法人员）查找测试方案
        Assertions.assertDoesNotThrow(
                () -> schemeservice.findScheme("SchemeId", 3001L, Role.QA));
        //由质量部主管（合法人员）查找测试方案
        Assertions.assertDoesNotThrow(
                () -> schemeservice.findScheme("SchemeId", 3000L, Role.QA_SUPERVISOR));

        //尝试查找不存在的测试方案
        when(schemeDAO.findSchemeById(any())).thenReturn(null);
        Assertions.assertThrows(
                SchemeNotFoundException.class,
                () -> schemeservice.findScheme("SchemeId", 2001L, Role.TESTER));
        Assertions.assertThrows(
                SchemeNotFoundException.class,
                () -> schemeservice.findScheme("SchemeId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                SchemeNotFoundException.class,
                () -> schemeservice.findScheme("SchemeId", 3001L, Role.QA));
        Assertions.assertThrows(
                SchemeNotFoundException.class,
                () -> schemeservice.findScheme("SchemeId", 3000L, Role.QA_SUPERVISOR));
        when(schemeDAO.findSchemeById(any())).thenReturn(scheme);

        //在SCHEME_AUDITING_DENIED状态（合法状态）查找测试方案
        when(schemeDAO.findSchemeById(any())).thenReturn(scheme);
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING_DENIED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        //由客户（非法人员）查找测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.findScheme("SchemeId", 11111L, Role.CUSTOMER));
        //由市场部员工（非法人员）查找测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.findScheme("SchemeId", 1001L, Role.MARKETER));
        //由市场部员工（非法人员）查找测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.findScheme("SchemeId", 1066L, Role.MARKETER));
        //由市场部主管（非法人员）查找测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.findScheme("SchemeId", 1000L, Role.MARKETING_SUPERVISOR));
        //由非指派的测试部员工（非法人员）查找测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.findScheme("SchemeId", 2066L, Role.TESTER));
        //由非指派的质量部员工（非法人员）查找测试方案
        Assertions.assertThrows(
                SchemePermissionDeniedException.class,
                () -> schemeservice.findScheme("SchemeId", 3066L, Role.QA));

        //由指派的测试部员工（合法人员）查找测试方案
        Assertions.assertDoesNotThrow(
                () -> schemeservice.findScheme("SchemeId", 2001L, Role.TESTER));
        //由测试部主管（合法人员）查找测试方案
        Assertions.assertDoesNotThrow(
                () -> schemeservice.findScheme("SchemeId", 2000L, Role.TESTING_SUPERVISOR));
        //由指派的质量部员工（合法人员）查找测试方案
        Assertions.assertDoesNotThrow(
                () -> schemeservice.findScheme("SchemeId", 3001L, Role.QA));
        //由质量部主管（合法人员）查找测试方案
        Assertions.assertDoesNotThrow(
                () -> schemeservice.findScheme("SchemeId", 3000L, Role.QA_SUPERVISOR));

        //尝试查找不存在的测试方案
        when(schemeDAO.findSchemeById(any())).thenReturn(null);
        Assertions.assertThrows(
                SchemeNotFoundException.class,
                () -> schemeservice.findScheme("SchemeId", 2001L, Role.TESTER));
        Assertions.assertThrows(
                SchemeNotFoundException.class,
                () -> schemeservice.findScheme("SchemeId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                SchemeNotFoundException.class,
                () -> schemeservice.findScheme("SchemeId", 3001L, Role.QA));
        Assertions.assertThrows(
                SchemeNotFoundException.class,
                () -> schemeservice.findScheme("SchemeId", 3000L, Role.QA_SUPERVISOR));
        when(schemeDAO.findSchemeById(any())).thenReturn(scheme);

        //尝试在阶段WAIT_FOR_QA（非法阶段之一）查找测试方案
        when(schemeDAO.findSchemeById(any())).thenReturn(scheme);
        project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        Assertions.assertThrows(
                SchemeInvalidStageException.class,
                () -> schemeservice.findScheme("SchemeId", 2001L, Role.TESTER));
        Assertions.assertThrows(
                SchemeInvalidStageException.class,
                () -> schemeservice.findScheme("SchemeId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                SchemeInvalidStageException.class,
                () -> schemeservice.findScheme("SchemeId", 3001L, Role.QA));
        Assertions.assertThrows(
                SchemeInvalidStageException.class,
                () -> schemeservice.findScheme("SchemeId", 3000L, Role.QA_SUPERVISOR));

        //尝试在阶段SCHEME_UNFILLED（非法阶段之二）查找测试方案
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_UNFILLED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        Assertions.assertThrows(
                SchemeInvalidStageException.class,
                () -> schemeservice.findScheme("SchemeId", 2001L, Role.TESTER));
        Assertions.assertThrows(
                SchemeInvalidStageException.class,
                () -> schemeservice.findScheme("SchemeId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                SchemeInvalidStageException.class,
                () -> schemeservice.findScheme("SchemeId", 3001L, Role.QA));
        Assertions.assertThrows(
                SchemeInvalidStageException.class,
                () -> schemeservice.findScheme("SchemeId", 3000L, Role.QA_SUPERVISOR));
    }
}