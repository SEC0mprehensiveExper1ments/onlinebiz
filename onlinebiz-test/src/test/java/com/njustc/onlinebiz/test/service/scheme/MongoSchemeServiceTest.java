package com.njustc.onlinebiz.test.service.scheme;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.Project;
import com.njustc.onlinebiz.common.model.test.project.ProjectBaseInfo;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.project.ProjectStatus;
import com.njustc.onlinebiz.common.model.test.scheme.Scheme;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.dao.scheme.SchemeDAO;
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
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());
        SchemeContent schemecontent = new SchemeContent();
        when(schemeDAO.updateContent(any(), any())).thenReturn(false);

        //开始测试
        //在SCHEME_UNFILLED阶段（合法阶段）
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

        //在SCHEME_AUDITING_DENIED阶段（合法阶段）
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
    void denyContent() {
    }

    @Test
    void approveContent() {
    }

    @Test
    void removeScheme() {
        Scheme scheme = new Scheme();
        when(schemeDAO.findSchemeById(any())).thenReturn(scheme);
        Project project = new Project();
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());
        SchemeContent schemecontent = new SchemeContent();
        when(schemeDAO.updateContent(any(), any())).thenReturn(false);
    }

    @Test
    void findScheme() {
        Scheme scheme = new Scheme();
        when(schemeDAO.findSchemeById(any())).thenReturn(scheme);
        Project project = new Project();
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());
        SchemeContent schemecontent = new SchemeContent();
        when(schemeDAO.updateContent(any(), any())).thenReturn(false);
    }
}