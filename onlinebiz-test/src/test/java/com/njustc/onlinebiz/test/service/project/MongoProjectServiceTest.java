package com.njustc.onlinebiz.test.service.project;

import com.njustc.onlinebiz.common.model.EntrustDto;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.*;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MongoProjectServiceTest {
  private final ProjectDAO projectDAO = mock(ProjectDAO.class);
  private final SchemeService schemeService = mock(SchemeService.class);
  private final SchemeReviewService schemeReviewService = mock(SchemeReviewService.class);
  private final TestcaseService testcaseService = mock(TestcaseService.class);
  private final TestRecordService testRecordService = mock(TestRecordService.class);
  private final ReportService reportService = mock(ReportService.class);
  private final RestTemplate restTemplate = mock(RestTemplate.class);
  private final EntrustTestReviewService entrustTestReviewService =
      mock(EntrustTestReviewService.class);
  private final ReportReviewService reportReviewService = mock(ReportReviewService.class);
  private final TestIssueService testIssueService = mock(TestIssueService.class);
  private final MongoProjectService mongoProjectService =
      new MongoProjectService(
          projectDAO,
          schemeService,
          schemeReviewService,
          testcaseService,
          testRecordService,
          reportService,
          entrustTestReviewService,
          reportReviewService,
          testIssueService,
          restTemplate);

  @Test
  void createTestProjectByMarketer() {
    EntrustDto entrustDto = new EntrustDto();
    entrustDto.setMarketerId(2L);
    Project project = new Project();
    project.setId("projectId");

    when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(entrustDto);
    when(projectDAO.insertProject(any())).thenReturn(project);
    when(restTemplate.postForEntity(any(), any(), any())).thenReturn(null);

    Assertions.assertEquals(
        "projectId", mongoProjectService.createTestProject(2L, Role.MARKETER, "entrustId"));
  }

  @Test
  void createTestProjectByCustomer() {
    EntrustDto entrustDto = new EntrustDto();
    entrustDto.setMarketerId(2L);
    Project project = new Project();
    project.setId("projectId");

    when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(entrustDto);
    when(projectDAO.insertProject(any())).thenReturn(project);
    when(restTemplate.postForEntity(any(), any(), any())).thenReturn(null);

    Assertions.assertThrows(
        ProjectPermissionDeniedException.class,
        () -> mongoProjectService.createTestProject(2L, Role.CUSTOMER, "entrustId"));
  }

  @Test
  void createTestProjectByInvalidMarketer() {
    EntrustDto entrustDto = new EntrustDto();
    entrustDto.setMarketerId(2L);
    Project project = new Project();
    project.setId("projectId");

    when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(entrustDto);
    when(projectDAO.insertProject(any())).thenReturn(project);
    when(restTemplate.postForEntity(any(), any(), any())).thenReturn(null);

    Assertions.assertThrows(
        ProjectPermissionDeniedException.class,
        () -> mongoProjectService.createTestProject(1L, Role.MARKETER, "entrustId"));
  }

  @Test
  void createProjectDtoNull() {
    Project project = new Project();
    project.setId("projectId");

    when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(null);
    when(projectDAO.insertProject(any())).thenReturn(project);
    when(restTemplate.postForEntity(any(), any(), any())).thenReturn(null);

    Assertions.assertThrows(
        ProjectDAOFailureException.class,
        () -> mongoProjectService.createTestProject(2L, Role.MARKETER, "entrustId"));
  }

  @Test
  void findProjectByCustomer() {
    Project project = new Project();
    project.setStatus(new ProjectStatus(ProjectStage.REPORT_WAIT_CUSTOMER, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertDoesNotThrow(
        () -> mongoProjectService.findProject("projectId", 0L, Role.CUSTOMER));
  }

  @Test
  void findProjectWaitForQA() {
    Project project = new Project();
    project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectInvalidStageException.class,
        () -> mongoProjectService.findProject("projectId", 0L, Role.QA));
  }

  @Test
  void findProjectDaoNull() {
    when(projectDAO.findProjectById(any())).thenReturn(null);
    Assertions.assertThrows(
        ProjectNotFoundException.class,
        () -> mongoProjectService.findProject("projectId", 0L, Role.QA));
  }

  @Test
  void findProjectSuccess() {
    Project project = new Project();
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_UNFILLED, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertDoesNotThrow(
        () -> {
          mongoProjectService.findProject("projectId", 0L, Role.QA);
        });
  }

  @Test
  void findProjectOutlinesInvalidPage() {
    Assertions.assertThrows(
        ProjectInvalidArgumentException.class,
        () -> mongoProjectService.findProjectOutlines(0, 1, 0L, Role.QA));
    Assertions.assertThrows(
        ProjectInvalidArgumentException.class,
        () -> mongoProjectService.findProjectOutlines(-1, 1, 0L, Role.QA));
  }

  @Test
  void findProjectOutlinesInvalidPageSize() {
    Assertions.assertThrows(
        ProjectInvalidArgumentException.class,
        () -> mongoProjectService.findProjectOutlines(1, 0, 0L, Role.QA));
    Assertions.assertThrows(
        ProjectInvalidArgumentException.class,
        () -> mongoProjectService.findProjectOutlines(1, -1, 0L, Role.QA));
  }

  @Test
  void findProjectOutlinesSupervisor() {
    when(projectDAO.countAll()).thenReturn(1L);
    List<ProjectOutline> projectOutlines = new ArrayList<>();
    when(projectDAO.findAllProjects(any(), any())).thenReturn(projectOutlines);
    Assertions.assertDoesNotThrow(
        () -> {
          mongoProjectService.findProjectOutlines(1, 1, 0L, Role.ADMIN);
        });
  }

  @Test
  void findProjectOutlinesMarketer() {
    when(projectDAO.countByMarketerId(any())).thenReturn(1L);
    List<ProjectOutline> projectOutlines = new ArrayList<>();
    when(projectDAO.findAllProjects(any(), any())).thenReturn(projectOutlines);
    Assertions.assertDoesNotThrow(
        () -> {
          mongoProjectService.findProjectOutlines(1, 1, 0L, Role.MARKETER);
        });
  }

  @Test
  void findProjectOutlinesTester() {
    when(projectDAO.countByTesterId(any())).thenReturn(1L);
    List<ProjectOutline> projectOutlines = new ArrayList<>();
    when(projectDAO.findAllProjects(any(), any())).thenReturn(projectOutlines);
    Assertions.assertDoesNotThrow(
        () -> {
          mongoProjectService.findProjectOutlines(1, 1, 0L, Role.TESTER);
        });
  }

  @Test
  void findProjectOutlinesQA() {
    when(projectDAO.countByQaId(any())).thenReturn(1L);
    List<ProjectOutline> projectOutlines = new ArrayList<>();
    when(projectDAO.findAllProjects(any(), any())).thenReturn(projectOutlines);
    Assertions.assertDoesNotThrow(
        () -> {
          mongoProjectService.findProjectOutlines(1, 1, 0L, Role.QA);
        });
  }

  @Test
  void findProjectOutlinesCustomer() {
    Assertions.assertThrows(
        ProjectPermissionDeniedException.class,
        () -> mongoProjectService.findProjectOutlines(1, 1, 0L, Role.CUSTOMER));
  }

  @Test
  void updateQaInvalidStage() {
    Project project = new Project();
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_UNFILLED, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectInvalidStageException.class,
        () -> mongoProjectService.updateQa("projectId", 0L, 0L, Role.QA));
  }

  @Test
  void updateQaBySuccess() {
    Project project = new Project();
    project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
    project.setProjectBaseInfo(new ProjectBaseInfo());
    when(projectDAO.findProjectById(any())).thenReturn(project);
    when(projectDAO.updateQaId(any(), any())).thenReturn(true);
    when(projectDAO.updateFormIds(any(), any())).thenReturn(true);
    Assertions.assertDoesNotThrow(
        () -> mongoProjectService.updateQa("projectId", 0L, 0L, Role.QA_SUPERVISOR));
  }

  @Test
  void updateQaWithoutPermission() {
    Project project = new Project();
    project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
    project.setProjectBaseInfo(new ProjectBaseInfo());
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectPermissionDeniedException.class,
        () -> mongoProjectService.updateQa("projectId", 0L, 0L, Role.CUSTOMER));
    Assertions.assertThrows(
        ProjectPermissionDeniedException.class,
        () -> mongoProjectService.updateQa("projectId", 0L, 0L, Role.TESTING_SUPERVISOR));
  }

  @Test
  void removeProjectWithoutPermission() {
    Assertions.assertThrows(
        ProjectPermissionDeniedException.class,
        () -> mongoProjectService.removeProject("projectId", 0L, Role.CUSTOMER));
    Assertions.assertThrows(
        ProjectPermissionDeniedException.class,
        () -> mongoProjectService.removeProject("projectId", 0L, Role.TESTING_SUPERVISOR));
  }

  @Test
  void removeProjectSuccess() {
    Project project = new Project();
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
    project.setProjectFormIds(new ProjectFormIds());
    when(projectDAO.findProjectById(any())).thenReturn(project);
    when(projectDAO.deleteProject(any())).thenReturn(true);
    Assertions.assertDoesNotThrow(
        () -> mongoProjectService.removeProject("projectId", 0L, Role.ADMIN));
  }

  @Test
  void removeProjectDaoFailure() {
    Project project = new Project();
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
    project.setProjectFormIds(new ProjectFormIds());
    when(projectDAO.findProjectById(any())).thenReturn(project);
    when(projectDAO.deleteProject(any())).thenReturn(false);
    Assertions.assertThrows(
        ProjectDAOFailureException.class,
        () -> mongoProjectService.removeProject("projectId", 0L, Role.ADMIN));
  }

  @Test
  void updateStatusInvalidCustomer() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectPermissionDeniedException.class,
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""),
                0L,
                Role.CUSTOMER));
  }

  @Test
  void updateStatusInvalidMarketer() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    projectBaseInfo.setMarketerId(1L);
    projectBaseInfo.setTesterId(1L);
    projectBaseInfo.setQaId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectPermissionDeniedException.class,
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""),
                0L,
                Role.MARKETER));
  }

  @Test
  void updateStatusInvalidQa() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    projectBaseInfo.setMarketerId(1L);
    projectBaseInfo.setTesterId(1L);
    projectBaseInfo.setQaId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectPermissionDeniedException.class,
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""),
                0L,
                Role.QA));
  }

  @Test
  void updateStatusWhenWaitForQaWithoutQaId() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    projectBaseInfo.setMarketerId(1L);
    projectBaseInfo.setTesterId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectInvalidStageException.class,
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.SCHEME_UNFILLED, ""),
                0L,
                Role.QA_SUPERVISOR));
  }

  @Test
  void updateStatusSuccess() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    projectBaseInfo.setMarketerId(1L);
    projectBaseInfo.setTesterId(1L);
    projectBaseInfo.setQaId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    when(projectDAO.updateStatus(any(), any())).thenReturn(true);
    Assertions.assertDoesNotThrow(
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.SCHEME_UNFILLED, ""),
                0L,
                Role.QA_SUPERVISOR));
  }

  @Test
  void updateStatusWhenSchemeUnfilledNextInvalidStatus() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    projectBaseInfo.setMarketerId(1L);
    projectBaseInfo.setTesterId(1L);
    projectBaseInfo.setQaId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_UNFILLED, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectInvalidStageException.class,
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""),
                0L,
                Role.QA_SUPERVISOR));
  }

  @Test
  void updateStatusWhenSchemeAuditingNextInvalidStatus() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    projectBaseInfo.setMarketerId(1L);
    projectBaseInfo.setTesterId(1L);
    projectBaseInfo.setQaId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectInvalidStageException.class,
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""),
                0L,
                Role.QA_SUPERVISOR));
  }

  @Test
  void updateStatusWhenSchemeAuditingPassedNextInvalidStatus() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    projectBaseInfo.setMarketerId(1L);
    projectBaseInfo.setTesterId(1L);
    projectBaseInfo.setQaId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING_PASSED, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectInvalidStageException.class,
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.SCHEME_AUDITING_DENIED, ""),
                0L,
                Role.QA_SUPERVISOR));
  }

  @Test
  void updateStatusWhenReportQaDeniedNextInvalidStatus() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    projectBaseInfo.setMarketerId(1L);
    projectBaseInfo.setTesterId(1L);
    projectBaseInfo.setQaId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.REPORT_QA_DENIED, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectInvalidStageException.class,
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.REPORT_QA_PASSED, ""),
                0L,
                Role.QA_SUPERVISOR));
  }

  @Test
  void updateStatusWhenReportAuditingNextInvalidStatus() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    projectBaseInfo.setMarketerId(1L);
    projectBaseInfo.setTesterId(1L);
    projectBaseInfo.setQaId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.REPORT_AUDITING, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectInvalidStageException.class,
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.REPORT_WAIT_SENT_TO_CUSTOMER, ""),
                0L,
                Role.QA_SUPERVISOR));
  }

  @Test
  void updateStatusWhenReportQaPassedNextInvalidStatus() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    projectBaseInfo.setMarketerId(1L);
    projectBaseInfo.setTesterId(1L);
    projectBaseInfo.setQaId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.REPORT_QA_PASSED, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectInvalidStageException.class,
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.REPORT_QA_DENIED, ""),
                0L,
                Role.QA_SUPERVISOR));
  }

  @Test
  void updateStatusWhenReportWaitSentToCustomerNextInvalidStatus() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    projectBaseInfo.setMarketerId(1L);
    projectBaseInfo.setTesterId(1L);
    projectBaseInfo.setQaId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.REPORT_WAIT_SENT_TO_CUSTOMER, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectInvalidStageException.class,
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.REPORT_QA_PASSED, ""),
                0L,
                Role.QA_SUPERVISOR));
  }

  @Test
  void updateStatusWhenReportWaitCustomerNextInvalidStatus() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    projectBaseInfo.setMarketerId(1L);
    projectBaseInfo.setTesterId(1L);
    projectBaseInfo.setQaId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.REPORT_WAIT_CUSTOMER, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectInvalidStageException.class,
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.REPORT_QA_PASSED, ""),
                0L,
                Role.QA_SUPERVISOR));
  }

  @Test
  void updateStatusWhenReportCustomerConfirmNextInvalidStatus() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    projectBaseInfo.setMarketerId(1L);
    projectBaseInfo.setTesterId(1L);
    projectBaseInfo.setQaId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.REPORT_CUSTOMER_CONFIRM, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectInvalidStageException.class,
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.REPORT_QA_PASSED, ""),
                0L,
                Role.QA_SUPERVISOR));
  }

  @Test
  void updateStatusWhenQaAllRejectedNextInvalidStatus() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    projectBaseInfo.setMarketerId(1L);
    projectBaseInfo.setTesterId(1L);
    projectBaseInfo.setQaId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.QA_ALL_REJECTED, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectInvalidStageException.class,
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.REPORT_QA_PASSED, ""),
                0L,
                Role.QA_SUPERVISOR));
  }

  @Test
  void updateStatusWhenQaAllPassedNextInvalidStatus() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    projectBaseInfo.setMarketerId(1L);
    projectBaseInfo.setTesterId(1L);
    projectBaseInfo.setQaId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.QA_ALL_PASSED, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ProjectInvalidStageException.class,
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.REPORT_QA_PASSED, ""),
                0L,
                Role.QA_SUPERVISOR));
  }

  @Test
  void updateStatusDaoFailure() {
    Project project = new Project();
    ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
    projectBaseInfo.setCustomerId(1L);
    projectBaseInfo.setMarketerId(1L);
    projectBaseInfo.setTesterId(1L);
    projectBaseInfo.setQaId(1L);
    project.setProjectBaseInfo(projectBaseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.REPORT_QA_PASSED, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    when(projectDAO.updateStatus(any(), any())).thenReturn(false);
    Assertions.assertThrows(
        ProjectDAOFailureException.class,
        () ->
            mongoProjectService.updateStatus(
                "projectId",
                new ProjectStatus(ProjectStage.REPORT_WAIT_SENT_TO_CUSTOMER, ""),
                0L,
                Role.QA_SUPERVISOR));
  }

  @Test
  void getProjectFormIdsDaoNull() {
    when(projectDAO.findProjectById(any())).thenReturn(null);
    Assertions.assertThrows(
        ProjectNotFoundException.class, () -> mongoProjectService.getProjectFormIds("projectId"));
  }

  @Test
  void getProjectFormIds() {
    Project project = new Project();
    project.setProjectFormIds(new ProjectFormIds());
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertDoesNotThrow(
        () -> {
          mongoProjectService.getProjectFormIds("projectId");
        });
  }

  @Test
  void getProjectBaseInfo() {
    Project project = new Project();
    project.setProjectBaseInfo(new ProjectBaseInfo());
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertDoesNotThrow(
        () -> {
          mongoProjectService.getProjectBaseInfo("projectId");
        });
  }

  @Test
  void getProjectBaseInfoNull() {
    when(projectDAO.findProjectById(any())).thenReturn(null);
    Assertions.assertThrows(
        ProjectNotFoundException.class, () -> mongoProjectService.getProjectBaseInfo("projectId"));
  }
}
