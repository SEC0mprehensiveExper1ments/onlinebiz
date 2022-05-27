package com.njustc.onlinebiz.test.service.project;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.exception.project.ProjectDAOFailureException;
import com.njustc.onlinebiz.test.exception.project.ProjectInvalidStageException;
import com.njustc.onlinebiz.test.exception.project.ProjectNotFoundException;
import com.njustc.onlinebiz.test.model.project.Project;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.exception.project.ProjectPermissionDeniedException;
import com.njustc.onlinebiz.test.model.project.ProjectStage;
import com.njustc.onlinebiz.test.model.project.ProjectStatus;
import com.njustc.onlinebiz.test.service.review.SchemeReviewService;
import com.njustc.onlinebiz.test.service.scheme.SchemeService;
import com.njustc.onlinebiz.test.service.testcase.TestcaseService;
import com.njustc.onlinebiz.test.service.testrecord.TestRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MongoProjectService implements ProjectService {

    private final ProjectDAO projectDAO;
    private final SchemeService schemeService;
    private final SchemeReviewService schemeReviewService;
    private final TestcaseService testcaseService;
    private final TestRecordService testRecordService;

    public MongoProjectService(ProjectDAO projectDAO,
                               SchemeService schemeService,
                               SchemeReviewService schemeReviewService,
                               TestcaseService testcaseService,
                               TestRecordService testRecordService) {
        this.projectDAO = projectDAO;
        this.schemeService = schemeService;
        this.schemeReviewService = schemeReviewService;
        this.testcaseService = testcaseService;
        this.testRecordService = testRecordService;
    }

    @Override
    public String createTestProject(Long userId, Role userRole, String entrustId, Long marketerId, Long testerId) {
        if (userRole != Role.ADMIN && userRole != Role.MARKETER && userRole != Role.MARKETING_SUPERVISOR) {
            throw new ProjectPermissionDeniedException("无权生成新的测试项目");
        }
        Project project = new Project();
        project.setEntrustId(entrustId);
        project.setMarketerId(marketerId);
        project.setTesterId(testerId);
        project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, "等待质量部主管分配质量部员工"));

        /*TODO: 根据其他部分给出的接口新建各表，并将表编号填入testProject中字段，替换null*/
        // 每个文档可以传入projectId，然后根据projectId可进行查找质量部人员Id

        // 对应的测试方案 id (006)
        String testSchemeId = schemeService.createScheme(entrustId, null, userId, userRole);
        project.setTestSchemeId(testSchemeId);
        // 对应的测试报告 id (007)
        String testReportId = null;
        project.setTestReportId(testReportId);
        // 对应的测试用例表 id (008)
        String testcaseListId = testcaseService.createTestcaseList(entrustId, null, userId, userRole);
        project.setTestcaseListId(testcaseListId);
        // 对应的测试记录表 id (009)
        String testRecordListId = testRecordService.createTestRecordList(entrustId, null, userId, userRole);
        project.setTestRecordListId(testRecordListId);
        // 对应的测试报告检查表 id (010)
        String testReportChecklistId = null;
        project.setTestReportCecklistId(testReportChecklistId);
        // 对应的测试问题清单 id (011)
        String testIssueListId = null;
        project.setTestIssueListId(testIssueListId);
        // 对应的工作检查表 id (012)
        String workChecklistId = null;
        project.setWorkChecklistId(workChecklistId);
        // 对应的测试方案评审表 (013)
        String testSchemeChecklistId = schemeReviewService.createSchemeReview(testSchemeId, userId, userRole);
        project.setTestSchemeChecklistId(testSchemeChecklistId);

        return projectDAO.insertProject(project).getId();
    }

    @Override
    public Project findProject(String projectId, Long userId, Role userRole) {
        if (userRole == Role.CUSTOMER) {
            throw new ProjectPermissionDeniedException("无权查看测试项目");
        }
        Project project = projectDAO.findProjectById(projectId);
        // 检查阶段
        ProjectStage curStage = project.getStatus().getStage();
        if (curStage == ProjectStage.WAIT_FOR_QA) {
            throw new ProjectInvalidStageException("待分配质量部人员");
        }
        if (project == null) {
            throw new ProjectNotFoundException("该测试项目不存在");
        }
        return project;
    }

    @Override
    public void updateQa(String projectId, Long qaId, Long userId, Role userRole) {
        Project project = projectDAO.findProjectById(projectId);
        // 检查测试项目阶段
        ProjectStage curStage = project.getStatus().getStage();
        if (curStage != ProjectStage.WAIT_FOR_QA) {
            throw new ProjectInvalidStageException("此阶段不能分配测试部人员");
        }
        // 检查用户权限
        if (userRole != Role.ADMIN && userRole != Role.QA_SUPERVISOR) {
            throw new ProjectPermissionDeniedException("无权分配质量部人员");
        }
        // 检查数据库操作是否成功
        if (!projectDAO.updateQaId(projectId, qaId) || !projectDAO.updateStatus(projectId, new ProjectStatus(ProjectStage.IN_PROGRESS, null))) {
            throw new ProjectDAOFailureException("更新质量部人员失败");
        }
    }

    @Override
    public void removeProject(String projectId, Long userId, Role userRole) {
        if (userRole != Role.ADMIN) {
            throw new ProjectPermissionDeniedException("无权删除项目");
        }
        Project project = findProject(projectId, userId, userRole);
        if (!projectDAO.deleteProject(project.getId())) {
            throw new ProjectDAOFailureException("删除项目失败");
        }
    }

    @Override
    public void updateStatus(String projectId, ProjectStatus status, Long userId, Role userRole) {
        if (userRole != Role.ADMIN) {
            throw new ProjectPermissionDeniedException("无权更改测试项目状态");
        }
        Project project = findProject(projectId, userId, userRole);
        if (!projectDAO.updateStatus(project.getId(), status)) {
            throw new ProjectDAOFailureException("更改测试项目状态失败");
        }
    }
}
