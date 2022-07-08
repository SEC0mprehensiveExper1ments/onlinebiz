package com.njustc.onlinebiz.test.service.project;

import com.njustc.onlinebiz.common.model.EntrustDto;
import com.njustc.onlinebiz.common.model.PageResult;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.*;
import com.njustc.onlinebiz.common.model.test.report.Report;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.exception.project.*;
import com.njustc.onlinebiz.test.service.report.ReportService;
import com.njustc.onlinebiz.test.service.review.EntrustTestReviewService;
import com.njustc.onlinebiz.test.service.review.ReportReviewService;
import com.njustc.onlinebiz.test.service.review.SchemeReviewService;
import com.njustc.onlinebiz.test.service.scheme.SchemeService;
import com.njustc.onlinebiz.test.service.testcase.TestcaseService;
import com.njustc.onlinebiz.test.service.testissue.TestIssueService;
import com.njustc.onlinebiz.test.service.testrecord.TestRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;


@Slf4j
@Service
public class MongoProjectService implements ProjectService {

    private final ProjectDAO projectDAO;
    private final SchemeService schemeService;
    private final SchemeReviewService schemeReviewService;
    private final TestcaseService testcaseService;
    private final TestRecordService testRecordService;
    private final ReportService reportService;
    private final EntrustTestReviewService entrustTestReviewService;
    private final ReportReviewService reportReviewService;
    private final TestIssueService testIssueService;

    private final RestTemplate restTemplate;
    private static final String ENTRUST_SERVICE = "http://onlinebiz-entrust";

    public MongoProjectService(ProjectDAO projectDAO,
                               SchemeService schemeService,
                               SchemeReviewService schemeReviewService,
                               TestcaseService testcaseService,
                               TestRecordService testRecordService,
                               ReportService reportService,
                               EntrustTestReviewService entrustTestReviewService,
                               ReportReviewService reportReviewService,
                               TestIssueService testIssueService,
                               RestTemplate restTemplate) {
        this.projectDAO = projectDAO;
        this.schemeService = schemeService;
        this.schemeReviewService = schemeReviewService;
        this.testcaseService = testcaseService;
        this.testRecordService = testRecordService;
        this.reportService = reportService;
        this.entrustTestReviewService = entrustTestReviewService;
        this.reportReviewService = reportReviewService;
        this.testIssueService = testIssueService;
        this.restTemplate = restTemplate;
    }

