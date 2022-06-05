package com.njustc.onlinebiz.test.service.scheme;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.Project;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.project.ProjectStatus;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.dao.scheme.SchemeDAO;
import com.njustc.onlinebiz.test.exception.project.ProjectNotFoundException;
import com.njustc.onlinebiz.test.exception.scheme.SchemeDAOFailureException;
import com.njustc.onlinebiz.test.exception.scheme.SchemeInvalidStageException;
import com.njustc.onlinebiz.test.exception.scheme.SchemeNotFoundException;
import com.njustc.onlinebiz.test.exception.scheme.SchemePermissionDeniedException;
import com.njustc.onlinebiz.common.model.test.scheme.Scheme;
import com.njustc.onlinebiz.common.model.test.scheme.SchemeContent;
import com.njustc.onlinebiz.common.model.test.scheme.SchemeStage;
import com.njustc.onlinebiz.common.model.test.scheme.SchemeStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class MongoSchemeService implements SchemeService {

    private static final String PROJECT_SERVICE_URL = "http://onlinebiz-project";
    private final RestTemplate restTemplate;
    private final SchemeDAO schemeDAO;
    private final ProjectDAO projectDAO;

    public MongoSchemeService(RestTemplate restTemplate, MongoTemplate mongoTemplate, SchemeDAO schemeDAO, ProjectDAO projectDAO) {
        this.restTemplate = restTemplate;
        this.schemeDAO = schemeDAO;
        this.projectDAO = projectDAO;
    }

    @Override
    public void updateScheme(String schemeId, SchemeContent content, Long userId, Role userRole) {
        Scheme scheme = schemeDAO.findSchemeById(schemeId);
        if (scheme == null) {
            throw new SchemeNotFoundException("该测试方案不存在");
        }
        if (!hasAuthorityToFill(userId, userRole, scheme)) {
            throw new SchemePermissionDeniedException("无权修改该测试方案");
        }
        if (scheme.getStatus().getStage() == SchemeStage.QUALITY_AUDITING ||
                scheme.getStatus().getStage() == SchemeStage.AUDITING_PASSED) {
            throw new SchemeInvalidStageException("此阶段不能修改测设方案");
        }
        if (!schemeDAO.updateContent(schemeId, content) || !schemeDAO.updateStatus(schemeId, new SchemeStatus(SchemeStage.QUALITY_AUDITING, null))) {
            throw new SchemeDAOFailureException("更新测试方案失败");
        }
        if (projectDAO.updateStatus(scheme.getProjectId(), new ProjectStatus(ProjectStage.SCHEME_AUDITING, null))) {
            throw new SchemeDAOFailureException("更新项目状态失败");
        }
    }

    @Override
    public void denyContent(String schemeId, String message, Long userId, Role userRole) {
        Scheme scheme = schemeDAO.findSchemeById(schemeId);
        if (scheme == null) {
            throw new SchemeNotFoundException("该测试方案不存在");
        }
        if (!hasAuthorityToAudit(userRole)) {
            throw new SchemePermissionDeniedException("无权审核测试方案");
        }
        if (scheme.getStatus().getStage() != SchemeStage.QUALITY_AUDITING) {
            throw new SchemeInvalidStageException("此阶段不能审核测设方案");
        }
        SchemeStage nextStage = SchemeStage.AUDITING_DENIED;
        if (!schemeDAO.updateStatus(schemeId, new SchemeStatus(nextStage, message))) {
            throw new SchemeDAOFailureException("否定测试方案失败");
        }
        if (projectDAO.updateStatus(scheme.getProjectId(), new ProjectStatus(ProjectStage.SCHEME_AUDITING_DENIED, null))) {
            throw new SchemeDAOFailureException("更新项目状态失败");
        }
    }

    @Override
    public void approveContent(String schemeId, Long userId, Role userRole) {
        Scheme scheme = schemeDAO.findSchemeById(schemeId);
        if (scheme == null) {
            throw new SchemeNotFoundException("该测试方案不存在");
        }
        if (!hasAuthorityToAudit(userRole)) {
            throw new SchemePermissionDeniedException("无权审核测试方案");
        }
        if (scheme.getStatus().getStage() != SchemeStage.QUALITY_AUDITING) {
            throw new SchemeInvalidStageException("此阶段不能审核测设方案");
        }
        SchemeStage nextStage = SchemeStage.AUDITING_PASSED;
        if (!schemeDAO.updateStatus(schemeId, new SchemeStatus(nextStage, null))) {
            throw new SchemeDAOFailureException("通过测试方案失败");
        }
        if (projectDAO.updateStatus(scheme.getProjectId(), new ProjectStatus(ProjectStage.SCHEME_AUDITING_PASSED, null))) {
            throw new SchemeDAOFailureException("更新项目状态失败");
        }
    }

    @Override
    public void removeScheme(String schemeId, Long userId, Role userRole) {
        if (!hasAuthorityToRemove(userRole)) {
            throw new SchemePermissionDeniedException("无权删除测试方案");
        }
        Scheme scheme = findScheme(schemeId, userId, userRole);
        if (!schemeDAO.deleteScheme(scheme.getId())) {
            throw new SchemeDAOFailureException("删除测试方案失败");
        }
        if (projectDAO.updateStatus(scheme.getProjectId(), new ProjectStatus(ProjectStage.SCHEME_UNFILLED, null))) {
            throw new SchemeDAOFailureException("更新项目状态失败");
        }
    }

    @Override
    public String createScheme(String entrustId, SchemeContent content, Long userId, Role userRole, String projectId) {
        if (!hasAuthorityToCreate(userRole)) {
            throw new SchemePermissionDeniedException("无权新建测试方案");
        }
        Scheme scheme = new Scheme();
        scheme.setCreatorId(userId);
        scheme.setEntrustId(entrustId);
        scheme.setContent(content);
        scheme.setStatus(new SchemeStatus(SchemeStage.UNFILLED, "等待填写测试方案"));
        return schemeDAO.insertScheme(scheme).getId();
    }

    @Override
    public Scheme findScheme(String schemeId, Long userId, Role userRole) {
        Scheme scheme = schemeDAO.findSchemeById(schemeId);
        if (scheme == null) {
            throw new SchemeNotFoundException("该测试方案不存在");
        }
        if (!hasAuthorityToCheck(userId, userRole, scheme)) {
            throw new SchemePermissionDeniedException("无权查看该测试方案");
        }
        return scheme;
    }

    private boolean hasAuthorityToCreate(Role userRole) {
        /*根据调研情况，只有市场部人员、市场部主管具有新建测试方案权限*/
        return userRole == Role.ADMIN || userRole == Role.MARKETER || userRole == Role.MARKETING_SUPERVISOR;
    }

    private boolean hasAuthorityToCheck(Long userId, Role userRole, Scheme scheme) {
        String projectId = scheme.getProjectId();
        Project project = restTemplate.getForObject(PROJECT_SERVICE_URL + "/api/project/{projectId}", Project.class, projectId);
        if (project == null) {
            throw new ProjectNotFoundException("该项目不存在");
        }
        Long testerId = project.getProjectBaseInfo().getTesterId();
            /*根据调研情况，分配的测试部人员、测试部主管、所有质量部人员、质量部主管均有权限查阅*/
        return userRole == Role.ADMIN || (userRole == Role.TESTER && userId.equals(testerId)) || userRole == Role.TESTING_SUPERVISOR || userRole == Role.QA || userRole == Role.QA_SUPERVISOR;
    }

    private boolean hasAuthorityToFill(Long userId, Role userRole, Scheme scheme) {
        String projectId = scheme.getProjectId();
        Project project = restTemplate.getForObject(PROJECT_SERVICE_URL + "/api/project/{projectId}", Project.class, projectId);
        if (project == null) {
            throw new ProjectNotFoundException("该项目不存在");
        }
        Long testerId = project.getProjectBaseInfo().getTesterId();
        /*根据调研情况，分配的测试部人员、测试部主管有权限修改*/
        return userRole == Role.ADMIN || (userRole == Role.TESTER && userId.equals(testerId)) || userRole == Role.TESTING_SUPERVISOR;

    }

    private boolean hasAuthorityToRemove(Role userRole) {
        return userRole == Role.ADMIN;
    }

    private boolean hasAuthorityToAudit(Role userRole) {
        return userRole == Role.QA || userRole == Role.QA_SUPERVISOR;
    }
}
