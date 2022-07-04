package com.njustc.onlinebiz.test.service.report;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.Project;
import com.njustc.onlinebiz.common.model.test.project.ProjectBaseInfo;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.project.ProjectStatus;
import com.njustc.onlinebiz.common.model.test.report.Report;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.dao.report.ReportDAO;
import com.njustc.onlinebiz.test.exception.report.ReportDAOFailureException;
import com.njustc.onlinebiz.test.exception.report.ReportInvalidStageException;
import com.njustc.onlinebiz.test.exception.report.ReportNotFoundException;
import com.njustc.onlinebiz.test.exception.report.ReportPermissionDeniedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MongoReportServiceTest {
  private final ReportDAO reportDAO = mock(ReportDAO.class);
  private final ProjectDAO projectDao = mock(ProjectDAO.class);
  private final MongoReportService reportService = new MongoReportService(reportDAO, projectDao);

  @Test
  void createReportByMarketer() {
    Report report = new Report();
    report.setId("reportId");
    when(reportDAO.insertReport(any())).thenReturn(report);
    Assertions.assertDoesNotThrow(
        () ->
            reportService.createReport(
                "projectId", "entrustId", new Report.ReportContent(), 1L, Role.MARKETER));
  }

  @Test
  void createReportByTester() {
    Report report = new Report();
    report.setId("reportId");
    when(reportDAO.insertReport(any())).thenReturn(report);
    Assertions.assertThrows(
        ReportPermissionDeniedException.class,
        () ->
            reportService.createReport(
                "projectId", "entrustId", new Report.ReportContent(), 1L, Role.TESTER));
  }

  @Test
  void createReportByQa() {
    Report report = new Report();
    report.setId("reportId");
    when(reportDAO.insertReport(any())).thenReturn(report);
    Assertions.assertThrows(
        ReportPermissionDeniedException.class,
        () ->
            reportService.createReport(
                "projectId", "entrustId", new Report.ReportContent(), 1L, Role.QA));
  }

  @Test
  void findReportDaoNull() {
    when(reportDAO.findReportById(any())).thenReturn(null);
    Assertions.assertThrows(
        ReportNotFoundException.class,
        () -> reportService.findReport("reportId", 1L, Role.MARKETER));
  }

  @Test
  void findReportByMarketer() {
    Report report = new Report();
    Project project = new Project();
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setMarketerId(1L);
    project.setProjectBaseInfo(baseInfo);
    when(projectDao.findProjectById(any())).thenReturn(project);
    when(reportDAO.findReportById(any())).thenReturn(report);
    Assertions.assertThrows(
        ReportPermissionDeniedException.class,
        () -> reportService.findReport("reportId", 1L, Role.MARKETER));
  }

  @Test
  void findReportByInvalidTester() {
    Report report = new Report();
    Project project = new Project();
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setTesterId(1L);
    project.setProjectBaseInfo(baseInfo);
    when(projectDao.findProjectById(any())).thenReturn(project);
    when(reportDAO.findReportById(any())).thenReturn(report);
    Assertions.assertThrows(
        ReportPermissionDeniedException.class,
        () -> reportService.findReport("reportId", 2L, Role.TESTER));
  }

  @Test
  void findReportByTester() {
    Report report = new Report();
    Project project = new Project();
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setTesterId(1L);
    project.setProjectBaseInfo(baseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
    when(projectDao.findProjectById(any())).thenReturn(project);
    when(reportDAO.findReportById(any())).thenReturn(report);
    Assertions.assertDoesNotThrow(() -> reportService.findReport("reportId", 1L, Role.TESTER));
  }

  @Test
  void findReportByQa() {
    Report report = new Report();
    Project project = new Project();
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setQaId(1L);
    project.setProjectBaseInfo(baseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
    when(projectDao.findProjectById(any())).thenReturn(project);
    when(reportDAO.findReportById(any())).thenReturn(report);
    Assertions.assertDoesNotThrow(() -> reportService.findReport("reportId", 1L, Role.QA));
  }

  @Test
  void findReportByInvalidQa() {
    Report report = new Report();
    Project project = new Project();
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setQaId(1L);
    project.setProjectBaseInfo(baseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
    when(projectDao.findProjectById(any())).thenReturn(project);
    when(reportDAO.findReportById(any())).thenReturn(report);
    Assertions.assertThrows(
        ReportPermissionDeniedException.class,
        () -> reportService.findReport("reportId", 2L, Role.QA));
  }

  @Test
  void findReportByCustomerInvalidStage() {
    Report report = new Report();
    Project project = new Project();
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setCustomerId(1L);
    project.setProjectBaseInfo(baseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
    when(projectDao.findProjectById(any())).thenReturn(project);
    when(reportDAO.findReportById(any())).thenReturn(report);
    Assertions.assertThrows(
        ReportInvalidStageException.class,
        () -> reportService.findReport("reportId", 1L, Role.CUSTOMER));
  }

  @Test
  void findReportByCustomer() {
    Report report = new Report();
    Project project = new Project();
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setCustomerId(1L);
    project.setProjectBaseInfo(baseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.REPORT_WAIT_CUSTOMER, ""));
    when(projectDao.findProjectById(any())).thenReturn(project);
    when(reportDAO.findReportById(any())).thenReturn(report);
    Assertions.assertDoesNotThrow(() -> reportService.findReport("reportId", 1L, Role.CUSTOMER));
  }

  @Test
  void findReportByInvalidCustomer() {
    Report report = new Report();
    Project project = new Project();
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setCustomerId(1L);
    project.setProjectBaseInfo(baseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.REPORT_WAIT_CUSTOMER, ""));
    when(projectDao.findProjectById(any())).thenReturn(project);
    when(reportDAO.findReportById(any())).thenReturn(report);
    Assertions.assertThrows(
        ReportPermissionDeniedException.class,
        () -> reportService.findReport("reportId", 2L, Role.CUSTOMER));
  }

  @Test
  void findReportInvalidStatus() {
    Report report = new Report();
    Project project = new Project();
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setQaId(1L);
    project.setProjectBaseInfo(baseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
    when(projectDao.findProjectById(any())).thenReturn(project);
    when(reportDAO.findReportById(any())).thenReturn(report);
    Assertions.assertThrows(
        ReportInvalidStageException.class, () -> reportService.findReport("reportId", 1L, Role.QA));
  }

  @Test
  void updateReportDaoNull() {
    when(reportDAO.findReportById(any())).thenReturn(null);
    Assertions.assertThrows(
        ReportNotFoundException.class,
        () ->
            reportService.updateReport("reportId", new Report.ReportContent(), 1L, Role.MARKETER));
  }

  @Test
  void updateReportByTester() {
    Report report = new Report();
    Project project = new Project();
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setTesterId(1L);
    project.setProjectBaseInfo(baseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
    when(projectDao.findProjectById(any())).thenReturn(project);
    when(reportDAO.findReportById(any())).thenReturn(report);
    when(reportDAO.updateContent(any(), any())).thenReturn(true);
    Assertions.assertDoesNotThrow(
        () -> reportService.updateReport("reportId", new Report.ReportContent(), 1L, Role.TESTER));
  }

  @Test
  void updateReportByInvalidTester() {
    Report report = new Report();
    Project project = new Project();
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setTesterId(1L);
    project.setProjectBaseInfo(baseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
    when(projectDao.findProjectById(any())).thenReturn(project);
    when(reportDAO.findReportById(any())).thenReturn(report);
    Assertions.assertThrows(
        ReportPermissionDeniedException.class,
        () -> reportService.updateReport("reportId", new Report.ReportContent(), 2L, Role.TESTER));
  }

  @Test
  void updateReportByCustomer() {
    Report report = new Report();
    Project project = new Project();
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setCustomerId(1L);
    project.setProjectBaseInfo(baseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
    when(projectDao.findProjectById(any())).thenReturn(project);
    when(reportDAO.findReportById(any())).thenReturn(report);
    Assertions.assertThrows(
        ReportPermissionDeniedException.class,
        () ->
            reportService.updateReport("reportId", new Report.ReportContent(), 1L, Role.CUSTOMER));
  }

  @Test
  void updateReportByMarketer() {
    Report report = new Report();
    Project project = new Project();
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setMarketerId(1L);
    project.setProjectBaseInfo(baseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
    when(projectDao.findProjectById(any())).thenReturn(project);
    when(reportDAO.findReportById(any())).thenReturn(report);
    Assertions.assertThrows(
        ReportPermissionDeniedException.class,
        () ->
            reportService.updateReport("reportId", new Report.ReportContent(), 1L, Role.MARKETER));
  }

  @Test
  void updateReportByQa() {
    Report report = new Report();
    Project project = new Project();
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setQaId(1L);
    project.setProjectBaseInfo(baseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
    when(projectDao.findProjectById(any())).thenReturn(project);
    when(reportDAO.findReportById(any())).thenReturn(report);
    Assertions.assertThrows(
        ReportPermissionDeniedException.class,
        () -> reportService.updateReport("reportId", new Report.ReportContent(), 1L, Role.QA));
  }

  @Test
  void updateReportInvalidStatus() {
    Report report = new Report();
    Project project = new Project();
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setTesterId(1L);
    project.setProjectBaseInfo(baseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
    when(projectDao.findProjectById(any())).thenReturn(project);
    when(reportDAO.findReportById(any())).thenReturn(report);
    Assertions.assertThrows(
        ReportInvalidStageException.class,
        () -> reportService.updateReport("reportId", new Report.ReportContent(), 1L, Role.TESTER));
  }

  @Test
  void updateReportDaoFailure() {
    Report report = new Report();
    Project project = new Project();
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setTesterId(1L);
    project.setProjectBaseInfo(baseInfo);
    project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
    when(projectDao.findProjectById(any())).thenReturn(project);
    when(reportDAO.findReportById(any())).thenReturn(report);
    when(reportDAO.updateContent(any(), any())).thenReturn(false);
    Assertions.assertThrows(
        ReportDAOFailureException.class,
        () -> reportService.updateReport("reportId", new Report.ReportContent(), 1L, Role.TESTER));
  }

  @Test
  void deleteReportByAdmin() {
    Report report = new Report();
    when(reportDAO.findReportById(any())).thenReturn(report);
    when(reportDAO.deleteReport(any())).thenReturn(true);
    Assertions.assertDoesNotThrow(() -> reportService.deleteReport("reportId", 1L, Role.ADMIN));
  }

  @Test
  void deleteReportByTester() {
    Assertions.assertThrows(
        ReportPermissionDeniedException.class,
        () -> reportService.deleteReport("reportId", 1L, Role.TESTER));
  }

  @Test
  void deleteReportByQa() {
    Assertions.assertThrows(
        ReportPermissionDeniedException.class,
        () -> reportService.deleteReport("reportId", 1L, Role.QA));
  }

  @Test
  void deleteReportNotExisted() {
    when(reportDAO.findReportById(any())).thenReturn(null);
    Assertions.assertThrows(
        ReportNotFoundException.class,
        () -> reportService.deleteReport("reportId", 1L, Role.ADMIN));
  }

  @Test
  void deleteReportDaoFailure() {
    Report report = new Report();
    when(reportDAO.findReportById(any())).thenReturn(report);
    when(reportDAO.deleteReport(any())).thenReturn(false);
    Assertions.assertThrows(
        ReportDAOFailureException.class,
        () -> reportService.deleteReport("reportId", 1L, Role.ADMIN));
  }
}
