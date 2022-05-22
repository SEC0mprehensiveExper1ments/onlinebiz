package com.njustc.onlinebiz.test.service.project;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.exception.project.ProjectNotFoundException;
import com.njustc.onlinebiz.test.model.Project;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.exception.project.ProjectPermissionDeniedException;
import com.njustc.onlinebiz.test.service.scheme.SchemeService;
import com.njustc.onlinebiz.test.service.testcase.TestcaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MongoProjectService implements ProjectService {

    private final ProjectDAO projectDAO;
    private final SchemeService schemeService;
    private final TestcaseService testcaseService;

    public MongoProjectService(ProjectDAO projectDAO, SchemeService schemeService,TestcaseService testcaseService) {
        this.projectDAO = projectDAO;
        this.schemeService = schemeService;
        this.testcaseService = testcaseService;
    }

    @Override
    public String createTestProject(Long userId, Role userRole, String entrustId) {
        if (userRole != Role.ADMIN && userRole != Role.MARKETER && userRole != Role.MARKETING_SUPERVISOR) {
            throw new ProjectPermissionDeniedException("无权生成新的测试项目");
        }
        Project project = new Project();
        project.setCreatorId(userId);
        project.setFinished(false);

        /*TODO: 根据其他部分给出的接口新建各表，并将表编号填入testProject中字段*/
        String schemeId = schemeService.createScheme(entrustId, null, userId, userRole);
        project.setTestSchemeId(schemeId);
        String testcaseId=testcaseService.createTestcaseList(entrustId,null,userId,userRole);
        project.setTestcaseListId(testcaseId);

        return projectDAO.insertProject(project).getId();
    }

    @Override
    public Project findProject(String projectId, Long userId, Role userRole) {
        Project project = projectDAO.findProjectById(projectId);
        if (project == null) {
            throw new ProjectNotFoundException("该测试项目不存在");
        }
        if (userRole == Role.CUSTOMER) {
            throw new ProjectPermissionDeniedException("无权查看测试项目");
        }
        return project;
    }
}