    @Override
    public String createTestProject(Long userId, Role userRole, String entrustId) {
        //非法人员尝试生成项目
        if (userRole != Role.ADMIN && userRole != Role.MARKETER && userRole != Role.MARKETING_SUPERVISOR) {
            throw new ProjectPermissionDeniedException("无权生成新的测试项目");
        }
        EntrustDto entrustDto;
        String params = "?userId=" + userId + "&userRole=" + userRole;
        String url = ENTRUST_SERVICE + "/api/entrust/" + entrustId + "/get_dto";
        try {
            entrustDto = restTemplate.getForObject(url + params, EntrustDto.class);
        } catch (HttpClientErrorException e) {
            throw new ProjectDAOFailureException("请求DTO失败：" + e.getResponseBodyAsString());
        }
        // 检查entrustDto非空
        if (entrustDto == null) {
            throw new ProjectDAOFailureException("entrustDto为空");
        }
        // 检查项目是否已被创建
        if (entrustDto.getProjectId() != null) {
            throw new ProjectDAOFailureException("该委托已有测试项目");
        }
        // 检查创建者匹配
        if (userRole == Role.MARKETER && !Objects.equals(userId, entrustDto.getMarketerId())) {
            throw new ProjectPermissionDeniedException("市场部人员信息不一致, 创建失败");
        }
        // 设置项目基本信息
        ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo(entrustId, entrustDto);

        // 设置项目表格信息 [在updateQa中在创建]
        // 只添加了项目的基本信息，并将项目状态初始化设置为 WAIT_FOR_QA
        Project project = new Project().setProjectBaseInfo(projectBaseInfo);
        project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, "等待质量部主管分配质量部员工"));
        // 获取项目ID
        String projectId = projectDAO.insertProject(project).getId();
        // 将项目ID注册到委托对象中
        params = "?projectId=" + projectId;
        url = ENTRUST_SERVICE + "/api/entrust/" + entrustId + "/register_project";
        try {
            restTemplate.postForEntity(url + params, null, Void.class);
        } catch (HttpClientErrorException e) {
            throw new InternalError("注册项目ID失败：" + e.getResponseBodyAsString());
        }
        return projectId;
    }

    @Override
    public Project findProject(String projectId, Long userId, Role userRole) {
        Project project = projectDAO.findProjectById(projectId);
        if (project == null) {
            throw new ProjectNotFoundException("该测试项目不存在");
        }
        ProjectStage curStage = project.getStatus().getStage();
        // 检查阶段
        if (curStage == ProjectStage.WAIT_FOR_QA) {
            throw new ProjectInvalidStageException("待分配质量部人员");
        }
        return project;
    }

    @Override
    public PageResult<ProjectOutline> findProjectOutlines(Integer page, Integer pageSize, Long userId, Role userRole) {
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

    // 该函数只是为了在项目分配质量部员工以后，创建项目中各表单，没有做权限/阶段检查，谨慎调用
    private Boolean createProjectForms(String projectId) {
        Project project = projectDAO.findProjectById(projectId);
        ProjectBaseInfo projectBaseInfo = project.getProjectBaseInfo();
        String entrustId = projectBaseInfo.getEntrustId();
        Long marketerId = projectBaseInfo.getMarketerId();
        Long testerId = projectBaseInfo.getTesterId();
        Long qaId = projectBaseInfo.getQaId();
        String serialNumber = projectBaseInfo.getSerialNumber();

        ProjectFormIds projectFormIds = new ProjectFormIds();
        /*TODO: 根据其他部分给出的接口新建各表，并将表编号填入testProject中字段，替换null*/
        /* 每个文档可以传入projectId，然后根据projectId可进行查找质量部人员Id */
        /* 如有相互关联表，可在自己的类中注入project service，然后根据其中提供的 getFormIds 找到所要关联的表单 */
        // 创建时，可以取消相关权限检查，可以默认到这一步前面都符合，表单的create函数从上述提供的人员ID参数中，各取所需即可

        // 对应的测试方案 id (JS006)
        String testSchemeId = schemeService.createScheme(entrustId, null, marketerId, Role.MARKETER, projectId);
        projectFormIds.setTestSchemeId(testSchemeId);
        Report.ReportContent reportContent = new Report.ReportContent();
        reportContent.setProjectSerialNumber(serialNumber);
        String testReportId = reportService.createReport(projectId, entrustId, reportContent, marketerId, Role.MARKETER);
        projectFormIds.setTestReportId(testReportId);
        String testcaseListId = testcaseService.createTestcaseList(projectId, entrustId, null, marketerId, Role.MARKETER);
        projectFormIds.setTestcaseListId(testcaseListId);
        String testRecordListId = testRecordService.createTestRecordList(projectId, entrustId, null, marketerId, Role.MARKETER);
        projectFormIds.setTestRecordListId(testRecordListId);
        // 对应的测试报告检查表 id (JS010)
        String testReportChecklistId = reportReviewService.createReportReview(projectId, marketerId, Role.MARKETER);
        projectFormIds.setTestReportCecklistId(testReportChecklistId);
        // 对应的测试问题清单 id (JS011)
        String testIssueListId = testIssueService.createTestIssueList(projectId, entrustId, null, marketerId, Role.MARKETER);
        projectFormIds.setTestIssueListId(testIssueListId);
        // 对应的工作检查表 id (JS012)
        String workChecklistId = entrustTestReviewService.createEntrustTestReview(projectId, marketerId, Role.MARKETER);
        projectFormIds.setWorkChecklistId(workChecklistId);
        // 对应的测试方案评审表 (JS013)
        String testSchemeChecklistId = schemeReviewService.createSchemeReview(projectId, serialNumber, qaId, testerId);
        projectFormIds.setTestSchemeChecklistId(testSchemeChecklistId);

        // 更新测试项目的formIds
        return projectDAO.updateFormIds(projectId, projectFormIds);
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
        if (!createProjectForms(projectId)) {
            throw new ProjectDAOFailureException("创建各测试表单失败");
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
    public void updateStatus(String projectId, ProjectStatus nextStatus, Long userId, Role userRole) {
        Project project = projectDAO.findProjectById(projectId);
        // 检查用户权限
        if (userRole != Role.ADMIN && userRole != Role.TESTING_SUPERVISOR && userRole != Role.QA_SUPERVISOR
            && !userId.equals(project.getProjectBaseInfo().getCustomerId())     // 项目有关的用户
            && !userId.equals(project.getProjectBaseInfo().getTesterId())       // 项目有关的测试部人员
            && !userId.equals(project.getProjectBaseInfo().getQaId())           // 项目有关的质量部人员
            && !userId.equals(project.getProjectBaseInfo().getMarketerId()))    // 项目有关的市场部人员
        {
            throw new ProjectPermissionDeniedException("无权更新项目状态");
        }

        if (userId.equals(project.getProjectBaseInfo().getCustomerId())) {
            // 测试报告已签发，待客户接受, next: REPORT_CUSTOMER_CONFIRM 或 REPORT_CUSTOMER_REJECT
            if (project.getStatus().getStage() != ProjectStage.REPORT_WAIT_CUSTOMER
                    || (nextStatus.getStage() != ProjectStage.REPORT_CUSTOMER_CONFIRM
                    && nextStatus.getStage() != ProjectStage.REPORT_CUSTOMER_REJECT)) {
                throw new ProjectInvalidStageException("更新的下一个状态不合法");
            }
            if (!projectDAO.updateStatus(project.getId(), nextStatus)) {
                throw new ProjectDAOFailureException("更改测试项目状态失败");
            }
            return;
        }

        switch (project.getStatus().getStage()) {
            case WAIT_FOR_QA:               // 等待分配质量人员（所有表不能改）, next: SCHEME_UNFILLED
                if (nextStatus.getStage() != ProjectStage.SCHEME_UNFILLED || project.getProjectBaseInfo().getQaId() == null) {
                    throw new ProjectInvalidStageException("更新的下一个状态不合法");
                }
                break;
            case SCHEME_UNFILLED:           // 测试方案未填写, next: SCHEME_AUDITING
            case SCHEME_AUDITING_DENIED:    // 测试方案经质量部审核不通过, next: SCHEME_AUDITING
                if (nextStatus.getStage() != ProjectStage.SCHEME_AUDITING) {
                    throw new ProjectInvalidStageException("更新的下一个状态不合法");
                }
                break;
            case SCHEME_AUDITING:           // 测试方案已提交，质量部可审核, next: SCHEME_AUDITING_DENIED 或 SCHEME_AUDITING_PASSED
                if (nextStatus.getStage() != ProjectStage.SCHEME_AUDITING_DENIED && nextStatus.getStage() != ProjectStage.SCHEME_AUDITING_PASSED) {
                    throw new ProjectInvalidStageException("更新的下一个状态不合法");
                }
                break;
            case SCHEME_AUDITING_PASSED:    // 测试方案经质量部审核通过, next: SCHEME_REVIEW_UPLOADED
                if (nextStatus.getStage() != ProjectStage.SCHEME_REVIEW_UPLOADED) {
                    throw new ProjectInvalidStageException("更新的下一个状态不合法");
                }
                break;
            case SCHEME_REVIEW_UPLOADED:    // 测试方案评审表已上传，进行相关测试，测试报告填写中, next: REPORT_AUDITING, 测试报告提交触发
            case REPORT_QA_DENIED:          // 测试报告经质量部审核不通过, next: REPORT_AUDITING,测试报告提交时触发
            case REPORT_CUSTOMER_REJECT:    // 测试报告被客户不接受, next: REPORT_AUDITING
                if (nextStatus.getStage() != ProjectStage.REPORT_AUDITING) {
                    throw new ProjectInvalidStageException("更新的下一个状态不合法");
                }
                break;
            case REPORT_AUDITING:           // 测试报告已提交，质量部可审核, next: REPORT_QA_DENIED 或 REPORT_QA_PASSED
                if (nextStatus.getStage() != ProjectStage.REPORT_QA_DENIED && nextStatus.getStage() != ProjectStage.REPORT_QA_PASSED) {
                    throw new ProjectInvalidStageException("更新的下一个状态不合法");
                }
                break;
            case REPORT_QA_PASSED:          // 测试报告经质量部审核通过，待上传测试报告检查表, next: REPORT_WAIT_SENT_TO_CUSTOMER
                if (nextStatus.getStage() != ProjectStage.REPORT_WAIT_SENT_TO_CUSTOMER) {
                    throw new ProjectInvalidStageException("更新的下一个状态不合法");
                }
                break;
            case REPORT_WAIT_SENT_TO_CUSTOMER: // 测试报告检查表已上传，待市场部点击向用户签发报告, next: REPORT_WAIT_CUSTOMER
                if (nextStatus.getStage() != ProjectStage.REPORT_WAIT_CUSTOMER) {
                    throw new ProjectInvalidStageException("更新的下一个状态不合法");
                }
                break;
//            case REPORT_WAIT_CUSTOMER:      // 测试报告已签发，待客户接受, next: REPORT_CUSTOMER_CONFIRM 或 REPORT_CUSTOMER_REJECT
//                if (nextStatus.getStage() != ProjectStage.REPORT_CUSTOMER_CONFIRM && nextStatus.getStage() != ProjectStage.REPORT_CUSTOMER_REJECT) {
//                    throw new ProjectInvalidStageException("更新的下一个状态不合法");
//                }
//                break;
            case REPORT_CUSTOMER_CONFIRM:   // 测试报告被客户接受，质量部可审计测试文档, next: QA_ALL_REJECTED 或 QA_ALL_PASSED
                if (nextStatus.getStage() != ProjectStage.QA_ALL_REJECTED && nextStatus.getStage() != ProjectStage.QA_ALL_PASSED) {
                    throw new ProjectInvalidStageException("更新的下一个状态不合法");
                }
                break;
            case QA_ALL_REJECTED:           // 质量部审计测试文档不合格, next: REPORT_CUSTOMER_CONFIRM
                if (nextStatus.getStage() != ProjectStage.REPORT_CUSTOMER_CONFIRM) {
                    throw new ProjectInvalidStageException("更新的下一个状态不合法");
                }
                break;
            case QA_ALL_PASSED:             // 质量部审计测试文档合格，完成（无可填，全可看）
                throw new ProjectInvalidStageException("项目已结项");
            default:
                throw new ProjectInvalidStageException("无法解析该状态或更新的下一个状态不合法");
        }
        if (!projectDAO.updateStatus(project.getId(), nextStatus)) {
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

    @Override
    public ProjectBaseInfo getProjectBaseInfo(String projectId) {
        Project project = projectDAO.findProjectById(projectId);
        if (project == null) {
            throw new ProjectNotFoundException("该测试项目不存在");
        }
        return project.getProjectBaseInfo();
    }
}
