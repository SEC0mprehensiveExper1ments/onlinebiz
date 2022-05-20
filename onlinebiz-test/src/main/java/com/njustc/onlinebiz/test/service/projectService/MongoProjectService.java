package com.njustc.onlinebiz.test.service.projectService;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.Project;
import com.njustc.onlinebiz.test.dao.projectDAO.ProjectDAO;
import com.njustc.onlinebiz.test.exception.TestPermissionDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MongoProjectService implements ProjectService{

    private final ProjectDAO projectDAO;

    public MongoProjectService(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    @Override
    public String createTestProject(Long userId, Role userRole) {
        if(userRole!=Role.ADMIN && userRole != Role.MARKETER && userRole!=Role.MARKETING_SUPERVISOR) {
            throw new TestPermissionDeniedException("无权生成新的测试项目");
        }
        Project project = new Project();
        project.setCreatorId(userId);

        /*TODO: 根据其他部分给出的接口新建各表，并将表编号填入testProject中字段*/




        return projectDAO.insertProject(project).getId();
    }
}
