package com.njustc.onlinebiz.test.service.project;

import com.njustc.onlinebiz.common.model.EntrustDto;
import com.njustc.onlinebiz.common.model.PageResult;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.exception.project.*;
import com.njustc.onlinebiz.test.model.project.*;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.service.review.SchemeReviewService;
import com.njustc.onlinebiz.test.service.scheme.SchemeService;
import com.njustc.onlinebiz.test.service.testcase.TestcaseService;
import com.njustc.onlinebiz.test.service.testrecord.TestRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;


@Slf4j
@Service
public class MongoProjectService implements ProjectService {

    private static final String ENTRUST_SERVICE_URL = "http://onlinebiz-entrust";
    private final RestTemplate restTemplate;
    private final ProjectDAO projectDAO;
    private final SchemeService schemeService;
    private final SchemeReviewService schemeReviewService;
    private final TestcaseService testcaseService;
    private final TestRecordService testRecordService;

    public MongoProjectService(RestTemplate restTemplate,
                               ProjectDAO projectDAO,
                               SchemeService schemeService,
                               SchemeReviewService schemeReviewService,
                               TestcaseService testcaseService,
                               TestRecordService testRecordService) {
        this.restTemplate = restTemplate;
        this.projectDAO = projectDAO;
        this.schemeService = schemeService;
        this.schemeReviewService = schemeReviewService;
        this.testcaseService = testcaseService;
        this.testRecordService = testRecordService;
    }

