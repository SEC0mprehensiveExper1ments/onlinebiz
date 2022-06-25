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
    Assertions.assertThrows(
        ProjectPermissionDeniedException.class,
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
  void updateStatus() {}

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