    @Override
    public String createTestProject(Long userId, Role userRole, String entrustId) {
        if (userRole != Role.ADMIN && userRole != Role.MARKETER && userRole != Role.MARKETING_SUPERVISOR) {
            throw new ProjectPermissionDeniedException("无权生成新的测试项目");
        }
        ResponseEntity<EntrustDto> responseEntity = restTemplate.getForEntity(ENTRUST_SERVICE_URL + "/api/entrust/{entrustId}/get_dto", EntrustDto.class, entrustId);
        // 检查委托id的有效性
        if (responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new ProjectDAOFailureException("没有找到项目对应的委托");
        }
        EntrustDto entrustDto = responseEntity.getBody();
        // 检查entrustDto非空
        if (entrustDto == null) {
            throw new ProjectDAOFailureException("entrustDto为空");
        }
        // 检查创建者匹配
        if (userRole == Role.MARKETER && !Objects.equals(userId, entrustDto.getMarketerId())) {
            throw new ProjectPermissionDeniedException("市场部人员信息不一致, 创建失败");
        }
        // 设置项目基本信息
        ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
        projectBaseInfo.setEntrustId(entrustId);
        projectBaseInfo.setMarketerId(entrustDto.getMarketerId());
        projectBaseInfo.setTesterId(entrustDto.getTesterId());
        projectBaseInfo.setSoftwareName(entrustDto.getSoftware());
        // 设置项目表格信息 [在updateStatus中在创建]
//        ProjectFormIds projectFormIds = new ProjectFormIds();
//        /* NOT_TODO: 根据其他部分给出的接口新建各表，并将表编号填入testProject中字段，替换null*/
//        /* 每个文档可以传入projectId，然后根据projectId可进行查找质量部人员Id */
//        /* 如有相互关联表，可在自己的类中注入project service，然后根据其中提供的 getFormIds 找到所要关联的表单 */
//
//        // 对应的测试方案 id (JS006)
//        String testSchemeId = schemeService.createScheme(entrustId, null, userId, userRole);
//        projectFormIds.setTestSchemeId(testSchemeId);
//        // 对应的测试报告 id (JS007)
//        String testReportId = null;
//        projectFormIds.setTestReportId(testReportId);
//        // 对应的测试用例表 id (JS008)
//        String testcaseListId = testcaseService.createTestcaseList(entrustId, null, userId, userRole);
//        projectFormIds.setTestcaseListId(testcaseListId);
//        // 对应的测试记录表 id (JS009)
//        String testRecordListId = testRecordService.createTestRecordList(entrustId, null, userId, userRole);
//        projectFormIds.setTestRecordListId(testRecordListId);
//        // 对应的测试报告检查表 id (JS010)
//        String testReportCecklistId = null;
//        projectFormIds.setTestReportCecklistId(testReportCecklistId);
//        // 对应的测试问题清单 id (JS011)
//        String testIssueListId = null;
//        projectFormIds.setTestIssueListId(testIssueListId);
//        // 对应的工作检查表 id (JS012)
//        String workChecklistId = null;
//        projectFormIds.setWorkChecklistId(workChecklistId);
//        // 对应的测试方案评审表 (JS013)
//        String testSchemeChecklistId = schemeReviewService.createSchemeReview(testSchemeId, userId, userRole);
//        projectFormIds.setTestSchemeChecklistId(testSchemeChecklistId);

        // 只添加了项目的基本信息
        Project project = new Project().setProjectBaseInfo(projectBaseInfo);
        project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, "等待质量部主管分配质量部员工"));

        return projectDAO.insertProject(project).getId();
    }

    // 该函数只是为了在项目分配质量部员工以后，创建项目中各表单，没有做权限/阶段检查，谨慎调用
    private void createProjectForms(String projectId) {
        Project project = projectDAO.findProjectById(projectId);
        ProjectBaseInfo projectBaseInfo = project.getProjectBaseInfo();
        String entrustId = projectBaseInfo.getEntrustId();
        Long marketerId = projectBaseInfo.getMarketerId();
        Long testerId = projectBaseInfo.getTesterId();
        Long qaId = projectBaseInfo.getQaId();

        ProjectFormIds projectFormIds = new ProjectFormIds();
        /*TODO: 根据其他部分给出的接口新建各表，并将表编号填入testProject中字段，替换null*/
        /* 每个文档可以传入projectId，然后根据projectId可进行查找质量部人员Id */
        /* 如有相互关联表，可在自己的类中注入project service，然后根据其中提供的 getFormIds 找到所要关联的表单 */
        // 创建时，可以取消相关权限检查，可以默认到这一步前面都符合，表单的create函数从上述提供的人员ID参数中，各取所需即可

        // 对应的测试方案 id (JS006)
//         String testSchemeId = schemeService.createScheme(entrustId, null, userId, userRole);
        String testSchemeId = null;
        projectFormIds.setTestSchemeId(testSchemeId);
        // 对应的测试报告 id (JS007)
        String testReportId = null;
        projectFormIds.setTestReportId(testReportId);
        // 对应的测试用例表 id (JS008)
//         String testcaseListId = testcaseService.createTestcaseList(entrustId, null, userId, userRole);
        String testcaseListId = null;
        projectFormIds.setTestcaseListId(testcaseListId);
        // 对应的测试记录表 id (JS009)
//        String testRecordListId = testRecordService.createTestRecordList(entrustId, null, userId, userRole);
        String testRecordListId = null;
        projectFormIds.setTestRecordListId(testRecordListId);
        // 对应的测试报告检查表 id (JS010)
        String testReportCecklistId = null;
        projectFormIds.setTestReportCecklistId(testReportCecklistId);
        // 对应的测试问题清单 id (JS011)
        String testIssueListId = null;
        projectFormIds.setTestIssueListId(testIssueListId);
        // 对应的工作检查表 id (JS012)
        String workChecklistId = null;
        projectFormIds.setWorkChecklistId(workChecklistId);
        // 对应的测试方案评审表 (JS013)
        /** 可以参考这个示例 **/
        String testSchemeChecklistId = schemeReviewService.createSchemeReview(projectId, qaId, testerId);
        projectFormIds.setTestSchemeChecklistId(testSchemeChecklistId);

        project.setProjectFormIds(projectFormIds);
        project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, "等待质量部主管分配质量部员工"));
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
    public PageResult<ProjectOutline> findProjectBaseInfos(Integer page, Integer pageSize, Long userId, Role userRole) {
        if (page <= 0 || pageSize <= 0) {
            throw new ProjectInvalidArgumentException("页号或每页大小必须为正整数");
        }
        long total;
        List<ProjectOutline> list;
        // 根据用户角色不同，返回不同结果
        if (userRole == Role.ADMIN || userRole == Role.MARKETING_SUPERVISOR
                || userRole == Role.QA_SUPERVISOR || userRole == Role.TESTING_SUPERVISOR) {
            total = projectDAO.countAll();
            list = projectDAO.findAllProjects(page, pageSize);
        }
        else if (userRole == Role.MARKETER) {
            total = projectDAO.countByMarketerId(userId);
            list = projectDAO.findProjectByMarketerId(userId, page, pageSize);
        }
        else if (userRole == Role.TESTER) {
            total = projectDAO.countByTesterId(userId);
            list = projectDAO.findProjectByTesterId(userId, page, pageSize);
        }
        else if (userRole == Role.QA) {
            total = projectDAO.countByQaId(userId);
            list = projectDAO.findProjectByQaId(userId, page, pageSize);
        }
        else {
            throw new ProjectPermissionDeniedException("无权查看测试项目列表");
        }
        return new PageResult<>(page, pageSize, total, list);
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
        if (!projectDAO.updateQaId(projectId, qaId)) {
            throw new ProjectDAOFailureException("更新质量部人员失败");
        }
        // 生成表格
        createProjectForms(projectId);
        // 更新状态
        if (!projectDAO.updateStatus(projectId, new ProjectStatus(ProjectStage.IN_PROGRESS, null))) {
            throw new ProjectDAOFailureException("更新项目状态失败");
        }
    }

    @Override
    public void removeProject(String projectId, Long userId, Role userRole) {
        if (userRole != Role.ADMIN) {
            throw new ProjectPermissionDeniedException("无权删除项目");
        }
        Project project = findProject(projectId, userId, userRole);

        // TODO: 删除项目时需先级联删除各表
        ProjectFormIds projectFormIds = project.getProjectFormIds();
        schemeReviewService.removeSchemeReview(projectFormIds.getTestSchemeChecklistId(), userId, userRole);

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

    @Override
    public ProjectFormIds getProjectFormIds(String projectId) {
        Project project = projectDAO.findProjectById(projectId);
        if (project == null) {
            throw new ProjectNotFoundException("该测试项目不存在");
        }
        return project.getProjectFormIds();
    }
}
